import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class BookingService {

    private RoomInventory inventory;
    private BookingRequestQueue requestQueue;

    // Track allocated room IDs per room type
    private Map<String, Set<String>> allocatedRooms;

    // Global set to ensure uniqueness across system
    private Set<String> allAllocatedRoomIds;

    private int idCounter = 1;

    public BookingService(RoomInventory inventory, BookingRequestQueue requestQueue) {
        this.inventory = inventory;
        this.requestQueue = requestQueue;
        this.allocatedRooms = new HashMap<>();
        this.allAllocatedRoomIds = new HashSet<>();
    }

    // Process all requests (FIFO)
    public void processBookings() {

        System.out.println("\n===== Processing Bookings =====");

        while (true) {

            Reservation request = requestQueue.getNextRequest();

            if (request == null) {
                System.out.println("No more requests.");
                break;
            }

            String roomType = request.getRoomType();

            int available = inventory.getAvailability(roomType);

            // Check availability
            if (available > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (extra safety)
                if (allAllocatedRoomIds.contains(roomId)) {
                    System.out.println("Duplicate room ID detected! Skipping...");
                    continue;
                }

                // Allocate room (atomic logical step)
                allAllocatedRoomIds.add(roomId);

                allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.bookRoom(roomType);

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();

            } else {
                System.out.println("Booking Failed for " + request.getGuestName() +
                        " (No " + roomType + " available)");
            }
        }
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.replace(" ", "").toUpperCase() + "-" + (idCounter++);
    }

    // Display allocated rooms
    public void displayAllocations() {
        System.out.println("\n===== Allocated Rooms =====");

        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

//main
public class HotelBookingApp {

    public static void main(String[] args) {

        // Inventory setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Request queue
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));

        // Booking service
        BookingService bookingService = new BookingService(inventory, queue);

        // Process bookings
        bookingService.processBookings();

        // Show final allocations
        bookingService.displayAllocations();

        // Show final inventory
        inventory.displayInventory();
    }
}