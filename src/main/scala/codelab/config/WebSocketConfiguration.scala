package codelab.config

import core.Logging
import org.springframework.context.annotation.{Bean, Configuration, Lazy}
import org.springframework.http.server.{ServerHttpRequest, ServerHttpResponse}
import org.springframework.messaging.simp.config.{ChannelRegistration, MessageBrokerRegistry}
import org.springframework.web.socket.{CloseStatus, WebSocketHandler, WebSocketMessage, WebSocketSession}
import org.springframework.web.socket.config.annotation.{AbstractWebSocketMessageBrokerConfigurer, EnableWebSocket, EnableWebSocketMessageBroker, StompEndpointRegistry, WebSocketConfigurer, WebSocketHandlerRegistry}
import org.springframework.web.socket.server.{HandshakeHandler, HandshakeInterceptor}

@Lazy
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
class WebSocketConfiguration extends Logging {

	@Bean
	def webSocketHandler = new WebSocketHandler {

		override def afterConnectionEstablished(session: WebSocketSession) = {
			logger.info(s"Connection established: ${session}")
		}

		override def handleMessage(session: WebSocketSession, message: WebSocketMessage[_]) = {
			logger.info(s"handle message: ${session}, ${message}")
		}

		override def handleTransportError(session: WebSocketSession, exception: Throwable) = {
			logger.error(s"Transport error: ${session}", exception)
		}

		override def afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) = {
			logger.info(s"Connection closed: ${session}")
		}

		override def supportsPartialMessages = true
	}

	@Bean
	def webSocketConfigurer = new WebSocketConfigurer {

	    override def registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
	    	registry.addHandler(webSocketHandler, "/WebSocketHandler")
	    		.addInterceptors(handshakeInterceptor)
	    }
	}

	@Bean
	def webSocketMessageBrokerConfigurer = new AbstractWebSocketMessageBrokerConfigurer {

		/**
		 * Configure message broker options.
		 */
		override def configureMessageBroker(registry: MessageBrokerRegistry) {
			registry.enableSimpleBroker("/topic")
			registry.setApplicationDestinationPrefixes("/app")
		}

		/**
		 * Configure STOMP over WebSocket endpoints.
		 */
		override def registerStompEndpoints(registry: StompEndpointRegistry) {
			registry.addEndpoint("/messaging").withSockJS()
		}

		/**
		 * Configure the {@link org.springframework.messaging.MessageChannel} used for
		 * incoming messages from WebSocket clients. By default the channel is backed
		 * by a thread pool of size 1. It is recommended to customize thread pool
		 * settings for production use.
		 */
		override def configureClientInboundChannel(registration: ChannelRegistration) {
			logger.info(s"Inbound: ${registration}");
		}

		/**
		 * Configure the {@link org.springframework.messaging.MessageChannel} used for
		 * incoming messages from WebSocket clients. By default the channel is backed
		 * by a thread pool of size 1. It is recommended to customize thread pool
		 * settings for production use.
		 */
		override def configureClientOutboundChannel(registration: ChannelRegistration) {
			logger.info(s"Outbound: ${registration}");
		}

	}

	@Bean
	def handshakeHandler = new HandshakeHandler {
		override def doHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler,
			attributes: java.util.Map[String, Object]) = true
	}

	@Bean
	def handshakeInterceptor = new HandshakeInterceptor {

		override def beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: java.util.Map[String,Object]) : Boolean = {
			true
		}

		override def afterHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, exception: Exception) = {

		}
	}
}
