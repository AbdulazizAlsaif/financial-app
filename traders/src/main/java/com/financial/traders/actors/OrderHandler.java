package com.financial.traders.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.Http;
import akka.http.javadsl.HttpsConnectionContext;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.financial.traders.entity.Order;

import javax.net.ssl.SSLContext;
import java.util.concurrent.CompletionStage;

public class OrderHandler extends AbstractActor {
    LoggingAdapter log= Logging.getLogger(getContext().getSystem(),this);

    private Http http = Http.get(getContext().getSystem());

    private final ObjectWriter objectMapper;

    public OrderHandler() {
        this.objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Order.class, (order) -> {
            log.info("Rest req with order, {}",order);
            HttpRequest request = HttpRequest.POST("http://localhost:8080/audit-svc/api/v1/orders").withEntity(ContentTypes.APPLICATION_JSON, objectMapper.writeValueAsBytes(order));
            CompletionStage<HttpResponse> responseCompletionStage=  http.singleRequest(request, HttpsConnectionContext.httpsClient(SSLContext.getDefault()));
                     responseCompletionStage.whenComplete((response, throwable) -> {
                         if (throwable != null || response.status().isFailure()) {
                             getContext().getParent().tell(new Trader.Refund(order.getActionType(),order.getQuote().getPrice()),getSelf());
                         }
                         response.discardEntityBytes(Materializer.createMaterializer(getContext()));
                         });
        }).build();
    }

    public static Props props(){
        return Props.create(OrderHandler.class);
    }
}
