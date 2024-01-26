package com.financial.traders;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
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

        List<Routee> routees = new ArrayList<Routee>();
            for (int i = 0; i < 5; i++) {
                ActorRef actor = system.actorOf(Trader.props(1500.00) , "trader"+i);
                routees.add(new ActorRefRoutee(actor));
            }

        Router router = new Router(new BroadcastRoutingLogic(), routees);
        system.actorOf(QuoteConsumer.props(consumerSettings,router),"kafka-consumer");
    }
}
