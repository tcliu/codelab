package codelab.service

import org.springframework.stereotype.Component
import scala.util.Random
import scala.math.sqrt
import org.apache.commons.math3.distribution.NormalDistribution

class PricingParameter(val volatility: Double, val drift: Double, val dt: Double)

@Component
class InvestmentEngine {

	val random = new Random
	val normDist = new NormalDistribution(0.0, 1.0)
	
	def nextPrice(lastPrice: Double, param: PricingParameter) : Double = 
		lastPrice + priceChange(lastPrice, param)
	
	def priceChange(lastPrice: Double, param: PricingParameter) : Double = {
		val drift = param.drift * param.dt * lastPrice
		val uncertainty = normDist.inverseCumulativeProbability(random.nextDouble) * sqrt(param.dt) * param.volatility * lastPrice
		val change = drift + uncertainty
		change
	}
	
}