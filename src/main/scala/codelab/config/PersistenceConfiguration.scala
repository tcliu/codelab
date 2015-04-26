package codelab.config

import javax.persistence.{EntityManagerFactory, Persistence}

import org.springframework.context.annotation.{Bean, Configuration, Lazy}
import org.springframework.transaction.annotation.EnableTransactionManagement

@Lazy
@Configuration
@EnableTransactionManagement
class PersistenceConfiguration {

	@Bean
	def entityManagerFactory : EntityManagerFactory = {
		Persistence.createEntityManagerFactory("PU-HSQLDB")
	}

}