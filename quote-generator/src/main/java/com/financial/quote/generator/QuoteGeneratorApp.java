package com.financial.quote.generator;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.financial.quote.generator.actors.QuoteGenerator;

import java.time.Duration;

public class QuoteGeneratorApp {


    public static void main(String[] args) {
        System.out.println("---- QuoteGeneratorApp Started----");
        ActorSystem system = ActorSystem.create("QuoteGeneratorSystem");
        ActorRef quoteGenerator = system.actorOf(QuoteGenerator.props(), "quote-generator");
        system.scheduler().scheduleAtFixedRate(
            Duration.ofMillis(0),
            Duration.ofMillis(1000),
            new Runnable() {
                @Override
                public void run() {
                    quoteGenerator.tell(System.currentTimeMillis(), ActorRef.noSender());
                }
            },
            system.dispatcher()
        );


        system.registerOnTermination(new Runnable() {
            @Override
            public void run() {
                System.out.println("---- QuoteGeneratorApp Terminated----");

            }
        });

    }
}
