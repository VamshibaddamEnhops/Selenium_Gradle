 @tag
Feature: Create Account and Register to OpenCart site
	@C9
  Scenario: Validate the Registration process
    Given user generate temperary email
    And user navigate to store site
    When user enter the Registration details
    And user click on continue
    Then Validate Account created with Generated Email
    When user navigates the Opencart site
    And User enter userEmail and Password
    And user click on Login
    Then validate the "My Account" text
    And log out from the application and verify successful log out

	
	@C10
  Scenario: Validate existence of OpenCart Logo on top left corner
    Given user navigate to store site
    Then user validates logo of My cart Application
