package rcm.model;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Client extends User {

    private List<Journey> journeyList;
    private LogisticsCompany company;

    /**
     * Client constructor
     * 
     * @param name      Name of the client
     * @param address   Address of the client
     * @param refPerson Reference person of the client
     * @param email     Email of the client
     * @param password  Password of the client
     * @throws WrongInputException
     */
    public Client(String name, String address, String refPerson, String email, String password)
            throws WrongInputException {
        super(name, address, refPerson, email, password);
        journeyList = new LinkedList<Journey>();
        id = IdGenerator.getInstance().getId(GroupIdType.CLIENT);
    }

    /**
     * Method to assign a logistics company to a client
     * 
     * @param company The logistics company to be assigned
     */
    public void assignCompany(LogisticsCompany company) {
        this.company = company;
    }

    /**
     * Method to assign a journey to a client
     * 
     * @param journey The journey to be assigned
     */
    public void addJourney(Journey journey) {
        journeyList.add(journey);
    }

    /**
     * Method to search journeys using destination
     * 
     * @param destination Destination to be searched for
     * @return a list of journeys with the required destination
     */
    public List<Journey> searchByDestination(String destination) {
        return journeyList.stream().filter(j -> j.getDestinationPort().equals(destination))
                .collect(Collectors.toList());
    }

    /**
     * Method to search journeys using Origin
     * 
     * @param origin Origin to be searched for
     * @return a list of journeys with the required origin
     */
    public List<Journey> searchByOrigin(String origin) {
        return journeyList.stream().filter(j -> j.getOriginPort().equals(origin)).collect(Collectors.toList());
    }

    /**
     * Method to search journeys by container contents
     * 
     * @param content Container contents to be searched for
     * @return a list of journeys with the required container contents
     */
    public List<Journey> searchByContent(String content) {
        return journeyList.stream().filter(j -> j.getContent().equals(content)).collect(Collectors.toList());
    }

    /**
     * Method to view the data of a client
     * 
     * @param loggedIn boolean representing the log-in status of the first client
     * @param email1   Email of the first client
     * @param email2   Email of the second client
     * @param access   boolean representing whether the second client has consented
     *                 to be viewed
     * @return a list of journeys of the second client
     */
    public List<Journey> viewClientData(boolean loggedIn, String email1, String email2, boolean access) {
        if ((email1.equals(email2) || access) && loggedIn) {
            LinkedList<Client> cl = new LinkedList<Client>();
            cl.addAll(company.searchByEmail(email2));
            if (cl.isEmpty()) {
                return null;
            } else {
                return cl.pop().getJourneyList();
            }
        } else {
            return null;
        }
    }

    public boolean closeButton() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Requests the history of container statuses from journey
     * 
     * @param journey Journey providing the status
     * @return List of container statuses of the given journey
     */
    public List<ContainerStatus> requestStatus(Journey journey) {
        if (journey.getClient().equals(this)) {
            return journey.getStatus();
        } else {
            return null;
        }
    }

    /**
     * Requests the company to create a journey
     * 
     * @param originPort      the origin port of the journey
     * @param destinationPort the destination port of the journey
     * @param content         content of the container in the journey
     * @param timestamp       time stamp of the journey start
     * @return Response.SUCCESS for journey created and added to journeyList
     *         JOURNEY_NOT_STARTED for failing to start journey. The journey is
     *         added to the journeyList. JOURNEY_NOT_CREATED for failing to create
     *         journey
     * @implNote This method only works if the client is assigned to a company
     */
    public Response requestJourney(String originPort, String destinationPort, String content, LocalDateTime timestamp) {
        Journey journey = company.createJourney(this, originPort, destinationPort, content);
        if (journey != null) {
            if (company.startJourney(journey, timestamp)) {
                return Response.SUCCESS;
            } else {
                return Response.JOURNEY_NOT_STARTED;
            }
        } else {
            return Response.JOURNEY_NOT_CREATED;
        }
    }

    /**
     * Getter for list of journeys
     * 
     * @return List of Journeys belonging to the clientS
     */
    public List<Journey> getJourneyList() {
        return journeyList;
    }
}

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.sql.SQLException;
@Entity
    @OneToMany(cascade = CascadeType.ALL)
    @ManyToOne
    private Client() {
        super();
    }