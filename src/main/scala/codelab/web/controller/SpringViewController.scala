package codelab.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.ui.Model
import javax.annotation.Resource
import codelab.service.AppService
import core.BaseComponent
import scala.io.Source
import java.io.File

@Controller
@RequestMapping(produces = Array("text/html;charset=utf-8"))
class SpringViewController extends BaseComponent {

	@Resource var appService : AppService = _

	@ModelAttribute("app") def app = appService

    @RequestMapping(value = Array("/"), method = Array(RequestMethod.GET))
    def index(model: Model) = "index"

//	@RequestMapping(value = Array("/error"))
	def error(model: Model) : String = "error"

    @RequestMapping(value = Array("/greeting"), method = Array(RequestMethod.GET))
    def greeting(@RequestParam(value="name", required=false, defaultValue="World") name : String, model : Model) = {
        model.addAttribute("name", name);
        "greeting"
    }

	@RequestMapping(value = Array("/chat"))
	def chat(model: Model) = "chat"

	@ResponseBody
	@RequestMapping(value = Array("/availability"), produces = Array("text/plain"))
	def availability(model: Model) = Source.fromFile(new File("../iphone/src/main/resources/availability.json")).mkString

}