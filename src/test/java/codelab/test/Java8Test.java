package codelab.test;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Test;

public class Java8Test {

	Collection<Long> _primes, _primes2;
	
	@Test
	public void execute() {
		execute("findPrime", () -> findPrime(this::isPrime));
		execute("findPrime2", () -> findPrime(this::isPrime2));
	}
	
	@Test
	public void lambdaExpression() {
		final Predicate<Long> isPrime = this::isPrime;
		final Function<Long,Long> isPrime2 = (a) -> (a+1);
		final Consumer<Long> consumer1 = (a) -> System.out.println(a);
		final Supplier<Long> supplier1 = () -> 1L;
	}

	@Test
	public void parallelStream() {
		List<Integer> l = Arrays.asList(1, 2, 3, 4);
		List<Integer> l2 = l.parallelStream().filter((i) -> i % 2 == 0).map((i) -> i+1).collect(Collectors.toList());
		assertEquals( Arrays.asList(3, 5), l2 );
	}
	
	@Test
	public void map() {
		Map<String,String> m = new HashMap<>();
		String v = m.getOrDefault("a", "???");
		m.replace("b", "xxx");
		assertEquals("???", v);
		assertEquals(null, m.get("b"));
	}
	
	void execute(String name, Runnable runnable) {
		System.out.printf("========== %s ==========\n", name);
		final Instant start = Instant.now();
		runnable.run();
		final Instant end = Instant.now();
		final Duration duration = Duration.between(start, end);
		System.out.printf("Elapsed time: %.3f s\n", duration.toMillis() / 1000.0);
	}

	boolean isPrime(long n) {
		if (n <= 3) {
			return n > 1;
		} else if (n % 2 == 0 || n % 3 == 0) {
			return false;
		} else {
			double sqrtN = Math.floor(Math.sqrt(n));
			int i = 5;
			while (i <= sqrtN) {
				if (n % i == 0 || n % (i+2) == 0) {
					return false;
				}
				i += 6;
			}
			return true;
		}
	}
	
	
	Collection<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101);
	
	boolean isPrime2(long n) {
		if (n <= 3) {
			return n > 1;
		} else {
			for (int prime : primes) {
				if (n == prime) {
					return true;
				} else if (n % prime == 0) {
					return false;
				}
			}
			double sqrtN = Math.floor(Math.sqrt(n));
			for (int i=5; i<=sqrtN; i+=6) {
				if (n % i == 0 || n % (i+2) == 0) {
					return false;
				}
			}
			return true;
		}
	}
	
	void findPrime(Predicate<Long> predicate) {
		Collection<Long> primes = findPrimes(0, 5000000, predicate);
		System.out.println("found " + primes.size() + " primes");
	}

	Collection<Long> findPrimes(int a, int b, Predicate<Long> predicate) {
		Collection<Long> primes = new ArrayList<>();
		for (long i=a; i<=b; i++) {
			if (predicate.test(i)) {
				primes.add(i);
			}
		}
		return primes;
	}

}
