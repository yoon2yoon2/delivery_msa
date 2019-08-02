package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
public class Delivery {

	@Id
	@GeneratedValue
	private Long deliveryId;
	private Long code;
	private String userId;
	private double total;// price*quantity 총가격
	private String productCode;
	private int quantity;

	private String deliveryState;
	private String addr;// hard coding rest pool API;

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@PostPersist
	private void publishDeliveryStart() {
		KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;

		DeliveryStarted deliveryStarted = new DeliveryStarted();
		deliveryStarted.setOrderCode(this.getCode());
		
		try {
			BeanUtils.copyProperties(this, deliveryStarted);
			json = objectMapper.writeValueAsString(deliveryStarted);
		} 
		catch (JsonProcessingException e) {
			throw new RuntimeException("JSON format exception", e);
		}

		if (json != null) {
			ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
			kafkaTemplate.send(producerRecord);
		}
	}

}
