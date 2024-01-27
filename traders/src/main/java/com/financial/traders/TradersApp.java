package com.financial.traders;

import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import com.financial.traders.actors.TradersManager;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

public class TradersApp {


    public static void main(String[] args) {
        System.out.println("TradersApp");
        ActorSystem system= ActorSystem.create("traders-system");

        ConsumerSettings<String ,String> consumerSettings=ConsumerSettings.apply(system,new StringDeserializer(),new StringDeserializer())
            .withGroupId("traders")
            .withBootstrapServers("localhost:9092")
            .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
            .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        system.actorOf(TradersManager.props(consumerSettings),"traders-manager");
    }
}
