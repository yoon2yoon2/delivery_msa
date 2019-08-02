package com.example.template;

import java.io.Serializable;
import java.util.Map;

public class OrderRequested implements Serializable {

    private String type;
    private String stateMessage = "주문이 발생함";

    private Long code;
    private String userId;
    private double total;//price*quantity 총가격
    private String productCode;
    private int quantity;
    

    public OrderRequested(){
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

   public void setStateMessage(String stateMessage) {
      this.stateMessage = stateMessage;
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
   
   

}
