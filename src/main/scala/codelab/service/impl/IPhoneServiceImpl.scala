package codelab.service.impl

import java.awt.BorderLayout
import java.util.Date
import java.util.Properties
import java.util.concurrent.TimeUnit

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.Select
import org.springframework.stereotype.Service

import com.fasterxml.jackson.databind.ObjectMapper
import codelab.service.IPhoneService

import codelab.iphone.IPhoneAvailability
import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

@Service
class IPhoneServiceImpl extends IPhoneService {

	@Resource(name="testProps") var testProps : Properties = _

	@PostConstruct
	def init {
	//	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
	}

	override def reserveIPhone(driver: WebDriver) {
		try {
			driver.get(testProps.getProperty("url.iPhoneAvailability"))
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS)

			/*
			val selProducts = new Select(driver.findElement(By.name("products")))
			val selStores = new Select(driver.findElement(By.name("stores")))
			val availabilities = getAllIPhoneAvailabilities(selProducts, selStores, driver)

			*/

		    processLogin(driver)
		    processAuthentication(driver)
		    processSmsPage(driver)
		    processReservation(driver)

		} finally {
			// driver.quit
		}
	}

	override def getJsonUrl : String = testProps.getProperty("url.iPhoneAvailabilityJson")

	override def getJsonString(jsonUrl: String) : String = {
		jsonUrl.split(";").foreach(urlStr => {
			try {
				return Source.fromURL(urlStr).mkString
			} catch {
				case e : Exception =>
			}
		})
		"{}"
	}

	override def isServiceOpen(jsonContent: String) : Boolean = {
		val objectMapper = new ObjectMapper
		val jsonTree = objectMapper.readTree(jsonContent)
		jsonTree.fields.hasNext
	}

	override def isAvailable(jsonString : String) : Boolean = !getOptions(jsonString).filter(_.availability).isEmpty

	override def lastUpdated(jsonString: String) : Date = {
		val objectMapper = new ObjectMapper
		val jsonTree = objectMapper.readTree(jsonString)
		new Date(jsonTree.get("updated").asLong)
	}

	def getOptions(jsonString: String) : Seq[IPhoneOption] = {
		val objectMapper = new ObjectMapper
		val jsonTree = objectMapper.readTree(jsonString)
		val options = new ArrayBuffer[IPhoneOption]
		jsonTree.fields.foreach(store => {
			val storeName = store.getKey
			store.getValue.fields.foreach(model => {
				val modelName = model.getKey
				val available = model.getValue.asBoolean
				options += new IPhoneOption(storeName, modelName, available)
			})
		})
		options
	}

	def processLogin(driver: WebDriver) {
		println(s"Processing login ... ${driver.getCurrentUrl}")
		driver.findElement(By.linkText("Start")).click
	}

	def getAccountProperty(propName: String) : String = {
		val acctIndex = testProps.getProperty("reserve.accountIndex")
		testProps.getProperty(s"apple.accounts[${acctIndex}].${propName}")
	}

	def processAuthentication(driver: WebDriver) {
		println(s"Processing authentication ... ${driver.getCurrentUrl}")
		var ok = false
		while (!ok) {
			driver.findElement(By.id("accountname")).clear
		    driver.findElement(By.id("accountname")).sendKeys(getAccountProperty("accountName"))
		    driver.findElement(By.id("accountpassword")).clear
		    driver.findElement(By.id("accountpassword")).sendKeys(getAccountProperty("accountPassword"))
		    val optionTexts = Seq("OK", "Regenerate", "Close")
		    var text : String = null

		    while (text == null) {
			    text = JOptionPane.showInputDialog("Enter text in image:")
			    if (text == null)
			    	driver.findElement(By.cssSelector("#captchaImageRightDiv a.new")).click
		    }
			println(s"Image text: ${text}")

			driver.findElement(By.id("captchaInput")).clear
		    driver.findElement(By.id("captchaInput")).sendKeys(text)
		    driver.findElement(By.id("signInHyperLink")).click
		    Thread.sleep(2000)
		    if (!driver.getCurrentUrl.startsWith(testProps.getProperty("url.iPhoneAuth")))
		    	ok = true
		}
	}

	def showInputPanel {
		val inputPanel = new JPanel(new BorderLayout)
		inputPanel.add(new JLabel("Image in text:"))
		inputPanel.add(new JTextField)

	}

	def processSmsPage(driver: WebDriver) {
		println(s"Processing SMS page ... ${driver.getCurrentUrl}")
	    driver.findElement(By.id("phoneNumber")).clear
	    driver.findElement(By.id("phoneNumber")).sendKeys(getAccountProperty("phone"))
	    driver.findElement(By.id("reservationCode")).clear
	    var reservationCode = testProps.getProperty("reserve.reservationCode")
	    if (reservationCode == null || reservationCode.isEmpty)
	    	reservationCode = JOptionPane.showInputDialog("Enter Reservation Code:")
	    driver.findElement(By.id("reservationCode")).sendKeys(reservationCode)
	    driver.findElement(By.linkText("Continue")).click
	}

	def processReservation(driver: WebDriver) {
		val jsDriver = driver.asInstanceOf[JavascriptExecutor]
		println(s"Processing reservation ... ${driver.getCurrentUrl}")
		val selStoreName = new Select(driver.findElement(By.name("selectedStoreNumber")))
		Thread.sleep(1000)
		val optionEls = selStoreName.getOptions
		val options = selStoreName.getOptions.splitAt(1)._2.map(_.getText)

		val optionIndexes = Seq(2, 3, 1)

		var ok = false
		var selected = false

		optionIndexes.toStream.takeWhile(_ => !ok).foreach { index =>
			selStoreName.selectByIndex(index)

			// select product
			if (!selected) {
				Thread.sleep(400)
				selected = true
			}
			driver.findElements(By.cssSelector("li#product div.box")).toStream.takeWhile(_ => !ok).foreach { productEl =>
				productEl.click

				// select plan
				Thread.sleep(200)
				driver.findElements(By.cssSelector("li#locked div.box"))(0).click

				// select color
				Thread.sleep(200)
				val colorsToSelect = Seq("Silver", "Gold")
				driver.findElements(By.cssSelector("li#color div.finish-option")).filter(el => colorsToSelect.contains(el.getText)).toStream.takeWhile(_ => !ok).foreach { colorEl =>
					colorEl.click
					Thread.sleep(1200)
					val errorDiv = driver.findElement(By.cssSelector("li#model .error-message > #noModelsAvailable > div"))
					if (!errorDiv.isDisplayed) {
						ok = true

					}

					val product = productEl.getText
					val color = colorEl.getText
					println(s"${product} - ${color}")

					/*
					val lis = driver.findElements(By.cssSelector("li#model li")).filter(_.isDisplayed)
					lis.foreach { li =>
						println( li.findElement(By.cssSelector("input")).getAttribute("value") )

					}*/
					/*
					var scp = <script><![CDATA[[
						return $('li#model li:visible input').map(function(i, elem) {
							return elem.getAttribute("value");
						});
					]]></script>
					println( scp.text )
					val o = jsDriver.executeScript(scp.text)
					println( o )*/
				}
			}

		}

		if (ok) {
			jsDriver.executeScript("jQuery('li#quantity, li#contact, li#time').removeClass('disabled');")

			// selected quantity
			new Select(driver.findElement(By.name("selectedQuantity"))).selectByVisibleText(testProps.getProperty("reserve.quantity"))
			// enter first name, last name and email
			/*driver.findElement(By.name("firstName")).clear
			driver.findElement(By.name("firstName")).sendKeys(getAccountProperty("firstName"))
			driver.findElement(By.name("lastName")).clear
			driver.findElement(By.name("lastName")).sendKeys(getAccountProperty("lastName"))
			driver.findElement(By.name("email")).clear
			driver.findElement(By.name("email")).sendKeys(getAccountProperty("email"))*/
			// select ID type
			new Select(driver.findElement(By.name("selectedGovtIdType"))).selectByValue(getAccountProperty("govtIdType"))
			// enter ID
			driver.findElement(By.name("govtId")).clear
			driver.findElement(By.name("govtId")).sendKeys(getAccountProperty("govtId"))

			// select timeslot
			Thread.sleep(1000)
			val selTimeSlotId = new Select(driver.findElement(By.name("selectedTimeSlotId")))
			if (selTimeSlotId.getOptions.size > 1)
				selTimeSlotId.selectByIndex(1)
		}
	}

	def getAllIPhoneAvailabilities(selProducts: Select, selStores: Select, driver: WebDriver) : Seq[IPhoneAvailability] = {
		val availabilities = new ArrayBuffer[IPhoneAvailability]
		val products = selProducts.getOptions.splitAt(1)._2.map(_.getText)
		val stores = selStores.getOptions.splitAt(1)._2.map(_.getText)
		products.foreach { product =>
			stores.foreach { store =>
				selProducts.selectByVisibleText(product)
				selStores.selectByVisibleText(store)
				Thread.sleep(300)
				availabilities ++= getIPhoneAvailabilities(product, store, driver)
			}
		}
		availabilities
	}

	def getIPhoneAvailabilities(productName: String, storeName: String, driver: WebDriver) : Seq[IPhoneAvailability] = {
		val products = new ArrayBuffer[IPhoneAvailability]
		val table = driver.findElement(By.className("device-sizes"))
		val colorNames = Seq("Silver", "Gold", "Black")
		val sizeNames = table.findElements(By.cssSelector("thead th")).map(_.getText)
		val rows = table.findElements(By.cssSelector("tbody > tr"))
		rows.zipWithIndex.foreach {
			case (row,i) =>
				val tds = row.findElements(By.cssSelector("td"))
				tds.zipWithIndex.foreach {
					case (td,j) => products += new IPhoneAvailability(productName, storeName, colorNames.get(i), sizeNames.get(j), td.getText)
				}
		}
		products
	}

	case class IPhoneOption(storeName: String, modelName: String, availability: Boolean) {

		override def toString : String = s"${storeName} - ${modelName}"

	}

}