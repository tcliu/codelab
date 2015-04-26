package codelab.config

import java.util.Locale
import javax.annotation.Resource
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{MultipartConfigElement, ServletContext}

import codelab.service.AppService
import core.Logging
import core.service.ResourceService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.embedded.ServletContextInitializer
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration, Lazy}
import org.springframework.web.servlet.config.annotation.{DefaultServletHandlerConfigurer, EnableWebMvc}
import org.thymeleaf.Arguments
import org.thymeleaf.messageresolver.MessageResolution

@Lazy
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = Array("codelab.web"), lazyInit = true)
@EnableAutoConfiguration
class WebConfiguration extends Logging {

	@Resource var appService : AppService = _
	@Resource var resourceService : ResourceService = _

	@Value("${server.context_path}") var contextPath : String = _
	@Value("${server.port:8080}") var port : String = _

	@Bean
	def servletContextInitializer = new ServletContextInitializer {

		override def onStartup(servletContext: ServletContext) {
			logger.info(s"Servlet context: ${servletContext}")
		}
	}

	@Bean
	def webMvcConfigurerAdapter = new org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter {

		override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) {
			configurer.enable
		}
	}

	@Bean
	def jspViewResolver = {
		val viewResolver = new org.springframework.web.servlet.view.InternalResourceViewResolver
		viewResolver.setViewClass(classOf[org.springframework.web.servlet.view.JstlView])
        viewResolver.setPrefix("/WEB-INF/jsp/")
       // viewResolver.setSuffix(".jsp")
        viewResolver.setViewNames("*jsp")
        viewResolver.setOrder(5)
        viewResolver
	}

	@Bean
	def localeResolver = new LocaleResolver

	// Thymeleaf

	@Bean
	def thymeleafViewResolver = {
		val viewResolver = new org.thymeleaf.spring4.view.ThymeleafViewResolver
		viewResolver.setTemplateEngine(templateEngine)
		viewResolver.setViewNames(Array("*"))
		viewResolver.setCharacterEncoding("UTF-8")
		viewResolver.setOrder(1)
		viewResolver
	}

	@Bean
	def templateEngine  = {
		val templateEngine = new org.thymeleaf.spring4.SpringTemplateEngine
		templateEngine.addTemplateResolver(templateResolver)
		templateEngine.setMessageResolver(messageResolver)
		templateEngine
	}

	@Bean
	def templateResolver = {
		val templateResolver = new org.thymeleaf.templateresolver.ServletContextTemplateResolver
		templateResolver.setPrefix("/WEB-INF/templates/")
		templateResolver.setSuffix(".html")
		templateResolver.setTemplateMode("HTML5")
		templateResolver.setCacheTTLMs(1000)
		templateResolver.setCharacterEncoding("UTF-8")
		templateResolver.setOrder(1)
		templateResolver
	}

	@Bean
	def messageResolver = {
		val messageResolver = new MessageResolver
		messageResolver.setOrder(2)
		messageResolver
	}

	// file upload

	@Bean
	def multipartConfigElement = new MultipartConfigElement("", appService.prop("upload.maxFileSize").toLong,
			appService.prop("upload.maxRequestSize").toLong, appService.prop("upload.sizeThreshold").toInt)

	@Bean
	def multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver

	class LocaleResolver extends org.springframework.web.servlet.i18n.AbstractLocaleResolver {

		lazy val LOCALE = "_locale"

		override def resolveLocale(request: HttpServletRequest) : Locale = {
			var locale = request.getSession.getAttribute(LOCALE).asInstanceOf[Locale]
			val lang = request.getParameter("lang")
			if (lang != null) {
				val newLocale = new Locale(lang)
				if (locale == null || !locale.equals(newLocale)) {
					locale = newLocale
					request.getSession.setAttribute(LOCALE, locale : Locale)
				}
			}
			if (locale == null)
				locale = Locale.getDefault
			locale
		}

		override def setLocale(request: HttpServletRequest, response: HttpServletResponse, locale: Locale) {
			response.setLocale(locale)
		}
	}

	class MessageResolver extends org.thymeleaf.messageresolver.AbstractMessageResolver with Logging {

		override def resolveMessage(args: Arguments, key: String, params: Array[Object]) : MessageResolution = {
			val locale = args.getContext().getLocale();
			val tokens = key.split("/")
			var message : String = null
			try {
				if (tokens.length == 2) {
					message = resourceService.getProperty("properties/" + tokens(0), tokens(1), locale)
				} else {
		        	message = resourceService.getProperty("properties/messages", key, locale)
				}
			} catch {
				case e : Throwable => message = key
			}
	        return new MessageResolution(message);
		}
	}

}