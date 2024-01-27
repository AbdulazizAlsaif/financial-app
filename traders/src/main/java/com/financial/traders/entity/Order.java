package com.financial.traders.entity;

public class Order {

    private Quote quote;
    private ActionType actionType;
    private String traderName;

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    @Override
    public String toString() {
        return "Order{" +
            "quote=" + quote +
            ", actionType=" + actionType +
            ", traderName='" + traderName + '\'' +
            '}';
    }
}
