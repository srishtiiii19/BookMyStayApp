import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor → initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register room type with count
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Book a room (decrease count)
    public boolean bookRoom(String roomType) {
        int available = getAvailability(roomType);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    // Cancel booking (increase count)
    public void releaseRoom(String roomType) {
        int available = getAvailability(roomType);
        inventory.put(roomType, available + 1);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

//main
public class HotelBookingApp {

    public static void main(String[] args) {

        // Create Room objects (Domain)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize Inventory (State)
        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType(single.getType(), 5);
        inventory.addRoomType(doubleRoom.getType(), 3);
        inventory.addRoomType(suite.getType(), 2);

        // Display Room Details
        System.out.println("===== Room Details =====\n");

        single.displayDetails();
        System.out.println();

        doubleRoom.displayDetails();
        System.out.println();

        suite.displayDetails();
        System.out.println();

        // Display Inventory
        inventory.displayInventory();

        // Simulate Booking
        System.out.println("\nBooking a Single Room...");
        if (inventory.bookRoom("Single Room")) {
            System.out.println("Booking Successful");
        } else {
            System.out.println("No rooms available");
        }

        // Display Updated Inventory
        inventory.displayInventory();
    }
}