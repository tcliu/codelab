package codelab.test

import java.util.Properties
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import codelab.service.IPhoneService
import core.Logging

import codelab.test.config.TestConfiguration
import javax.annotation.Resource

object IPhoneReserver extends Logging {

	def main(args: Array[String]) {
		val ctx = new AnnotationConfigApplicationContext(classOf[TestConfiguration])
		val iPhoneService = ctx.getBean(classOf[IPhoneService])
		val scheduler = Executors.newScheduledThreadPool(3)
		val jsonUrl = iPhoneService.getJsonUrl
		var reserving = false

		val reserveRunnable = new Runnable {

			override def run {
				try {
					val jsonStr = iPhoneService.getJsonString(jsonUrl)
					val serviceOpen = iPhoneService.isServiceOpen(jsonStr)
					if (serviceOpen) {
						if (!reserving) {
							reserving = serviceOpen
							val driver : WebDriver = new FirefoxDriver
							iPhoneService.reserveIPhone(driver)
						}
					} else {
						logger.info(s"Service is unavailable ... ${jsonUrl}")
					}
				} catch {
					case e : Exception => e.printStackTrace
				}
			}
		}

		scheduler.scheduleAtFixedRate(reserveRunnable, 0, 5, TimeUnit.SECONDS)
	}

}