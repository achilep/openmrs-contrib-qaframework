package org.openmrs.contrib.qaframework.automation;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openmrs.reference.page.HomePage;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FindPatientSteps extends Steps {

    @After("@selenium")
    public void destroy() {
        quit();
    }

    @When("Search user rightly logs in")
    public void patientSearchLogin() throws Exception {
        goToLoginPage();
        loginPage.login(testProperties.getUsername(),
                testProperties.getPassword(), "Registration Desk");
    }

    @And("User clicks on Find Patient App")
    public void visitFindPatientPage() throws InterruptedException {
        homePage = new HomePage(loginPage);
        findPatientPage = homePage.goToFindPatientRecord();
    }

    @And("User enters missing patient")
    public void enterMissingPatient() throws InterruptedException {
        findPatientPage.enterPatient("MissingPatient");
    }

    @Then("Search Page returns no patients")
    public void noPatients() throws InterruptedException {
        assertNotNull(getElement(By.className("dataTables_empty")));
    }

    @And("User enters John")
    public void enterJohn() throws InterruptedException {
        findPatientPage.enterPatient("John");
    }

    @Then("Search Page returns patients")
    public void returnResults() throws InterruptedException {
        firstPatientIdentifier = findPatientPage.getFirstPatientIdentifier();
        assertNotNull(firstPatientIdentifier);
    }

    @And("User clicks on first patient")
    public void clickFirstPatient() throws InterruptedException {
        dashboardPage = findPatientPage.clickOnFirstPatient();
    }

    @Then("System loads patient dashboard")
    public void loadPatientDashboard() throws InterruptedException {
        String id = getElement(patientHeaderId).getText();
        trimPatientId(id);
        assertEquals(firstPatientIdentifier, id);
    }

    private void trimPatientId(String id) {
        if (firstPatientIdentifier.indexOf("[") > 0 && id.indexOf("[") > 0) {
            firstPatientIdentifier = firstPatientIdentifier.split("\\[")[0];
            id = id.split("\\[")[0];
        }
        if (firstPatientIdentifier.indexOf(" ") > 0) {
            firstPatientIdentifier = firstPatientIdentifier.split(" ")[0];
        }
    }
}
