package com.financial.auditor.entity;

import java.io.Serializable;

public record Quote(String companyName, double price) implements Serializable {

    @Override
    public String toString() {
        return "Quote{" +
            "companyName='" + companyName + '\'' +
            ", price=" + price +
            '}';
    }
}
