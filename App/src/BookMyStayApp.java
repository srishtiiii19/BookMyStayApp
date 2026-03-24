class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

//rollback
import java.util.*;

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void increaseAvailability(String type) {
        inventory.put(type, getAvailability(type) + 1);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

//tracking
import java.util.*;

class BookingRegistry {

    // reservationId → roomType
    private Map<String, String> activeBookings = new HashMap<>();

    public void addBooking(String reservationId, String roomType) {
        activeBookings.put(reservationId, roomType);
    }

    public boolean exists(String reservationId) {
        return activeBookings.containsKey(reservationId);
    }

    public String getRoomType(String reservationId) {
        return activeBookings.get(reservationId);
    }

    public void removeBooking(String reservationId) {
        activeBookings.remove(reservationId);
    }

    public void display() {
        System.out.println("\nActive Bookings:");
        for (Map.Entry<String, String> e : activeBookings.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

//core rollback
import java.util.*;

class CancellationService {

    private RoomInventory inventory;
    private BookingRegistry registry;

    // Stack to track released room IDs (LIFO rollback)
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingRegistry registry) {
        this.inventory = inventory;
        this.registry = registry;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        try {
            // Step 1: Validate existence
            if (!registry.exists(reservationId)) {
                throw new CancellationException(
                        "Invalid or already cancelled reservation: " + reservationId
                );
            }

            // Step 2: Get room type
            String roomType = registry.getRoomType(reservationId);

            // Step 3: Push to rollback stack
            rollbackStack.push(reservationId);

            // Step 4: Restore inventory
            inventory.increaseAvailability(roomType);

            // Step 5: Remove booking
            registry.removeBooking(reservationId);

            // Step 6: Confirmation
            System.out.println("Cancellation Successful!");
            System.out.println("Reservation ID: " + reservationId);
            System.out.println("Room Type Released: " + roomType);
            System.out.println();

        } catch (CancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent Cancellations): " + rollbackStack);
    }
}

//main
public class CancellationApp {

    public static void main(String[] args) {

        // Inventory setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);

        // Booking registry (simulate confirmed bookings)
        BookingRegistry registry = new BookingRegistry();
        registry.addBooking("SINGLEROOM-1", "Single Room");
        registry.addBooking("SINGLEROOM-2", "Single Room");

        registry.display();
        inventory.displayInventory();

        // Cancellation service
        CancellationService cancelService =
                new CancellationService(inventory, registry);

        // Valid cancellation
        cancelService.cancelBooking("SINGLEROOM-1");

        // Invalid cancellation (already removed)
        cancelService.cancelBooking("SINGLEROOM-1");

        // Another valid cancellation
        cancelService.cancelBooking("SINGLEROOM-2");

        // Final state
        registry.display();
        inventory.displayInventory();
        cancelService.displayRollbackStack();
    }
}