package codelab.service

import org.openqa.selenium.WebDriver
import java.util.Date

trait IPhoneService {
	
	def getJsonUrl : String
	
	def getJsonString(jsonUrl: String) : String
	
	def isServiceOpen(jsonString : String) : Boolean
	
	def isAvailable(jsonString: String) : Boolean
	
	def lastUpdated(jsonString: String) : Date
	
	def reserveIPhone(driver: WebDriver)
}