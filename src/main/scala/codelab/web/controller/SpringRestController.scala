package codelab.web.controller

import scala.collection.JavaConversions._
import java.io.File
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import core.service.ResourceService
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import java.util.Locale
import org.springframework.web.bind.annotation.RequestParam
import core.service.FormatService
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import core.BaseComponent

@RestController
@RequestMapping(produces = Array("text/plain;charset=utf-8"))
class SpringRestController extends BaseComponent {

	@Resource var resourceService : ResourceService = _
	@Resource var formatService : FormatService = _

    @RequestMapping(value = Array("/rest"))
    def home = "Hello World"

    @RequestMapping(value = Array("/test/{name}"))
    def hello(@PathVariable name: String) = String.format("Hello, %s!", name)

    /**
     * Compiles a LESS file and output the CSS content
     */
	@RequestMapping(value = Array("/css/{path:.+}"), produces = Array("text/css;charset=utf-8"))
	def compileLess(@PathVariable path: String, request: HttpServletRequest) = {
		val servletContext = request.getServletContext
    	val file = new File(servletContext.getRealPath("/less/" + path))
    	val css = resourceService.compileLess(file)
    	css
    }

	/**
	 * Loads one or more properties from a resource bundle
	 */
	@RequestMapping(value = Array("/i18n"))
	def getProperty(@RequestParam bundleName: String, @RequestParam(required = false) key: String, locale: Locale) = {
		if (key == null) {
			val map = resourceService.getProperties(bundleName, locale)
			formatService.toJsonString(map)
		} else {
			resourceService.getProperty(bundleName, key, locale)
		}
	}

}