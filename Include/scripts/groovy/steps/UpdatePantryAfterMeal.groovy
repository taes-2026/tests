
package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.And

import java.time.LocalDate

class PantryAfterMeal {

	@Given("the pantry has the product {string} with quantity {string} and unit {string}")
	def addProduct(String name, String quantity, String unit) {

		WebUI.click(findTestObject('Page_SmartPantry/button_Adicionar produto'))
		WebUI.click(findTestObject('Page_SmartPantry/button_Introduzir manualmente'))

		WebUI.setText(findTestObject('Page_SmartPantry/input_Nome do produto'), name)

		WebUI.selectOptionByValue(
			findTestObject('Page_SmartPantry/select_Categoria FrutasLegumesCarnesPeixeLaticn'),
			'fruits', false
		)

		WebUI.setText(findTestObject('Page_SmartPantry/input_Quantidade'), quantity)

		WebUI.selectOptionByLabel(
			findTestObject('Page_SmartPantry/select_Unidade Unidade(s)kggLmlEmbalagem'),
			unit, false
		)

		WebUI.setText(
			findTestObject('Page_SmartPantry/input_Data de validade'),
			LocalDate.now().plusDays(5).toString()
		)

		WebUI.click(findTestObject('Page_SmartPantry/button_Guardar'))
	}

	@Then("the pantry shows {string} with {string}")
	def pantryShowsWith(String name, String expected) {

		TestObject to = new TestObject()

		to.addProperty('xpath', ConditionType.EQUALS,
			"//table//tr[td[1][normalize-space()='${name}']]/td[3]")

		String text = WebUI.getText(to)

		WebUI.verifyEqual(text.contains(expected), true)
	}

	@Then("the pantry shows {string} with quantity less than {string}")
	def pantryShowsQuantityLessThan(String name, String limit) {

		TestObject to = new TestObject()

		to.addProperty('xpath', ConditionType.EQUALS,
			"//table//tr[td[1][normalize-space()='${name}']]/td[3]")

		String text = WebUI.getText(to)

		String quantity = text.replaceAll('[^0-9,.]', '').replace(',', '.')

		WebUI.verifyEqual(
			new BigDecimal(quantity) < new BigDecimal(limit),
			true
		)
	}

	@Then("the pantry does not show {string}")
	def pantryDoesNotShow(String name) {

		TestObject to = new TestObject()

		to.addProperty('xpath', ConditionType.EQUALS,
			"//table//tr[td[1][normalize-space()='${name}']]/td[1]")

		WebUI.verifyElementNotPresent(to, 2)
	}

	@Then("I see the pantry warning")
	def seeWarning() {

		WebUI.verifyTextPresent('Estes ingredientes não foram descontados', false)
	}
}
