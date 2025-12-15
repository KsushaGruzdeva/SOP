package com.example.beauty_salon.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "appointments-exchange";
    public static final String ROUTING_KEY_APPOINTMENT_CREATED = "appointment.created";
    public static final String ROUTING_KEY_APPOINTMENT_DELETED = "appointment.deleted";
    public static final String FANOUT_EXCHANGE = "analytics-fanout";

    @Bean
    public FanoutExchange analyticsExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange appointmentsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(jsonMessageConverter());
//        return template;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
//        return factory;
//    }
}
