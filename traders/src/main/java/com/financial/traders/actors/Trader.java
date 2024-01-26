package com.financial.traders.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.financial.traders.entity.Quote;

public class Trader extends AbstractActor {
    LoggingAdapter log= Logging.getLogger(getContext().getSystem(),this);

    private final Double amount;

    public Trader(Double amount) {
        this.amount = amount;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Quote.class,quote -> {
            log.info("message received to trader , {}",quote);

            //TODO: buy and sell logic
        }).build();
    }

    public static Props props(Double amount){
        return Props.create(Trader.class ,amount);
    }
}
