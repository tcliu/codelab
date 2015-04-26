package codelab.config

import org.springframework.context.annotation.{ComponentScan, Configuration, Import, Lazy}

@Lazy
@Configuration
@Import(Array(classOf[core.config.DaoConfiguration]))
@ComponentScan(basePackages = Array("codelab.dao"), lazyInit = true)
class DaoConfiguration {

}