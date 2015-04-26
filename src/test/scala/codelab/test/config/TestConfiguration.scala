package codelab.test.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource
import codelab.config.AppConfiguration

@Configuration
@ImportResource(Array("/META-INF/test-context.xml"))
class TestConfiguration {

}
