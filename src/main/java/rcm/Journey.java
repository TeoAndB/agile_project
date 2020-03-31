package rcm;

import java.util.LinkedList;

public class Journey {
    private static int idGlobal;
    private int id;
    private String originPort;
    private String destinationPort;
    private String content;
    private String currentPosition;

    private Container container;
    private Client client;

    private LinkedList<ContainerStatus> history;

    public Journey(String originPort, String destinationPort, String content, Container container, Client client) {
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.content = content;
        this.container = container;
        client.addJourney(this);
        this.client = client;
        history = new LinkedList<ContainerStatus>();
    }

    public void setID() {
        idGlobal++;
        this.id = idGlobal;
    }

    public int getID() {
        return this.id;
    }

    public LogisticsCompany getCompany() {
        return (container == null) ? null : container.getCompany();
    }

    public boolean addStatus(ContainerStatus status, LogisticsCompany company) {
        if (company.equals(getCompany())) {
            history.add(status);
            return true;
        } else {
            return false;
        }
    }

    public LinkedList<ContainerStatus> getStatus(Client client1) {
        return (client1.equals(client)) ? history : null;
    }

    public boolean containsStatus(ContainerStatus status) {
        return history.contains(status);
    }

}
