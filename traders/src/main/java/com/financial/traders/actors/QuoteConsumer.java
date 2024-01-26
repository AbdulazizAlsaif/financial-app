package com.financial.traders.actors;

import akka.Done;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.routing.Router;
import akka.stream.Materializer;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.traders.entity.Quote;

import java.util.concurrent.CompletionStage;

public class QuoteConsumer extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final CompletionStage<Done> drainingControl;

    private final Router traders;

    public QuoteConsumer(ConsumerSettings<String, String> consumerSettings, Router traders) {
        this.drainingControl = createConsumer(consumerSettings);
        this.traders = traders;
    }

    private CompletionStage<Done> createConsumer(ConsumerSettings<String, String> consumerSettings) {
        return Consumer.plainSource(consumerSettings, Subscriptions.topics("financial-quotes"))
            .map(record -> {
                processMessage(record.value());
                return record.value();
            })
            .toMat(Sink.ignore(), Keep.both())
            .mapMaterializedValue(Pair::second)
            .run(Materializer.createMaterializer(getContext()));
    }

    private void processMessage(String message)  {
        ObjectMapper objectMapper= new ObjectMapper();
        try {
           Quote quote= objectMapper.readValue(message, Quote.class);
           traders.route(quote,ActorRef.noSender());
        }catch (Exception ignored){
        }

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    @Override
    public void postStop() {
        drainingControl.toCompletableFuture().join();
    }

    public static Props props(ConsumerSettings<String, String> consumerSettings,Router traders) {
        return Props.create(QuoteConsumer.class, consumerSettings,traders);
    }
}
