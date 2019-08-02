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

    @Id @GeneratedValue
    private Long deliveryId;
    private Long orderCode;
    private String userId;
    private int paymentType;
    //private Map<String,Integer> orderMap;
    private String deliveryState;
    private String addr;//hard coding rest pool API;

    
    
    public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Long getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

//	public Map<String, Integer> getOrderMap() {
//		return orderMap;
//	}
//
//	public void setOrderMap(Map<String, Integer> orderMap) {
//		this.orderMap = orderMap;
//	}

	public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    @PostPersist
    private void publishDeliveryStart() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        if( deliveryState.equals(DeliveryStarted.class.getSimpleName())){
            DeliveryStarted deliveryStarted = new DeliveryStarted();
            deliveryStarted.setOrderCode(this.getOrderCode());
            try {
                BeanUtils.copyProperties(this, deliveryStarted);
                json = objectMapper.writeValueAsString(deliveryStarted);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON format exception", e);
            }
        }

        if( json != null ){
            ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
            kafkaTemplate.send(producerRecord);
        }
    }

}
