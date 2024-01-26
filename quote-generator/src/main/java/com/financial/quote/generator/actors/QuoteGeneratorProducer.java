package com.financial.quote.generator.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.kafka.ProducerSettings;
import com.financial.quote.generator.entity.Quote;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class QuoteGeneratorProducer extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final ProducerSettings<String, String> producerSettings;
    private Producer<String,String> kafkaProducer;
    public QuoteGeneratorProducer() {
        this.producerSettings = ProducerSettings.create(getContext().getSystem(), new StringSerializer(), new StringSerializer())
            .withBootstrapServers("localhost:9092");
        this.kafkaProducer = producerSettings.createKafkaProducer();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Quote.class, quote -> {
                String kakfaTopic="financial-quotes";
                ProducerRecord<String, String> record = new ProducerRecord<>(kakfaTopic, quote.getCompanyName(), String.valueOf(quote.getPrice()));
                kafkaProducer.send(record);
                log.info("Producing to kafka topic {} , with values {}",kakfaTopic,quote);
            })
            .build();
    }

    public static Props props() {
        return Props.create(QuoteGeneratorProducer.class);
    }

}
