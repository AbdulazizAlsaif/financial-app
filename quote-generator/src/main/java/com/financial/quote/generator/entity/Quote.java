package com.financial.quote.generator.entity;

import java.io.Serializable;

public class Quote implements Serializable {
    private final String companyName;
    private final double price;

    public Quote(String companyName, double price) {
        this.companyName = companyName;
        this.price = price;
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
