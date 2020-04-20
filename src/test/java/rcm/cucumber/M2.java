package rcm.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rcm.Journey;
import rcm.Response;

public class M2 {

    private List<Journey> filteredContent, filteredDestination, filteredOrigin;
    private Response response;
    private SharedObjectHolder holder;

    public M2(SharedObjectHolder holder) {
        this.holder = holder;
    }

    @Given("the first logistics company has two available containers")
    public void the_first_logistics_company_has_two_available_containers() {
        holder.getFirstCompany().createContainer();
        holder.getFirstCompany().createContainer();
    }

    @Given("the container has a location {string}")
    public void the_container_has_a_location(String location) {
        holder.getFirstContainer().setLocation(location);
    }

    @When("the first client requests to register a journey with the first logistics company with origin {string}, destination {string} and content {string}")
    public void the_first_client_requests_to_register_a_journey_with_the_first_logistics_company_with_origin_destination_and_content(
            String originPort, String destinationPort, String content) {
        response = holder.getFirstClient().requestJourney(originPort, destinationPort, content,
                LocalDateTime.of(2020, 3, 13, 4, 20));
    }

    @When("the first logistics company updates containers location to a new location {string}")
    public void the_first_logistics_company_updates_containers_location_to_a_new_location(String newLocation) {
        response = holder.getFirstCompany().updateLocation(holder.getFirstContainer(), newLocation);
    }

    @When("the second logistics company updates containers location to a new location {string}")
    public void the_second_logistics_company_updates_containers_location_to_a_new_location(String newLocation) {
        response = holder.getSecondCompany().updateLocation(holder.getFirstContainer(), newLocation);
    }

    @When("the first client filters his journeys based on the origin port {string}")
    public void the_first_client_filters_his_journeys_based_on_the_origin_port(String origin) {
        filteredOrigin = holder.getFirstClient().searchByOrigin(origin);
    }

    @When("the first client filters his journeys based on the destination {string}")
    public void the_first_client_filters_his_journeys_based_on_the_destination(String destination) {
        filteredDestination = holder.getFirstClient().searchByDestination(destination);
    }

    @When("the first client filters his journeys based on the content {string}")
    public void the_first_client_filters_his_journeys_based_on_the_content(String content) {
        filteredContent = holder.getFirstClient().searchByContent(content);
    }

    @Then("the location is changed")
    public void the_location_is_changed() {
        assertEquals(Response.SUCCESS, response);
        assertEquals("Atlantic Ocean", holder.getFirstContainer().getLocation());
    }

    @Then("the location is not changed")
    public void the_location_is_not_changed() {

        assertEquals(Response.LOCATION_NOT_CHANGED, response);
        assertEquals("Los Angeles", holder.getFirstContainer().getLocation());
    }

    @Then("an id is created")
    public void an_id_is_created() {
        assertEquals(Response.SUCCESS, response);
        assertTrue(!holder.getFirstClient().getJourneyList().isEmpty());

    }

    @Then("the first journey is listed")
    public void the_first_journey_is_listed() {
        assertTrue(filteredDestination.contains(holder.getFirstJourney()));
        assertTrue(filteredDestination.size() == 1);
    }

    @Then("the second journey is listed")
    public void the_second_journey_is_listed() {
        assertTrue(filteredOrigin.contains(holder.getSecondJourney()));
        assertTrue(filteredOrigin.size() == 1);
    }

    @Then("both journeys are listed")
    public void both_journeys_are_listed() {
        assertTrue(filteredContent.size() == 2);
        assertTrue(filteredContent.contains(holder.getFirstJourney()));
        assertTrue(filteredContent.contains(holder.getSecondJourney()));
    }

    @Then("the journey doesnt start")
    public void the_journey_doesnt_start() {
        assertEquals(Response.JOURNEY_NOT_STARTED, response);
    }

}