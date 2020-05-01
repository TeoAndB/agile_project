package rcm.unitTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rcm.model.Application;
import rcm.model.Client;
import rcm.model.LogisticsCompany;
import rcm.model.WrongInputException;
import rcm.repository.Repository;
import rcm.repository.SqliteRepository;

public class LogisticsCompanyTest {

    Application app;
    LogisticsCompany company1, company2;
    Client client1, client2;

    @Before
    public void init() throws WrongInputException, IOException {
        Repository repo = new SqliteRepository();
        app = new Application(repo);
        repo.clearDatabase();
        company1 = app.createNewLogisticsCompany("Maersk", "Esplanaden 50, 1098 Koebenhavn K", "Soeren Skou",
                "info@maersk.com", "Agile123");
        company2 = app.createNewLogisticsCompany("Hamburg Sud", "Willy-Brandt-Strasse 59, 20457 Hamburg, Germany",
                "Dr. Arnt Vespermann", "info@hamburgsud-line.com", "Agile123");
        app.logInUser("info@maersk.com", "Agile123");
        client1 = app.createNewClient("Novo Nordisk", "Novo Alle, 2880 Bagsvaerd", "Lars Fruergaard Joergensen",
                "info@novonordisk.com", "Agile123");
        app.logInUser("info@hamburgsud-line.com", "Agile123");
        client2 = app.createNewClient("Chiquita", "1855 Griffin Rd. Miami, Florida", "Carmen Rodriguez",
                "bananas@chiquita.com", "Agile123");
    }

    @Test
    public void TestWrongClientCreatingJourney() throws IOException, WrongInputException {
        app.logInUser("info@maersk.com", "Agile123");
        assertEquals(null,app.requestNewJourney("Nordhavn", "Rotterdam", "remoulade"));
        assertNotEquals(null, app.requestNewJourney("Nordhavn", "Rotterdam", "remoulade"));
        app.logOut();
    }

    @Test
    public void TestPartialStringSearch() throws WrongInputException {
        app.logInUser("info@maersk.com", "Agile123");
        Set<Client> result;
        result = company1.searchClientByName("Novo");
        assertTrue(result.contains(client1));
        result = company1.searchClientByName("OrDisk");
        assertTrue(result.contains(client1));
        result = company1.searchClientByAddress("lLe");
        assertTrue(result.contains(client1));
        result = company1.searchClientByRefPerson("Lars fru");
        assertTrue(result.contains(client1));
        result = company1.searchClientByEmail("iNfO");
        assertTrue(result.contains(client1));
        app.logOut();
    }
}
