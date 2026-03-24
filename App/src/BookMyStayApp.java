import java.util.List;

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Read-only search
    public void searchAvailableRooms(List<Room> rooms) {

        System.out.println("===== Available Rooms =====\n");

        boolean found = false;

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getType());

            // Defensive check: only show available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}
//main
import java.util.ArrayList;
import java.util.List;

public class HotelBookingApp {

    public static void main(String[] args) {

        // Room objects (Domain)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Store rooms in a list
        List<Room> rooms = new ArrayList<>();
        rooms.add(single);
        rooms.add(doubleRoom);
        rooms.add(suite);

        // Inventory (State)
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single.getType(), 2);
        inventory.addRoomType(doubleRoom.getType(), 0); // intentionally unavailable
        inventory.addRoomType(suite.getType(), 1);

        // Search Service (Read-only)
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Guest searches rooms
        searchService.searchAvailableRooms(rooms);

        // Verify state is unchanged
        System.out.println("===== Inventory After Search (Should be same) =====");
        inventory.displayInventory();
    }
}