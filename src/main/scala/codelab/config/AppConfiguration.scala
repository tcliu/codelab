package codelab.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
@Import(Array(classOf[ServiceConfiguration], classOf[WebConfiguration], classOf[WebSocketConfiguration]))
@ImportResource(Array("/META-INF/app-context.xml"))
class AppConfiguration {

}