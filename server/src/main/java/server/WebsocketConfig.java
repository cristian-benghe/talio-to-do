package server;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * Overrides the default implementation of the 'registerStompEndpoints'
     * method from the 'WebSocketMessageBrokerConfigurer' interface.
     * Registers the '/websocket' endpoint with the specified 'StompEndpointRegistry'.
     * @param registry the registry used to register the '/websocket' endpoint
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket");
    }

    /**
     * Overrides the default implementation of the 'configureMessageBroker'
     * method from the 'WebSocketMessageBrokerConfigurer' interface.
     * Configures the message broker registry with the specified 'config'.
     * This method enables a simple message broker with the specified '/topic' prefix,
     * which will be used to handle subscription messages
     * and send messages to subscribed clients. It also sets the application destination
     * prefix to '/app', which is used to distinguish
     * messages that are intended for application-specific destinations.
     * @param config the message broker registry configuration to be used
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

}
