package codelab.config

import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence
import java.util.Properties

@Lazy
@Configuration
@EnableTransactionManagement
class PersistenceConfiguration {

	@Bean
	def entityManagerFactory : EntityManagerFactory = {
		Persistence.createEntityManagerFactory("PU-HSQLDB")
	}

}