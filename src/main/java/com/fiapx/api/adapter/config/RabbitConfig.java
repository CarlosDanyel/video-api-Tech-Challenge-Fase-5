package com.fiapx.api.adapter.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "fiapx.events";
    public static final String JOB_QUEUE = "video.jobs";
    public static final String RESULT_QUEUE = "video.results";
    public static final String NOTIFICATION_QUEUE = "video.notifications";
    @Bean DirectExchange deadExchange() { return new DirectExchange("fiapx.dead", true, false); }
    @Bean DirectExchange exchange() { return new DirectExchange(EXCHANGE, true, false); }
    @Bean Queue jobs() { return QueueBuilder.durable(JOB_QUEUE).deadLetterExchange("fiapx.dead").deadLetterRoutingKey(JOB_QUEUE).build(); }
    @Bean Queue results() { return QueueBuilder.durable(RESULT_QUEUE).deadLetterExchange("fiapx.dead").deadLetterRoutingKey(RESULT_QUEUE).build(); }
    @Bean Queue notifications() { return QueueBuilder.durable(NOTIFICATION_QUEUE).deadLetterExchange("fiapx.dead").deadLetterRoutingKey(NOTIFICATION_QUEUE).build(); }
    @Bean Queue deadJobs() { return QueueBuilder.durable(JOB_QUEUE + ".dead").build(); }
    @Bean Queue deadResults() { return QueueBuilder.durable(RESULT_QUEUE + ".dead").build(); }
    @Bean Queue deadNotifications() { return QueueBuilder.durable(NOTIFICATION_QUEUE + ".dead").build(); }
    @Bean Binding deadJobsBinding() { return BindingBuilder.bind(deadJobs()).to(deadExchange()).with(JOB_QUEUE); }
    @Bean Binding deadResultsBinding() { return BindingBuilder.bind(deadResults()).to(deadExchange()).with(RESULT_QUEUE); }
    @Bean Binding deadNotificationsBinding() { return BindingBuilder.bind(deadNotifications()).to(deadExchange()).with(NOTIFICATION_QUEUE); }
    @Bean Binding jobsBinding() { return BindingBuilder.bind(jobs()).to(exchange()).with("video.requested"); }
    @Bean Binding resultsBinding() { return BindingBuilder.bind(results()).to(exchange()).with("video.result"); }
    @Bean Binding notificationsBinding() { return BindingBuilder.bind(notifications()).to(exchange()).with("video.failed"); }
}
