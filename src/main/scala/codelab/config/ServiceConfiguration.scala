package codelab.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.util.Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
@Import(Array(classOf[core.config.ServiceConfiguration], classOf[DaoConfiguration]))
@ComponentScan(basePackages = Array("codelab.service"), lazyInit = true)
class ServiceConfiguration {

	@Bean
	def testProps : Properties = {
		val props = new Properties
		props.load(getClass.getResourceAsStream("/test.properties"))
		props
	}
}