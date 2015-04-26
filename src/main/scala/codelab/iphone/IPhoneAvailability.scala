package codelab.iphone

case class IPhoneAvailability(productName: String, storeName: String, color: String, size: String, availability: String) {
	override def toString = s"${storeName} - [${productName}] ${color} ${size} -> ${availability}"
}