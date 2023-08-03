package com.example.karu_android_app;

public class exhibitionPostInfo {

    String eventName;
    String eventPlace;
    String eventHost;
    String eventDate;
    double hostNID;
    double ticketPrice;
    double paymentNum;
    String eventLogo;

    public exhibitionPostInfo() {
    }

    public exhibitionPostInfo(String eventName, String eventPlace, String eventHost, String eventDate, double hostNID, double ticketPrice, double paymentNum, String eventLogo) {
        this.eventName = eventName;
        this.eventPlace = eventPlace;
        this.eventHost = eventHost;
        this.eventDate = eventDate;
        this.hostNID = hostNID;
        this.ticketPrice = ticketPrice;
        this.paymentNum = paymentNum;
        this.eventLogo = eventLogo;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public String getEventHost() {
        return eventHost;
    }

    public String getEventDate() {
        return eventDate;
    }

    public double getHostNID() {
        return hostNID;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public double getPaymentNum() {
        return paymentNum;
    }

    public String getEventLogo() {
        return eventLogo;
    }
}
