package codelab

import org.springframework.boot.SpringApplication
import core.BaseApp
import codelab.config.AppConfiguration

object WebLauncher extends BaseApp {

	val sources : Array[Object] = Array(classOf[AppConfiguration])
	val ctx = SpringApplication.run(sources, args)
	/*
	logger.info("Registered beans:")
	ctx.getBeanDefinitionNames.zipWithIndex.foreach {
		case (name, i) =>
			val bean = ctx.getBean(name)
			println(s"[${i}] ${name} -> ${bean}")
	}*/

}