Feature: Below is the list of scenarios to test CRUD Operation.

  Scenario: Ability to return an item
    Given the API base URI is set
    When I send a GET request to "/objects/7"
    Then the response status code should be 200
    And the response should contain name as "Apple MacBook Pro 16"

  Scenario: Ability to list multiple items
    Given the API base URI is set
    When I send a GET request to "/objects"
    Then the response status code should be 200
    And Google Pixel 6 Pro should have colour "Cloudy White"

  Scenario: Verify the objects can be created and it can be deleted as well
    Given the API base URI is set
    Given the product payload is:
      |  name                 | year | price   | CPU model     | Hard disk size |
      |  Apple MacBook Pro 17 | 2019 | 1849.99 | Intel Core i9 | 1 TB           |
    When I send a POST request to "/objects"
    Then the response status code should be 200
    And the response should contain name as "Apple MacBook Pro 17"
    When I send a DELETE request for the created object
    Then the response status code should be 200

  Scenario: Negative : Validate Apple iPhone 17 exists in product catalogue
    Given the API base URI is set
    When I send a GET request to "/objects"
    Then the product "Apple iPhone 17" should exist in the response



