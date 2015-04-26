package codelab.test

import scala.collection.mutable.ArrayBuffer

object ConceptChecker {

	def isPrime(n: Int) : Boolean = {
		if (n <= 3) {
			return n > 1
		} else if (n % 2 == 0 || n % 3 == 0) {
			return false
		} else {
			val sqrtN = Math.floor(Math.sqrt(n))
			var i = 5
			while (i <= sqrtN) {
				if (n % i == 0 || n % (i+2) == 0) {
					return false
				}
				i += 6
			}
			return true
		}
	}

	def findPrimes(a : Int, b : Int) : Seq[Int] = {
		val primes = new ArrayBuffer[Int]
		var n = a
		while (n <= b) {
			if (isPrime(n)) {
				primes += n
			}
			n += 1
		}
		primes
	}

	def main(args: Array[String]) {
		println("Hello world!")
		val primes = findPrimes(0, 10000)
		println( primes.mkString(" ") )
	}

}