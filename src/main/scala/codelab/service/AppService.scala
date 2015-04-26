package codelab.service

import org.springframework.stereotype.Service
import core.service.DefaultAppService

trait AppService extends DefaultAppService {

	def uploadDir : String

}
