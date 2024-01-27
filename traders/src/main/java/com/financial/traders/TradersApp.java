package com.financial.traders;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.routing.*;
import com.financial.traders.actors.QuoteConsumer;
import com.financial.traders.actors.Trader;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.List;

public class TradersApp {


    public static void main(String[] args) {
        System.out.println("TradersApp");

        ActorSystem system= ActorSystem.create("traders-system");

        ConsumerSettings<String ,String> consumerSettings=ConsumerSettings.apply(system,new StringDeserializer(),new StringDeserializer())
            .withGroupId("traders")
            .withBootstrapServers("localhost:9092")
            .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
            .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        List<Routee> tradersBroadcastRouter = new ArrayList<Routee>();
            for (int i = 0; i < 2; i++) {
                String traderName="trader"+i;
                ActorRef actor = system.actorOf(Trader.props(1500.0,traderName) , traderName);
                tradersBroadcastRouter.add(new ActorRefRoutee(actor));
            }

        Router router = new Router(new BroadcastRoutingLogic(), tradersBroadcastRouter);
        system.actorOf(QuoteConsumer.props(consumerSettings,router),"quote-consumer");
    }
}
