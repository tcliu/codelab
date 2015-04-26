package codelab

import resource._
import core.BaseApp
import codelab.config.AppConfiguration
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import codelab.service.AppService

object AppLauncher extends BaseApp {

	profile("main") {
		for (ctx <- managed(new AnnotationConfigApplicationContext(classOf[AppConfiguration]))) {
			val appService = ctx.getBean(classOf[AppService])
			logger.info(s"${appService.name} started ...")
			logger.info(s"Upload directory: ${appService.uploadDir}")
			logger.info(s"Default locale: ${appService.defaultLocale}")
			logger.info(s"Default date format: ${appService.defaultDateFormat}")
			logger.info(s"Default time format: ${appService.defaultTimeFormat}")
			logger.info(s"Default archive date format: ${appService.defaultArchiveDateFormat}")
			logger.info(s"${appService.name} exited.")
		}
	}
	profiler.print

}