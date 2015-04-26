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
import org.springframework.ui.Model
import scala.io.Source

@RestController
@RequestMapping(produces = Array("text/plain;charset=utf-8"))
class SpringRestController extends BaseComponent {

	@Resource var resourceService : ResourceService = _
	@Resource var formatService : FormatService = _

    @RequestMapping(value = Array("/test/{name}"))
    def hello(@PathVariable name: String) = s"Hello, ${name}"

    /**
     * Compiles a LESS file and output the CSS content
     */
	@RequestMapping(value = Array("/less/{path:.+}"), produces = Array("text/css;charset=utf-8"))
	def compileLess(@PathVariable path: String, request: HttpServletRequest) = {
		val servletContext = request.getServletContext
    	val file = new File(servletContext.getRealPath("/less/" + path))
		if (file.isFile) resourceService.compileLess(file) else ""
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

	@RequestMapping(value = Array("/availability"))
	def availability(model: Model) = Source.fromFile(new File("../iphone/src/main/resources/availability.json")).mkString

}