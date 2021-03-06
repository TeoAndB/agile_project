package rcm.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<String> errors = validateUser(name, address, refPerson, email, password);
        if (errors.isEmpty()) {
            LogisticsCompany c = new LogisticsCompany(name, address, refPerson, email, password);
            repo.createLogisticsCompany(c);
            system.add(c);
            return c;
        } else {
            throw new WrongInputException("Please correct the following input: " + String.join(" ", errors));
        }
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
                support.firePropertyChange("newClientCreated", null, null);
                return c;
            } else {
                throw new WrongInputException("Please correct the following input: " + String.join(" ", errors));
            }
        } else {
            return null;
        }
    }

    /**
     * Validates the personal information of a user
     * 
     * @param name      Name of the user
     * @param address   Address of the user
     * @param refPerson Reference person of the user
     * @param email     Email of the user
     * @param password  Password of the user
     * @return list of errors containing the name of the fields which contain
     *         incorrect information
     */
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

    /**
     * Method to update user name
     * 
     * @param newName New name of the user
     * @throws WrongInputException
     */
    public void updateName(String newName) throws WrongInputException {
        if (User.validateName(newName)) {
            if (loggedInCompany != null) {
                loggedInCompany.setName(newName);
                repo.updateCompany(loggedInCompany);
            } else if (loggedInClient != null) {
                loggedInClient.setName(newName);
                repo.updateCompany(loggedInClient.getCompany());
            }
        } else {
            throw new WrongInputException("The given name is not valid.");
        }
    }

    /**
     * Method to update user address
     * 
     * @param newAddress New address of the user
     * @throws WrongInputException
     */
    public void updateAddress(String newAddress) throws WrongInputException {
        if (User.validateAddress(newAddress)) {
            if (loggedInCompany != null) {
                loggedInCompany.setAddress(newAddress);
                repo.updateCompany(loggedInCompany);
            } else if (loggedInClient != null) {
                loggedInClient.setAddress(newAddress);
                repo.updateCompany(loggedInClient.getCompany());
            }
        } else {
            throw new WrongInputException("The given address is not valid.");
        }
    }

    /**
     * Method to update user reference person
     * 
     * @param newRefPerson New reference person of the user
     * @throws WrongInputException
     */
    public void updateRefPerson(String newRefPerson) throws WrongInputException {
        if (User.validateRefPerson(newRefPerson)) {
            if (loggedInCompany != null) {
                loggedInCompany.setRefPerson(newRefPerson);
                repo.updateCompany(loggedInCompany);
            } else if (loggedInClient != null) {
                loggedInClient.setRefPerson(newRefPerson);
                repo.updateCompany(loggedInClient.getCompany());
            }
        } else {
            throw new WrongInputException("The given reference name is not valid.");
        }
    }

    /**
     * Method to update user email
     * 
     * @param newEmail New email of the user
     * @throws WrongInputException
     */
    public void updateEmail(String newEmail) throws WrongInputException {
        if (User.validateEmail(newEmail) && !getAllEmails().contains(newEmail)) {
            if (loggedInCompany != null) {
                loggedInCompany.setEmail(newEmail);
                repo.updateCompany(loggedInCompany);
            } else if (loggedInClient != null) {
                loggedInClient.setEmail(newEmail);
                repo.updateCompany(loggedInClient.getCompany());
            }
        } else {
            throw new WrongInputException("The given email is not valid.");
        }
    }

    /**
     * Method to update user password
     * 
     * @param newPassword New password of the user
     * @throws WrongInputException
     */
    public void updatePassword(String newPassword) throws WrongInputException {
        if (User.validatePassword(newPassword)) {
            if (loggedInCompany != null) {
                loggedInCompany.setPassword(User.SHA1_Hasher(newPassword));
                repo.updateCompany(loggedInCompany);
            } else if (loggedInClient != null) {
                loggedInClient.setPassword(User.SHA1_Hasher(newPassword));
                repo.updateCompany(loggedInClient.getCompany());
            }
        } else {
            throw new WrongInputException("The given password is not valid.");
        }
    }

    /**
     * Method to find all existing emails in the system
     * 
     * @return List of all user emails
     */
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
            loggedInCompany.addContainer(c);
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
     * @return requested Journey
     * @throws IOException
     */
    public Journey requestNewJourney(String originPort, String destinationPort, String content) throws IOException {
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
        if (loggedInCompany != null) {
            Journey journey = getJourneyById(journey_id);
            if (journey != null && journey.getCompany() == loggedInCompany && journey.isStarted()
                    && journey.getStartTimestamp().isBefore(status.getTimestamp()) && !journey.isEnded()) {
                journey.addStatus(status);
                repo.updateCompany(loggedInCompany);
                return true;
            }
        }
        return false;
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
            Journey journey = getJourneyById(journey_id);
            Container container = loggedInCompany.getAvailableContainer(timestamp);
            if (journey != null && !journey.isStarted() && container != null && timestamp != null) {
                journey.setContainer(container);
                journey.setStartTimestamp(timestamp);
                journey.setStarted();
                journey.getContainer().addJourney(journey);
                return true;
            }
        }
        return false;
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
            Journey journey = getJourneyById(journey_id);
            if (journey != null && !journey.isEnded() && journey.isStarted()
                    && journey.isValidEndTimestamp(timestamp)) {
                journey.setEndTimestamp(timestamp);
                journey.setEnded();
                return true;
            }
        }
        return false;
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
                for (Client cl : c.getClients()) {
                    if (cl.getEmail().equals(email)) {
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
     * @return List of clients
     */
    public List<Client> searchForClients(String query) {
        return searchForClients(query, true, true, true, true);
    }

    /**
     * Searches for clients of logged in logistics company by specified parameters
     * 
     * @param query       Query to be searched for
     * @param byName      enable search by name
     * @param byAddress   enable search by address
     * @param byRefPerson enable search by reference person
     * @param byEmail     enable search by email
     * @return
     */
    public List<Client> searchForClients(String query, boolean byName, boolean byAddress, boolean byRefPerson,
            boolean byEmail) {
        Set<Client> resultList = new HashSet<>();
        if (byName)
            resultList.addAll(loggedInCompany.searchClientByName(query));
        if (byAddress)
            resultList.addAll(loggedInCompany.searchClientByAddress(query));
        if (byRefPerson)
            resultList.addAll(loggedInCompany.searchClientByRefPerson(query));
        if (byEmail)
            resultList.addAll(loggedInCompany.searchClientByEmail(query));
        return new ArrayList<>(resultList);
    }

    /**
     * Searches for journeys of logged in client by all parameters
     * 
     * @param query Query to be searched for
     * @return List of journeys
     */
    public List<Journey> searchForJourneys(String query) {
        return searchForJourneys(query, true, true, true);
    }

    /**
     * Searches for journeys of logged in client by specified parameters
     * 
     * @param query         Query to be searched for
     * @param byOrigin      enable search by name
     * @param byDestination enable search by address
     * @param byContent     enable search by reference person
     * @return
     */
    public List<Journey> searchForJourneys(String query, boolean byOrigin, boolean byDestination, boolean byContent) {
        Set<Journey> resultList = new HashSet<>();
        if (byOrigin)
            resultList.addAll(loggedInClient.searchJourneyByOrigin(query));
        if (byDestination)
            resultList.addAll(loggedInClient.searchJourneyByDestination(query));
        if (byContent)
            resultList.addAll(loggedInClient.searchJourneyByContent(query));
        return new ArrayList<>(resultList);
    }

    /**
     * Searches for shared journeys of logged in client by all parameters
     * 
     * @param query Query to be searched for
     * @return List of shared journeys
     */
    public List<Journey> searchForSharedJourneys(String query) {
        return searchForSharedJourneys(query, true, true, true);
    }

    /**
     * Searches for shared journeys of logged in client by specified parameters
     * 
     * @param query         Query to be searched for
     * @param byOrigin      enable search by name
     * @param byDestination enable search by address
     * @param byContent     enable search by reference person
     * @return
     */
    public List<Journey> searchForSharedJourneys(String query, boolean byOrigin, boolean byDestination,
            boolean byContent) {
        Set<Journey> resultList = new HashSet<>();
        if (byOrigin)
            resultList.addAll(loggedInClient.searchSharedJourneyByOrigin(query));
        if (byDestination)
            resultList.addAll(loggedInClient.searchSharedJourneyByDestination(query));
        if (byContent)
            resultList.addAll(loggedInClient.searchSharedJourneyByContent(query));
        return new ArrayList<>(resultList);
    }

    /**
     * Searches for clients journeys of logged in client by all parameters
     * 
     * @param client_id Id of the client to be searched for journeys
     * @param query     Query to be searched for
     * @return List of shared journeys
     */
    public List<Journey> searchForClientsJourneys(int client_id, String query) {
        return searchForClientsJourneys(client_id, query, true, true, true);
    }

    /**
     * Searches for clients journeys of logged in client by specified parameters
     * 
     * @param client_id     Id of the client to be searched for journeys
     * @param query         Query to be searched for
     * @param byOrigin      enable search by name
     * @param byDestination enable search by address
     * @param byContent     enable search by reference person
     * @return
     */
    public List<Journey> searchForClientsJourneys(int client_id, String query, boolean byOrigin, boolean byDestination,
            boolean byContent) {
        Client client = getClientById(client_id);
        Set<Journey> resultList = new HashSet<>();
        if (byOrigin)
            resultList.addAll(client.searchJourneyByOrigin(query));
        if (byDestination)
            resultList.addAll(client.searchJourneyByDestination(query));
        if (byContent)
            resultList.addAll(client.searchJourneyByContent(query));
        return new ArrayList<>(resultList);
    }

    /**
     * Searches for shared journeys of logged in client by all parameters
     * 
     * @param query Query to be searched for
     * @return List of shared journeys
     */
    public List<Journey> searchForContainersJourneys(int container_id, String query) {
        return searchForContainersJourneys(container_id, query, true, true, true);
    }

    /**
     * Searches for containers journeys of logged in client by specified parameters
     * 
     * @param query         Query to be searched for
     * @param byOrigin      enable search by name
     * @param byDestination enable search by address
     * @param byContent     enable search by reference person
     * @param client_id     Id of the client to be searched for journeys
     * 
     * @return
     */
    public List<Journey> searchForContainersJourneys(int container_id, String query, boolean byOrigin,
            boolean byDestination, boolean byContent) {
        Container container = getContainerById(container_id);
        Set<Journey> resultList = new HashSet<Journey>();
        if (byOrigin)
            resultList.addAll(container.searchJourneyByOrigin(query));
        if (byDestination)
            resultList.addAll(container.searchJourneyByDestination(query));
        if (byContent)
            resultList.addAll(container.searchJourneyByContent(query));
        return new ArrayList<>(resultList);
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
     * Requests status for given journey of a client
     * 
     * @param client_id  Client id of the client owning the journey
     * @param journey_id Journey id to get status history out of
     * @return List of the Container Statuses
     */
    public List<ContainerStatus> requestClientsStatus(int client_id, int journey_id) {
        if (loggedInCompany != null) {
            Client client = getClientById(client_id);
            if (loggedInCompany.getClients().contains(client)) {
                return client.requestStatus(getJourneyById(journey_id));
            }
        }
        return null;
    }

    /**
     * Requests status for given journey
     * 
     * @param container_id Container id of the container involved in the journey
     * @param journey_id   Journey id to get status history out of
     * @return List of the Container Statuses
     */
    public List<ContainerStatus> requestContainersStatus(int container_id, int journey_id) {
        if (loggedInCompany != null) {
            Container container = getContainerById(container_id);
            if (loggedInCompany.getContainers().contains(container)) {
                return container.requestStatus(getJourneyById(journey_id));
            }
        }
        return null;
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
     * Returns all journeys of a given container
     * 
     * @param container_id The ID of container to check for
     * @return List of journeys of a given container
     */
    public List<Journey> requestContainersJourneys(int container_id) {
        return getAllJourneys().stream().filter(j -> j.checkContainerById(container_id)).collect(Collectors.toList());
    }

    /**
     * Returns all journeys of a given client
     * 
     * @param client_id The ID of client to check for
     * @return List of journeys of a given client
     */
    public List<Journey> requestClientsJourneys(int client_id) {
        return getAllJourneys().stream().filter(j -> j.getClient().getId() == client_id).collect(Collectors.toList());
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

    /**
     * Method to find a valid client Id from an email
     * 
     * @param email of client to be found
     * @return Id of found client
     */
    public Integer searchClientIdByEmail(String email) {
        Integer id = null;
        for (Client c : getAllClients()) {
            if (c.getEmail().equals(email)) {
                id = c.getId();
            }
        }
        return id;
    }

    /**
     * Returns all Clients in the system
     * 
     * @return List of all clients in the system
     */
    private List<Client> getAllClients() {
        List<Client> clients = new LinkedList<>();
        for (LogisticsCompany c : system) {
            clients.addAll(c.getClients());
        }
        return clients;
    }

    /**
     * Returns all Journeys in the system
     * 
     * @return List of all journeys in the system
     */
    private List<Journey> getAllJourneys() {
        List<Journey> journeys = new LinkedList<>();
        for (Client c : getAllClients()) {
            journeys.addAll(c.getJourneyList());
        }
        return journeys;
    }

    /**
     * Returns client with given ID
     * 
     * @param id ID of the client to be retrieved
     * @return Client object if found otherwise null
     */
    public Client getClientById(int id) {
        for (Client c : getAllClients()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Returns journey with given ID
     * 
     * @param id ID of the journey to be retrieved
     * @return Journey object if found otherwise null
     */
    public Journey getJourneyById(int id) {
        for (Journey j : getAllJourneys()) {
            if (j.getId() == id) {
                return j;
            }
        }
        return null;
    }

    /**
     * Returns container with given ID
     * 
     * @param id ID of the container to be retrieved
     * @return object if found otherwise null
     */
    public Container getContainerById(int id) {
        for (Container c : requestContainers()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * Informs the presentation layer about change inside the system
     * 
     * @param change The change name
     * @param obj    The object related to the change
     */
    public void fireChange(String change, Object obj) {
        support.firePropertyChange(change, null, obj);
    }

    /**
     * Informs the presentation layer about change inside the system
     * 
     * @param change The change name
     */
    public void fireChange(String change) {
        support.firePropertyChange(change, null, null);
    }
}
