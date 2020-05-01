package rcm.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import rcm.repository.Repository;

public class Application {

    private List<LogisticsCompany> system;
    private Repository repo;

    private PropertyChangeSupport support;

    private LogisticsCompany loggedInCompany;
    private Client loggedInClient;

    /**
     * Application constructor
     * 
     * @param repo Repository connected to the database
     * @throws IOException
     */
    public Application(Repository repo) throws IOException {
        this.repo = repo;
        support = new PropertyChangeSupport(this);
        system = repo.readAllLogisticsCompanies();
    }

    /**
     * Adds an observer to the Application
     * 
     * @param l PropertyChangeListener to be added to the support object
     */
    public void addObserver(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    /**
     * Creates a logistics company and adds it to the system and database
     * 
     * @param name      Name of the logistics company
     * @param address   Address of the logistics company
     * @param refPerson Reference person of the logistics company
     * @param email     Email of the logistics company
     * @param password  Password for the account of the logistics company
     * @return created Logistics Company
     * @throws WrongInputException
     * @throws IOException
     */
    public LogisticsCompany createNewLogisticsCompany(String name, String address, String refPerson, String email,
            String password) throws WrongInputException, IOException {
        LogisticsCompany c = new LogisticsCompany(name, address, refPerson, email, password);
        repo.createLogisticsCompany(c);
        system.add(c);
        return c;
    }

    /**
     * Creates a client and adds it to the system and database
     * 
     * @param name      Name of the client
     * @param address   Address of the client
     * @param refPerson Reference person of the client
     * @param email     Email of the client
     * @param password  Password for the account of the client
     * @return created Client
     * @throws IOException
     * @throws WrongInputException
     */
    public Client createNewClient(String name, String address, String refPerson, String email, String password)
            throws IOException, WrongInputException {
        if (loggedInCompany != null) {
            List<String> errors = validateUser(name, address, refPerson, email, password);
            if (errors.isEmpty()) {
                Client c = new Client(name, address, refPerson, email, password);
                loggedInCompany.addClient(c);
                c.assignCompany(loggedInCompany);
                repo.createClient(c);
                return c;
            } else {
                throw new WrongInputException("Please correct the following input: " + String.join(" ", errors));
            }
        } else {
            return null;
        }
    }

    private List<String> validateUser(String name, String address, String refPerson, String email, String password) {
        List<String> errors = new LinkedList<>();
        if (!User.validateName(name)) {
            errors.add("name");
        }

        if (!User.validateAddress(address)) {
            errors.add("address");
        }

        if (!User.validateRefPerson(refPerson)) {
            errors.add("reference person");
        }

        if (!User.validateEmail(email) || getAllEmails().contains(email)) {
            errors.add("email");
        }

        if (!User.validatePassword(password)) {
            errors.add("password");
        }

        return errors;
    }

    private List<String> getAllEmails() {
        List<String> emails = system.stream().map(c -> c.getEmail()).collect(Collectors.toList());
        emails.addAll(getAllClients().stream().map(c -> c.getEmail()).collect(Collectors.toList()));
        return emails;
    }

    /**
     * Creates a container and adds it to the system and database
     * 
     * @return created Container
     * 
     * @throws IOException
     */
    public Container createNewContainer() throws IOException {
        if (loggedInCompany != null) {
            Container c = new Container(loggedInCompany);
            repo.createContainer(c);
            return c;
        } else {
            return null;
        }
    }

    /**
     * Requests a new journey adds it to the system and database
     * 
     * @param originPort      Origin port of the journey
     * @param destinationPort Destination port of the journey
     * @param content         Content of the journey
     * @param timestamp       Time stamp when the journey should start
     * @return requested Journey
     * @throws IOException
     */
    public Journey requestNewJourney(String originPort, String destinationPort, String content)
            throws IOException {
        if (loggedInClient != null) {
            Journey journey = new Journey(originPort, destinationPort, content, loggedInClient);
            loggedInClient.addJourney(journey);
            repo.createJourney(journey);
            return journey;
        } else {
            return null;
        }
    }

    /**
     * Enters a new container status adds it to the system and database
     * 
     * @param journey_id Journey id the status should be added to
     * @param timestamp  Time stamp of the status
     * @param temp       Temperature inside the container
     * @param humid      Humidity inside the container
     * @param atmPress   Atmospheric pressure inside the container
     * @param loc        Location of the container
     * @return Boolean of whether it was successfully added or not
     * @throws IOException
     */
    public boolean enterNewContainerStatus(int journey_id, LocalDateTime timestamp, double temp, double humid,
            double atmPress, String loc) throws IOException {
        ContainerStatus status = new ContainerStatus(timestamp, temp, humid, atmPress, loc);
        return enterNewContainerStatus(journey_id, status);
    }

    /**
     * Enters a new container status adds it to the system and database
     * 
     * @param journey_id Journey id the status should be added to
     * @param status     Container status of the container
     * @return Boolean of whether it was successfully added or not
     * @throws IOException
     */
    public boolean enterNewContainerStatus(int journey_id, ContainerStatus status) throws IOException {
        if (loggedInCompany != null && loggedInCompany.enterStatus(getJourneyById(journey_id), status)) {
            repo.updateCompany(loggedInCompany);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Starts a journey with the given time stamp
     * 
     * @param journey_id Journey id to be started
     * @param timestamp  Starting time stamp of the journey
     * @return Boolean whether the journey was successfully started
     */
    public boolean startJourney(int journey_id, LocalDateTime timestamp) {
        if (loggedInCompany != null) {
            return loggedInCompany.startJourney(getJourneyById(journey_id), timestamp);
        } else {
            return false;
        }
    }

    /**
     * Ends a journey with the given time stamp
     * 
     * @param journey_id Journey id to be ended
     * @param timestamp  Ending time stamp of the journey
     * @return Boolean whether the journey was successfully ended
     */
    public boolean endJourney(int journey_id, LocalDateTime timestamp) {
        if (loggedInCompany != null) {
            return loggedInCompany.endJourney(getJourneyById(journey_id), timestamp);
        } else {
            return false;
        }
    }

    /**
     * Logs in a user by email and password
     * 
     * @param email    Email of the user to be logged in
     * @param password Password of the user to be logged in
     * @throws WrongInputException
     */
    public void logInUser(String email, String password) throws WrongInputException {
        loggedInCompany = null;
        loggedInClient = null;

        for (LogisticsCompany c : system) {
            if (c.getEmail().equals(email)) {
                if (User.SHA1_Hasher(password).equals(c.getPassword())) {
                    loggedInCompany = c;
                    support.firePropertyChange("companyLoggedIn", null, null);
                    break;
                } else {
                    throw new WrongInputException("Your password is incorrect");
                }
            } else {
                Set<Client> cs = c.searchClientByEmail(email);
                if (!cs.isEmpty()) {
                    Client cl = (new LinkedList<>(cs)).pop();
                    if (User.SHA1_Hasher(password).equals(cl.getPassword())) {
                        loggedInClient = cl;
                        support.firePropertyChange("clientLoggedIn", null, null);
                        break;
                    } else {
                        throw new WrongInputException("Your password is incorrect");
                    }
                }
            }
        }
        if (loggedInClient == null && loggedInCompany == null) {
            throw new WrongInputException("Email not found");
        }
    }

    /**
     * Logs out the user
     */
    public void logOut() {
        loggedInClient = null;
        loggedInCompany = null;
        support.firePropertyChange("userLoggedOut", null, null);
    }

    /**
     * Searches for clients of logged in logistics company by all parameters
     * 
     * @param query Query to be searched for
     * @return Set of clients
     */
    public Set<Client> searchForClients(String query) {
        Set<Client> results = loggedInCompany.searchClientByName(query);
        results.addAll(loggedInCompany.searchClientByAddress(query));
        results.addAll(loggedInCompany.searchClientByRefPerson(query));
        results.addAll(loggedInCompany.searchClientByEmail(query));
        results.addAll(loggedInCompany.searchClientById(query));
        return results;
    }

    /**
     * Searches for journeys of logged in client by all parameters
     * 
     * @param query Query to be searched for
     * @return Set of journeys
     */
    public Set<Journey> searchForJourneys(String query) {
        // TODO: Adrienne change to set first here and below
        List<Journey> resultList = loggedInClient.journeySearchByOrigin(query);
        resultList.addAll(loggedInClient.journeySearchByDestination(query));
        resultList.addAll(loggedInClient.journeySearchByContent(query));
        resultList.addAll(loggedInClient.journeySearchById(query));
        Set<Journey> results = new HashSet<Journey>(resultList);
        return results;
    }

    /**
     * Searches for shared journeys of logged in client by all parameters
     * 
     * @param query Query to be searched for
     * @return Set of shared journeys
     */
    public Set<Journey> searchForSharedJourneys(String query) {
        List<Journey> resultList = loggedInClient.sharedJourneySearchByOrigin(query);
        resultList.addAll(loggedInClient.sharedJourneySearchByDestination(query));
        resultList.addAll(loggedInClient.sharedJourneySearchByContent(query));
        resultList.addAll(loggedInClient.sharedJourneySearchById(query));
        Set<Journey> results = new HashSet<Journey>(resultList);
        return results;
    }

    /**
     * Getter for logged in logistics company
     * 
     * @return null if no logistics company is logged in, otherwise returns the
     *         object of the company
     */
    public LogisticsCompany getLoggedInCompany() {
        return loggedInCompany;
    }

    /**
     * Getter for logged in client
     * 
     * @return null if no client is logged in, otherwise returns the object of the
     *         client
     */
    public Client getLoggedInClient() {
        return loggedInClient;
    }

    /**
     * Requests status for given journey
     * 
     * @param journey_id Journey id to get status history out of
     * @return List of the Container Statuses
     */
    public List<ContainerStatus> requestStatus(int journey_id) {
        if (loggedInClient != null) {
            return loggedInClient.requestStatus(getJourneyById(journey_id));
        } else {
            return null;
        }
    }

    /**
     * Requests clients of the logged in company
     * 
     * @return List of the Clients
     */
    public List<Client> requestClients() {
        if (loggedInCompany != null) {
            return new LinkedList<>(loggedInCompany.getClients());
        } else {
            return null;
        }
    }

    /**
     * Requests containers of the logged in company
     * 
     * @return List of the Containers
     */
    public List<Container> requestContainers() {
        if (loggedInCompany != null) {
            return new LinkedList<>(loggedInCompany.getContainers());
        } else {
            return null;
        }
    }

    /**
     * Requests journeys of the logged in client
     * 
     * @return List of the Journeys
     */
    public List<Journey> requestJourneys() {
        if (loggedInClient != null) {
            return new LinkedList<>(loggedInClient.getJourneyList());
        } else {
            return null;
        }
    }

    /**
     * Requests shared journeys of the logged in client
     * 
     * @return List of the shared Journeys
     */
    public List<Journey> requestSharedJourneys() {
        if (loggedInClient != null) {
            return new LinkedList<>(loggedInClient.getSharedJourneyList());
        } else {
            return null;
        }
    }

    /**
     * Shares a journey with another client
     * 
     * @param client_id  Client id to be shared with
     * @param journey_id Journey id to be shared
     * @return Boolean whether it was successfully shared
     */
    public boolean shareJourney(int client_id, int journey_id) {
        if (loggedInClient != null) {
            return loggedInClient.shareJourney(getClientById(client_id), getJourneyById(journey_id));
        } else {
            return false;
        }

    }

    private List<Client> getAllClients() {
        List<Client> clients = new LinkedList<>();
        for (LogisticsCompany c : system) {
            clients.addAll(c.getClients());
        }
        return clients;
    }

    private List<Journey> getAllJourneys() {
        List<Journey> journeys = new LinkedList<>();
        for (Client c : getAllClients()) {
            journeys.addAll(c.getJourneyList());
        }
        return journeys;
    }

    private Client getClientById(int id) {
        for (Client c : getAllClients()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private Journey getJourneyById(int id) {
        for (Journey j : getAllJourneys()) {
            if (j.getId() == id) {
                return j;
            }
        }
        return null;
    }

    public void companyTabChanged() {
        support.firePropertyChange("companyTabChanged", null, null);
    }

    public void clientTabChanged() {
        support.firePropertyChange("clientTabChanged", null, null);
    }

}
