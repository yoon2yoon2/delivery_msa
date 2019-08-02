package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "eventTopic")
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PaymentCompleted paymentCompleted = null;
        try {
            paymentCompleted = objectMapper.readValue(message, PaymentCompleted.class);

            System.out.println(" #### type = " + paymentCompleted.getType());

            /**
             * 주문이 들어옴 -> 배송 시작 이벤트 발송
             */
            if( paymentCompleted.getType() != null && paymentCompleted.getType().equals(PaymentCompleted.class.getSimpleName())){

                Delivery delivery = new Delivery();
                delivery.setOrderCode(paymentCompleted.getOrderCode());
                delivery.setUserId(paymentCompleted.getUserId());
                delivery.setPaymentType(paymentCompleted.getPaymentType());
                delivery.setDeliveryState(DeliveryStarted.class.getSimpleName());
                deliveryRepository.save(delivery);

            /**
             * 배송이 시작됨 -> 배송 완료 이벤트 발송
             */
            }else if( paymentCompleted.getType() != null && paymentCompleted.getType().equals(DeliveryStarted.class.getSimpleName())){

                DeliveryStarted deliveryStarted = objectMapper.readValue(message, DeliveryStarted.class);

                Delivery delivery = new Delivery();
                delivery.setDeliveryId(deliveryStarted.getDeliveryId());
                delivery.setDeliveryState(DeliveryCompleted.class.getSimpleName());
                deliveryRepository.save(delivery);

                String json = null;

                try {
                    DeliveryCompleted deliveryCompleted = new DeliveryCompleted();
                    deliveryCompleted.setOrderCode(deliveryStarted.getOrderCode());
                    deliveryCompleted.setUserId(deliveryStarted.getUserId());
                    deliveryCompleted.setPaymentType(deliveryStarted.getPaymentType());
                    deliveryCompleted.setDeliveryState(DeliveryCompleted.class.getSimpleName());

                    json = objectMapper.writeValueAsString(deliveryCompleted);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("JSON format exception", e);
                }

                ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
                kafkaTemplate.send(producerRecord);

            }

        }catch (Exception e){

        }
    }
}
