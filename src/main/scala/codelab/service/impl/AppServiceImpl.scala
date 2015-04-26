package codelab.service.impl

import core.service.impl.DefaultAppServiceImpl
import codelab.service.AppService
import org.springframework.stereotype.Service

@Service
class AppServiceImpl extends DefaultAppServiceImpl with AppService {

	override def uploadDir = prop("upload.dir")

}