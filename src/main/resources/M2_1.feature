#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Container Registration

  @tag1
  Scenario: Successful registration
  	Given a first logistics company "Maersk" with address "Esplanaden 50, 1098 Koebenhavn K", reference person "Soeren Skou" and email "info@maersk.com"
    And a container of the first logistics company with ID 1
    And a first client "Novo Nordisk" with address "Novo Alle, 2880 Bagsvaerd", reference person "Lars Fruergaard Joergensen" and email "info@novonordisk.com"
    And a journey of given container and first client with origin port of "Shenzhen", destination port of "Rotterdam" and a content of "medical goods"   
    When a client requests to register the container
    And an Id is created

  #Scenario: Container not
   # Given a container already on a journey
    #And a client "Novo Nordisk" who owns the container journey
    #And the container journey has start location "Copenhagen" and destination "New York"
    #When a client requests to register the container
    #Then the two locations are not assigned to the containers journey
    #And an Id is not created
