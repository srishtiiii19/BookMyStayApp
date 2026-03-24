import java.io.Serializable;

class ReservationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public ReservationRecord(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}
//room inventory
import java.io.Serializable;
import java.util.*;

class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

//booking history
import java.io.Serializable;
import java.util.*;

class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ReservationRecord> records = new ArrayList<>();

    public void addRecord(ReservationRecord r) {
        records.add(r);
    }

    public List<ReservationRecord> getRecords() {
        return records;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (ReservationRecord r : records) {
            r.display();
        }
    }
}

//system state
import java.io.Serializable;

class SystemState implements Serializable {

    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

//persistence service
import java.io.*;

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // SAVE state
    public static void save(SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state.");
        }
    }

    // LOAD state
    public static SystemState load() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("\nSystem state loaded successfully.");
            return (SystemState) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("\nNo previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nError loading state. Starting with safe defaults.");
        }

        // fallback safe state
        return new SystemState(new RoomInventory(), new BookingHistory());
    }
}

//main
public class PersistenceApp {

    public static void main(String[] args) {

        // STEP 1: Load previous state (recovery)
        SystemState state = PersistenceService.load();

        RoomInventory inventory = state.inventory;
        BookingHistory history = state.history;

        // If fresh start, initialize data
        if (inventory.getInventory().isEmpty()) {
            inventory.addRoomType("Single Room", 2);
            inventory.addRoomType("Double Room", 1);

            history.addRecord(new ReservationRecord("S-1", "Alice", "Single Room"));
            history.addRecord(new ReservationRecord("D-1", "Bob", "Double Room"));
        }

        // Display current state
        inventory.display();
        history.display();

        // STEP 2: Simulate new booking
        history.addRecord(new ReservationRecord("S-2", "Charlie", "Single Room"));

        // STEP 3: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, history));
    }
}