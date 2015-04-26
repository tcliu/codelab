package codelab.web.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest
import org.springframework.web.servlet.ModelAndView
import core.BaseComponent
import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.ui.Model

// @Controller
class ExceptionHandlingController extends BaseComponent with ErrorController {

	val ERROR_PATH = "/error"

	override def getErrorPath(): String = ERROR_PATH

	@RequestMapping(Array("/error"))
	def handleError(req: HttpServletRequest, model : Model, e: Exception) = {
		val mnv = new ModelAndView
		logger.error(s"Request: ${req.getRequestURL} raised ${e}")
		model.addAttribute("exception", e)
		model.addAttribute("url", req.getRequestURL)
		"error"
	}

}