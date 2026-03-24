abstract class Room {
    private String type;
    private int beds;
    private double size;
    private double price;

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}
//single room

class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 150.0, 2000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

//double room
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 250.0, 3500.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

//suite room
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 400.0, 6000.0);
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq ft");
        System.out.println("Price: ₹" + getPrice());
    }
}
//main
public class HotelBookingApp {

    public static void main(String[] args) {

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Polymorphism: using Room reference
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("===== Hotel Room Details =====\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}