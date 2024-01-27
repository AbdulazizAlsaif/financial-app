package com.financial.traders.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.kafka.ConsumerSettings;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.financial.traders.entity.Quote;

import java.util.ArrayList;
import java.util.List;

public class TradersManager extends AbstractActor {

    private final Router traders;

    public TradersManager(ConsumerSettings<String,String> consumerSettings) {
        getContext().actorOf(QuoteConsumer.props(consumerSettings),"quote-consumer");
        List<Routee> tradersBroadcastRouter = new ArrayList<Routee>();
        for (int i = 0; i < 5; i++) {
            String traderName="trader"+i;
            ActorRef actor = getContext().actorOf(Trader.props(1500.0,traderName) , traderName);
            tradersBroadcastRouter.add(new ActorRefRoutee(actor));
        }
        this.traders = new Router(new BroadcastRoutingLogic(), tradersBroadcastRouter);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Quote.class,quote -> {
            traders.route(quote,self());
        }).build();
    }

    public static Props props(ConsumerSettings<String,String> consumerSettings){
        return Props.create(TradersManager.class,consumerSettings);
    }

}
