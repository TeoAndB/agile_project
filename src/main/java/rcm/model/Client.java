package rcm.model;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Client extends User {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "CLIENT_ID")
    private List<Journey> journeyList;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Journey> sharedJourneyList;

    @ManyToOne
    private LogisticsCompany company;

    @SuppressWarnings("unused")
    private Client() {
        super();
    }

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
        sharedJourneyList = new LinkedList<Journey>();
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
    public List<Journey> searchJourneyByDestination(String destination) {
        String regexSearch = "(" + destination + ")";
        Pattern pattern = Pattern.compile(regexSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return journeyList.stream().filter(j -> (pattern.matcher(j.getDestinationPort())).find())
                .collect(Collectors.toList());
    }

    /**
     * Method to search journeys using Origin
     * 
     * @param origin Origin to be searched for
     * @return a list of journeys with the required origin
     */
    public List<Journey> searchJourneyByOrigin(String origin) {
        String regexSearch = "(" + origin + ")";
        Pattern pattern = Pattern.compile(regexSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return journeyList.stream().filter(j -> (pattern.matcher(j.getOriginPort())).find())
                .collect(Collectors.toList());
    }

    /**
     * Method to search journeys by container contents
     * 
     * @param content Container contents to be searched for
     * @return a list of journeys with the required container contents
     */
    public List<Journey> searchJourneyByContent(String content) {
        String regexSearch = "(" + content + ")";
        Pattern pattern = Pattern.compile(regexSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return journeyList.stream().filter(j -> (pattern.matcher(j.getContent())).find()).collect(Collectors.toList());
    }

    /**
     * Method to search journeys by id
     * 
     * @param id Journey id to be searched for
     * @return a list of journeys with the required id
     */
    public List<Journey> searchJourneyById(String id) {
        return journeyList.stream().filter(j -> Integer.toString(j.getId()).equals(id)).collect(Collectors.toList());
    }

    /**
     * Method to share a journey with another client
     * 
     * @param client  Client the journey is shared with
     * @param journey Journey to be shared with the client
     * @return true if journey is successfully shared, otherwise false
     */
    public boolean shareJourney(Client client, Journey journey) {
        if (client != null && journey != null && journeyList.contains(journey)) {
            client.addSharedJourney(journey);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to add a shared journey to the list of shared journeys
     * 
     * @param journey Journey to be added to the list of shared journeys
     */
    public void addSharedJourney(Journey journey) {
        sharedJourneyList.add(journey);
    }

    /**
     * Method to search shared journeys using destination
     * 
     * @param destination Destination to be searched for
     * @return a list of shared journeys with the required destination
     */
    public List<Journey> searchSharedJourneyByDestination(String destination) {
        String regexSearch = "(" + destination + ")";
        Pattern pattern = Pattern.compile(regexSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return sharedJourneyList.stream().filter(j -> (pattern.matcher(j.getDestinationPort())).find())
                .collect(Collectors.toList());
    }

    /**
     * Method to search shared journeys using Origin
     * 
     * @param origin Origin to be searched for
     * @return a list of shared journeys with the required origin
     */
    public List<Journey> searchSharedJourneyByOrigin(String origin) {
        String regexSearch = "(" + origin + ")";
        Pattern pattern = Pattern.compile(regexSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return sharedJourneyList.stream().filter(j -> (pattern.matcher(j.getOriginPort())).find())
                .collect(Collectors.toList());
    }

    /**
     * Method to search shared journeys by container contents
     * 
     * @param content Container contents to be searched for
     * @return a list of shared journeys with the required container contents
     */
    public List<Journey> searchSharedJourneyByContent(String content) {
        String regexSearch = "(" + content + ")";
        Pattern pattern = Pattern.compile(regexSearch, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return sharedJourneyList.stream().filter(j -> (pattern.matcher(j.getContent())).find())
                .collect(Collectors.toList());
    }

    /**
     * Method to search shared journeys by id
     * 
     * @param id Journey id to be searched for
     * @return a list of shared journeys with the required id
     */
    public List<Journey> searchSharedJourneyById(String id) {
        return sharedJourneyList.stream().filter(j -> Integer.toString(j.getId()).equals(id))
                .collect(Collectors.toList());
    }

    /**
     * Requests the history of container statuses from journey
     * 
     * @param journey Journey providing the status
     * @return List of container statuses of the given journey
     */
    public List<ContainerStatus> requestStatus(Journey journey) {
        if (journey.getClient().equals(this) || sharedJourneyList.contains(journey)) {
            return journey.getStatus();
        } else {
            return null;
        }
    }

    /**
     * Getter for list of journeys
     * 
     * @return List of Journeys belonging to the client
     */
    public List<Journey> getJourneyList() {
        return journeyList;
    }

    /**
     * Getter for List of shared Journeys belonging to the client
     * 
     * @return List of shared Journeys belonging to the client
     */
    public List<Journey> getSharedJourneyList() {
        return sharedJourneyList;
    }

    /**
     * Getter for logistics company
     * 
     * @return a logistics company
     */
    public LogisticsCompany getCompany() {
        return company;
    }
}
