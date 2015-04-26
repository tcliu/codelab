package codelab.web.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import javax.annotation.Resource
import org.apache.commons.lang3.time.DateFormatUtils
import java.util.Calendar
import org.springframework.messaging.simp.SimpMessagingTemplate
import core.BaseComponent

@Controller
class WebSocketController extends BaseComponent {

	@Resource var messagingTemplate: SimpMessagingTemplate = _

	@MessageMapping(Array("/messaging"))
	@SendTo(Array("/topic/messaging"))
	def hello(message: java.util.Map[String,Any]) = {
		message.put("timestamp", System.currentTimeMillis)
		logger.info(s"Incoming message: ${message}")
		message
	}
}