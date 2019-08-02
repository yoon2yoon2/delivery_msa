package com.example.template;

import java.util.Map;

public class DeliveryStarted {

    private String type ;
    private String stateMessage = "배송이 시작됨";

    private Long deliveryId;
    private Long orderCode;
    private String userId;
    private int paymentType;
 //   private Map<String,Integer> orderMap;
    private String deliveryState;
    
    public Long getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
	public DeliveryStarted(){
        this.setType(this.getClass().getSimpleName());
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStateMessage() {
        return stateMessage;
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
//	public void setOrderMap(Map<String, Integer> orderMap) {
//		this.orderMap = orderMap;
//	}
	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}
	public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }
}
