package codelab.test

import java.util.Properties
import org.junit.Test
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.springframework.test.context.ContextConfiguration
import codelab.service.IPhoneService
import core.Logging
import javax.annotation.Resource
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.openqa.selenium.support.ui.FluentWait
import java.util.concurrent.TimeUnit
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebElement
import com.google.common.base.Function

class SeleniumTest extends ServiceTest {

	@Resource var iPhoneService : IPhoneService = _

	@Resource var testProps : Properties = _

	@Test
	def runHtmlUnit {
		val driver : WebDriver = new HtmlUnitDriver
		try {
			google("HtmlUnit", driver)
		} finally {
			driver.quit
		}
	}

	@Test
	def runFirefox {
		val driver : WebDriver = new FirefoxDriver
		try {
			iPhoneService.reserveIPhone(driver)
		} finally {
			driver.quit
		}
	}

	@Test
	def runChrome {
		System.setProperty("webdriver.chrome.driver", testProps.getProperty("webdriver.chrome.driver"))
		val driver : WebDriver = new ChromeDriver
		try {
			aastocks("Chrome", driver)
		} finally {
			driver.quit
		}
	}

	@Test
	def runInternetExplorer {
		System.setProperty("webdriver.ie.driver", testProps.getProperty("webdriver.ie.driver"))
		val driver : WebDriver = new InternetExplorerDriver
		try {
			iPhoneService.reserveIPhone(driver)
		} finally {
			driver.quit
		}
	}

	def google(client: String, driver: WebDriver) {
		driver.get("http://www.google.com")
		val text = driver.findElement(By.cssSelector("#gbgs4")).getText
		logger.info(s"${client} gets ${text}")
	}

	def aastocks(client: String, driver: WebDriver) {
		driver.get(testProps.getProperty("url.aastocks"))
		val quoteElem = driver.findElement(By.id("txtHKQuote"))
		quoteElem.sendKeys("2888")
		quoteElem.sendKeys(Keys.ENTER)
		val text = driver.findElement(By.cssSelector(".tb-c .data-row .pos.bold")).getText
		logger.info(s"${client} gets ${text}")
	}

	def findElement(by: By, driver: WebDriver) : WebElement = {
		val wait = new FluentWait[WebDriver](driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(classOf[NoSuchElementException], classOf[StaleElementReferenceException])
		wait.until(new Function[WebDriver,WebElement]() {
			override def apply(driver: WebDriver) : WebElement = driver.findElement(by)
		})
	}

	def loginSc(client: String, driver: WebDriver) {
		driver.get(testProps.getProperty("url.sc"))
		driver.findElement(By.cssSelector("[name='j_username']")).sendKeys(testProps.getProperty("sc.username"))
		driver.findElement(By.cssSelector("[name='j_password']")).sendKeys(testProps.getProperty("sc.password"))
		driver.findElement(By.cssSelector("[name='j_password']")).sendKeys(Keys.ENTER)
	}

	def loginChief(client: String, driver: WebDriver) {
		driver.get(testProps.getProperty("url.chief"))
		driver.findElement(By.cssSelector("[name='ClntCode']")).sendKeys(testProps.getProperty("chief.username"))
		driver.findElement(By.cssSelector("[name='Password']")).sendKeys(testProps.getProperty("chief.password"))
		driver.findElement(By.cssSelector("[name='Password']")).sendKeys(Keys.ENTER)
	}

}