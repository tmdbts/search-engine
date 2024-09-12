package pt.uc.dei.student.tmdbts.search_engine.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    /**
     * Websocket endpoint
     */
    public static final String WEBSOCKET_ENDPOINT = "/websocket";

    /**
     * Monitor topic
     */
    public static final String TOPIC_MONITOR = "/topic/monitor";

    /**
     * Configure message broker
     *
     * @param config message broker registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Register stomp endpoints
     *
     * @param registry stomp endpoint registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_ENDPOINT).withSockJS();
    }
}
