class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
//validation
import java.util.*;

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Room count cannot be negative.");
        }
        inventory.put(type, count);
    }

    public boolean hasRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Safe booking with validation
    public void bookRoom(String type)
            throws InvalidRoomTypeException, InvalidBookingException {

        if (!hasRoomType(type)) {
            throw new InvalidRoomTypeException("Invalid room type: " + type);
        }

        int available = getAvailability(type);

        if (available <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for: " + type
            );
        }

        inventory.put(type, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nInventory State:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

//layer
class BookingValidator {

    public static void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException, InvalidRoomTypeException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type existence
        if (!inventory.hasRoomType(roomType)) {
            throw new InvalidRoomTypeException("Room type does not exist: " + roomType);
        }

        // Validate availability
        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("Selected room is not available.");
        }
    }
}

//safe
class BookingService {

    private RoomInventory inventory;
    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(String guestName, String roomType) {

        try {
            // Step 1: Validate (FAIL FAST)
            BookingValidator.validate(guestName, roomType, inventory);

            // Step 2: Allocate
            inventory.bookRoom(roomType);

            String reservationId = generateId(roomType);

            System.out.println("Booking Confirmed!");
            System.out.println("Guest: " + guestName);
            System.out.println("Room Type: " + roomType);
            System.out.println("Reservation ID: " + reservationId);
            System.out.println();

        } catch (InvalidRoomTypeException | InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch unexpected errors
            System.out.println("Unexpected error occurred.");
        }
    }

    private String generateId(String type) {
        return type.replace(" ", "").toUpperCase() + "-" + (idCounter++);
    }
}

//main
public class ValidationApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0);

        BookingService service = new BookingService(inventory);

        // VALID booking
        service.processBooking("Alice", "Single Room");

        // INVALID: no availability
        service.processBooking("Bob", "Double Room");

        // INVALID: wrong room type
        service.processBooking("Charlie", "Suite Room");

        // INVALID: empty name
        service.processBooking("", "Single Room");

        // System continues safely
        inventory.displayInventory();
    }
}