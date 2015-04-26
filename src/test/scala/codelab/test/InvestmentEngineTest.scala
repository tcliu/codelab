package codelab.test

import javax.annotation.Resource
import codelab.service.InvestmentEngine
import org.junit.Test
import scala.collection.mutable.ArrayBuffer
import codelab.service.PricingParameter

class InvestmentEngineTest extends ServiceTest {

	@Resource var investmentEngine : InvestmentEngine = _

	@Test
	def testNextPrice = {
		var nextPrice = 100.0
		val pricingParam = new PricingParameter(0.07, 0.1, 0.01)
		for (i <- 0 until 100) {
			nextPrice = investmentEngine.nextPrice(nextPrice, pricingParam)
			logger.info(f"Price(${i}) = ${nextPrice}%.3f")
		}
	}

}