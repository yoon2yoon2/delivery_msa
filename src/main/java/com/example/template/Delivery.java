package com.example.template;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Delivery {

    @Id @GeneratedValue
    private Long deliveryId;
    private Long orderId;
    private String deliveryAddress;
    private String deliveryState;

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

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
            deliveryStarted.setOrderId(this.getOrderId());
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
