package com.example.beauty_salon.controllers;

import com.example.beauty_salon.AnalyticsServiceGrpc;
import com.example.beauty_salon.AccommodationRatingRequest;
import com.example.beauty_salon.config.RabbitMQConfig;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.events.AccommodationRatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

    @GrpcClient("analytics-service")
    private AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;

    public RatingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/users/{id}/rate")
    public String rateUser(@PathVariable Long id) {
        // Вызов gRPC
        var request = AccommodationRatingRequest.newBuilder().setAccommodationId(id).setCategory("General").build();
        var gRpcResponse = analyticsStub.calculateAccommodationRating(request);

        // Отправка события в Fanout
        var event = new AccommodationRatedEvent(gRpcResponse.getAccommodationId(), gRpcResponse.getRatingScore(), gRpcResponse.getVerdict());

        // Для Fanout routingKey не важен, оставляем пустым ""
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", event);

        return "Rating calculated: " + gRpcResponse.getRatingScore();
    }
}
