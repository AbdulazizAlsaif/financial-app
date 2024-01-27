package com.financial.traders.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.financial.traders.entity.ActionType;
import com.financial.traders.entity.Order;
import com.financial.traders.entity.Quote;
import java.util.Random;

public class Trader extends AbstractActor  {


    public interface Command{}

    public static class Refund implements Command{

        private final ActionType actionType;
        private final Double amount;

        public Refund(ActionType actionType, Double amount) {
            this.actionType = actionType;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "Refund{" +
                "actionType=" + actionType +
                ", amount=" + amount +
                '}';
        }
    }

    LoggingAdapter log= Logging.getLogger(getContext().getSystem(),this);
    private  Double amount;
    private final String traderName;

    private final ActorRef orderHandler;


    public Trader(Double amount, String traderName) {
        this.amount = amount;
        this.traderName = traderName;
        this.orderHandler=getContext().actorOf(OrderHandler.props(), "order-handler");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Quote.class,quote -> {
            if(quote.getPrice()<amount && new Random().nextBoolean()) {
                log.info("Buying with current amount : {}, quote :{}", amount, quote );
                Order order = new Order();
                order.setActionType(ActionType.BUY);
                order.setQuote(quote);
                order.setTraderName(traderName);
                amount=amount-quote.getPrice();
                orderHandler.tell(order,getSelf());
            }else if (new Random().nextBoolean()){
                log.info("Selling with current amount : {}, quote :{}", amount, quote);
                Order order = new Order();
                order.setActionType(ActionType.SELL);
                order.setQuote(quote);
                order.setTraderName(traderName);
                amount=amount+quote.getPrice();
                orderHandler.tell(order,getSelf());
                }
        }).match(Refund.class,refund -> {
            log.info("Processing Refund with , {}",refund.toString() );
            if(refund.actionType== ActionType.BUY){
                amount=amount+ refund.amount;
            }else amount=amount-refund.amount;
            })
            .build();
    }

    public static Props props(Double amount, String traderName){
        return Props.create(Trader.class ,amount,traderName);
    }
}
