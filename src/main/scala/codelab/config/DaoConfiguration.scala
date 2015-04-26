package codelab.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
@Import(Array(classOf[core.config.DaoConfiguration]))
@ComponentScan(basePackages = Array("codelab.dao"), lazyInit = true)
class DaoConfiguration {

}