package codelab.test

import core.Logging
import core.Profiling
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ActiveProfiles
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import codelab.config.{ServiceConfiguration, DaoConfiguration, PersistenceConfiguration}
import codelab.test.config.TestConfiguration
import codelab.config.AppConfiguration

@RunWith(classOf[SpringJUnit4ClassRunner])
abstract class AppTest extends Logging with Profiling

@ContextConfiguration(classes=Array(classOf[ServiceConfiguration], classOf[TestConfiguration]))
abstract class ServiceTest extends AppTest

@ContextConfiguration(classes=Array(classOf[DaoConfiguration], classOf[PersistenceConfiguration], classOf[TestConfiguration]))
abstract class DaoTest extends AppTest