package rcm;

public class SharedObjectHolder {

    private LogisticsCompany company1, company2;
    private Client client1, client2;
    private Journey journey1, journey2;
    private Container container;

    public LogisticsCompany getFirstCompany() {
        return company1;
    }

    public void setFirstCompany(LogisticsCompany company) {
        company1 = company;
    }

    public LogisticsCompany getSecondCompany() {
        return company2;
    }

    public void setSecondCompany(LogisticsCompany company) {
        company2 = company;
    }

    public Client getFirstClient() {
        return client1;
    }

    public void setFirstClient(Client client) {
        client1 = client;
    }

    public Client getSecondClient() {
        return client2;
    }

    public void setSecondClient(Client client) {
        client2 = client;
    }

    public Journey getFirstJourney() {
        return journey1;
    }

    public void setFirstJourney(Journey journey) {
        journey1 = journey;
    }

    public Journey getSecondJourney() {
        return journey2;
    }

    public void setSecondJourney(Journey journey) {
        journey2 = journey;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

}
