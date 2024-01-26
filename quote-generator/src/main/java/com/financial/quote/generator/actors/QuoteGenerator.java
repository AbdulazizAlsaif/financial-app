package com.financial.quote.generator.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.financial.quote.generator.entity.Quote;

public class QuoteGenerator extends AbstractActor {

    private final ActorRef quoteGeneratorProducer;

    public QuoteGenerator() {
        this.quoteGeneratorProducer = getContext().actorOf(QuoteGeneratorProducer.props(),"quote-generator-producer");
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().matchAny(msg->{
            Quote quote = new Quote("company1", Math.random() * 100);
            Quote quote2 = new Quote("company2", Math.random() * 100);
            Quote quote3 = new Quote("company3", Math.random() * 100);
            Quote quote4 = new Quote("company4", Math.random() * 100);
            Quote quote5 = new Quote("company5", Math.random() * 100);
            quoteGeneratorProducer.tell(quote, getSelf());
            quoteGeneratorProducer.tell(quote2, getSelf());
            quoteGeneratorProducer.tell(quote3, getSelf());
            quoteGeneratorProducer.tell(quote4, getSelf());
            quoteGeneratorProducer.tell(quote5, getSelf());

        }).build();
    }

    public static Props props() {
        return Props.create(QuoteGenerator.class);
    }
}
