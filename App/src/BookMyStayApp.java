class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println(serviceName + " - ₹" + price);
    }
}

//core
import java.util.*;

class AddOnServiceManager {

    // reservationId → list of services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println(service.getServiceName() +
                " added to Reservation " + reservationId);
    }

    // Get all services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<AddOnService> services = getServices(reservationId);

        for (AddOnService s : services) {
            total += s.getPrice();
        }

        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        List<AddOnService> services = getServices(reservationId);

        System.out.println("\nServices for Reservation " + reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            s.display();
        }

        System.out.println("Total Add-On Cost: ₹" +
                calculateTotalCost(reservationId));
    }
}

//main
public class AddOnServiceApp {

    public static void main(String[] args) {

        // Sample reservation IDs (assume already created earlier)
        String res1 = "ROOM-101";
        String res2 = "ROOM-102";

        // Create service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        // Guest selects services
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);

        manager.addService(res2, wifi);
        manager.addService(res2, spa);

        // Display services and cost
        manager.displayServices(res1);
        manager.displayServices(res2);
    }
}