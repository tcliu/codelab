package codelab.service.test

import scala.Array.canBuildFrom
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.assertThat
import org.junit.Test
import codelab.service.AppService
import javax.annotation.Resource
import codelab.test.ServiceTest

class AppServiceTest extends ServiceTest {

	@Resource var appService : AppService = _

	@Test def testName {
		profile("name") {
			assertThat("CodeLab", is(appService.name))
		}
	}

	@Test def testReadProperty {
		profile("prop") {
			assertThat("CodeLab", is(appService.prop("name")))
		}
	}

	@Test def testMethodCount {
		val methods = appService.getClass.getMethods
		methods.zipWithIndex.foreach { case (m,i) => println(s"[${i+1}] ${m}") }

	}

	def testConcurency {

		val c = new ListBuffer[Int]
		val f1 = Future {
			for (i <- 1 to 100) {
				Thread.sleep(10)
				c += i
			}
			Thread.sleep(10)
		}
		val f2 = Future {
			for (i <- 101 to 200) {
				Thread.sleep(10)
				c += i
			}
		}
		f1 onComplete {
			case Success(x) => logger.info(c.toString)
			case Failure(x) => logger.info("failure: " + x)
		}

	}

}