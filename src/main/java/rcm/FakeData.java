/**
 * 
 */
package rcm;

import java.io.IOException;
import java.time.LocalDateTime;

import rcm.model.Application;
import rcm.model.ContainerStatus;
import rcm.model.Journey;
import rcm.model.WrongInputException;

/**
 * @author ATB
 *
 */
public class FakeData {
    public static void setupFakeApp(Application app) throws WrongInputException, IOException {
        app.createNewLogisticsCompany("Agrofert", "Rendebanen 6D, 6000 Kolding", "Peter Hansen",
                "peter@3plogistics.dk", "Password12345");
        app.createNewLogisticsCompany("Maersk", "Esplanaden 50, 6000 Kolding", "Cristian Hansen",
                "christiam@3maersk.dk", "Password12345");

        app.logInUser("peter@3plogistics.dk", "Password12345");
        app.createNewClient("CBS", "Byhojen 2", "Tom Hanks", "tom@cbs.dk", "Password12345");
        app.createNewClient("Novozymes", "Smorumvej 43", "William Andersen", "linea@novozymes.dk",
                "Password12345");
        for (int i = 0; i < 8; i++) {
            app.createNewContainer();
        }

        app.logInUser("christiam@3maersk.dk", "Password12345");
        app.createNewClient("DTU", "Tekniksevej 43", "Pablo Escobar", "linea@dtu.dk", "Password12345");
        app.createNewClient("Netto", "Ondrovej 43", "Oscar Vinci", "dave@netto.dk", "Password12345");
        for (int i = 0; i < 7; i++) {
            app.createNewContainer();
        }

        app.logInUser("tom@cbs.dk", "Password12345");
        Journey j1 = app.requestNewJourney("Copenhagen", "New York", "bananas", null);
        Journey j3 = app.requestNewJourney("Rotterdam", "London", "cocaine", null);

        app.logInUser("linea@novozymes.dk", "Password12345");
        Journey j2 = app.requestNewJourney("Pearl Harbor", "Tokyo", "robots", null);
        Journey j4 = app.requestNewJourney("Hong Kong", "New York", "people", null);

        app.logInUser("linea@dtu.dk", "Password12345");
        Journey j5 = app.requestNewJourney("Los Angeles", "Rio de Janeiro", "furniture", null);
        Journey j7 = app.requestNewJourney("Rome", "Beijing", "toys", null);

        app.logInUser("dave@netto.dk", "Password12345");
        Journey j6 = app.requestNewJourney("Copenhagen", "Cairo", "cars", null);
        app.requestNewJourney("New Dili", "New York", "steel", null);

        app.logInUser("peter@3plogistics.dk", "Password12345");
        app.startJourney(j1, LocalDateTime.of(2019, 4, 22, 15, 0));
        app.startJourney(j2, LocalDateTime.of(2019, 4, 23, 15, 0));
        app.startJourney(j3, LocalDateTime.of(2019, 4, 24, 15, 0));
        app.startJourney(j4, LocalDateTime.of(2019, 4, 25, 15, 0));
        app.enterNewContainerStatus(j1, new ContainerStatus(LocalDateTime.of(2020, 4, 22, 15, 0), 35.0, 20.0, 103.0, "Copenhagen"));
        app.enterNewContainerStatus(j1, new ContainerStatus(LocalDateTime.of(2020, 4, 23, 15, 0), 39.0, 24.0, 102.0, "New York"));
        app.enterNewContainerStatus(j1, new ContainerStatus(LocalDateTime.of(2020, 4, 24, 15, 0), 12.0, 20.0, 121.0, "Atlantic Ocean"));
        app.enterNewContainerStatus(j2, new ContainerStatus(LocalDateTime.of(2020, 4, 20, 15, 0), 35.0, 5.0, 90.0, "Pacific Ocean"));
        app.enterNewContainerStatus(j3, new ContainerStatus(LocalDateTime.of(2020, 4, 30, 15, 0), 0.0, 89.0, 101.0, "Indian Ocean"));
        app.enterNewContainerStatus(j3, new ContainerStatus(LocalDateTime.of(2020, 5, 22, 19, 32), -35.0, 20.0, 101.0, "San Diego"));
        app.enterNewContainerStatus(j4, new ContainerStatus(LocalDateTime.of(2020, 4, 22, 17, 0), -35.0, 2.0, 101.0, "Wakanda"));
        app.enterNewContainerStatus(j4, new ContainerStatus(LocalDateTime.of(2020, 4, 23, 15, 0), 53.0, 2.0, 101.0, "Atlantida"));
        app.enterNewContainerStatus(j4, new ContainerStatus(LocalDateTime.of(2020, 4, 24, 15, 0), 35.0, 12.0, 132.0, "Copenhagen"));

        app.logInUser("christiam@3maersk.dk", "Password12345");
        app.startJourney(j5, LocalDateTime.of(2019, 4, 26, 15, 0));
        app.startJourney(j6, LocalDateTime.of(2019, 4, 28, 15, 0));
        app.startJourney(j7, LocalDateTime.of(2019, 4, 28, 15, 0));
        app.enterNewContainerStatus(j5, new ContainerStatus(LocalDateTime.of(2020, 4, 05, 15, 0), 35.0, 20.0, 91.0, "Prague"));
        app.enterNewContainerStatus(j5, new ContainerStatus(LocalDateTime.of(2020, 4, 23, 15, 0), 45.0, 20.0, 101.0, "Moscow"));
        app.enterNewContainerStatus(j5, new ContainerStatus(LocalDateTime.of(2020, 4, 27, 15, 0), 3.0, 32.0, 121.0, "Red Sea"));
        app.enterNewContainerStatus(j7, new ContainerStatus(LocalDateTime.of(2020, 4, 23, 15, 0), 5.0, 20.0, 111.0, "Dead Sea"));
        app.enterNewContainerStatus(j7, new ContainerStatus(LocalDateTime.of(2020, 4, 24, 15, 0), 35.0, 24.0, 111.0, "Black Sea"));
        app.enterNewContainerStatus(j7, new ContainerStatus(LocalDateTime.of(2020, 4, 25, 15, 0), 35.0, 20.0, 108.0, "Copenhagen"));
        app.enterNewContainerStatus(j7, new ContainerStatus(LocalDateTime.of(2020, 4, 26, 15, 0), 35.0, 24.0, 101.0, "North Pole"));
        app.enterNewContainerStatus(j7, new ContainerStatus(LocalDateTime.of(2020, 4, 27, 15, 0), 35.0, 20.0, 121.0, "Antarctida"));
        app.enterNewContainerStatus(j6, new ContainerStatus(LocalDateTime.of(2020, 4, 22, 15, 0), 35.0, 6.0, 106.0, "Copenhagen"));
        app.enterNewContainerStatus(j6, new ContainerStatus(LocalDateTime.of(2020, 4, 23, 15, 0), 35.0, 5.0, 107.0, "Copenhagen"));
    }
}