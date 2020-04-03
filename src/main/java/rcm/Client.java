package rcm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedList;

public class Client extends User {

    private List<Journey> journeyList;

    public Client(String name, String address, String refPerson, String email) {
        super(name, address, refPerson, email);
        journeyList = new LinkedList<Journey>();
    }

    public void addJourney(Journey journey) {
        this.journeyList.add(journey);
    }

    public List<Journey> searchByDestination(String destination) {
        return journeyList.stream()
                .filter(j -> j.getDestinationPort().equals(destination))
                .collect(Collectors.toList());
        
        
        
//        LinkedList<Journey> filtered = new LinkedList<Journey>();
//        for (Journey j : journeyList) {
//            if (j.getDestinationPort().equals(destination)) {
//                filtered.add(j);
//            }
//        }
//        return filtered;
    }

    public LinkedList<Journey> searchByOrigin(String origin) {
        LinkedList<Journey> filtered = new LinkedList<Journey>();
        for (Journey j : journeyList) {
            if (j.getOriginPort().equals(origin)) {
                filtered.add(j);
            }
        }
        return filtered;
    }

    public LinkedList<Journey> searchByContent(String content) {
        LinkedList<Journey> filtered = new LinkedList<Journey>();
        for (Journey j : journeyList) {
            if (j.getContent().equals(content)) {
                filtered.add(j);
            }
        }
        return filtered;
    }


    public boolean updateClient(String newName, String newAddress, String newRefPerson, String newEmail) {
        name = newName;
        address = newAddress;
        refPerson = newRefPerson;
        email = newEmail;
        return true;
    }


    public boolean closeButton() {
        // TODO Auto-generated method stub
        return true;
        
    }
}
