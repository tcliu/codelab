package codelab.config

import org.springframework.context.annotation.{Configuration, Import, ImportResource, Lazy}

@Lazy
@Configuration
@Import(Array(classOf[ServiceConfiguration], classOf[WebConfiguration], classOf[WebSocketConfiguration]))
@ImportResource(Array("/META-INF/app-context.xml"))
class AppConfiguration {

}