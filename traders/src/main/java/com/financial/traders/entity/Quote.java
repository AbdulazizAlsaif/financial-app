package com.financial.traders.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class Quote implements Serializable {
    private String companyName;
    private double price;

    public Quote() {
    }

    public Quote(String companyName, double price) {
        this.companyName = companyName;
        this.price = price;
    }
    public static Quote fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Quote.class);
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return null;
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Quote{" +
            "companyName='" + companyName + '\'' +
            ", price=" + price +
            '}';
    }
}
