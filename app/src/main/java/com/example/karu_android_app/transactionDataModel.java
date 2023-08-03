package com.example.karu_android_app;

public class transactionDataModel {
    String delivery_address;
    String order_list;
    int total_price;
//    String time_date;

    public transactionDataModel() {
    }

    public transactionDataModel(String delivery_address, String order_list, int total_price) {
        this.delivery_address = delivery_address;
        this.order_list = order_list;
        this.total_price = total_price;
//        this.time_date = time_date;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getOrder_list() {
        return order_list;
    }

    public void setOrder_list(String order_list) {
        this.order_list = order_list;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

//    public String getTime_date() {
//        return time_date;
//    }
//
//    public void setTime_date(String time_date) {
//        this.time_date = time_date;
//    }
}
