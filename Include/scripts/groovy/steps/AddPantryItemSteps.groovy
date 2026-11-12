package steps

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When



class AddPantryItemSteps {

    @And("I see the item in my pantry")
    def verifyItemInPantry() {
	    WebUI.verifyTextPresent('(?is).*Milk.*', true)
	}
}