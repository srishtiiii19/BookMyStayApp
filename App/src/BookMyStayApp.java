class ReservationRecord {

    private String reservationId;
    private String guestName;
    private String roomType;

    public ReservationRecord(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

//storage
import java.util.*;

class BookingHistory {

    // Maintains insertion order
    private List<ReservationRecord> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addRecord(ReservationRecord record) {
        history.add(record);
    }

    // Retrieve all records (read-only usage)
    public List<ReservationRecord> getAllRecords() {
        return history;
    }

    // Display full history
    public void displayHistory() {

        System.out.println("\n===== Booking History =====");

        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (ReservationRecord r : history) {
            r.display();
        }
    }
}//reporting
import java.util.*;

class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Total bookings report
    public void totalBookingsReport() {
        int total = history.getAllRecords().size();
        System.out.println("\nTotal Bookings: " + total);
    }

    // Bookings per room type
    public void bookingsByRoomType() {

        Map<String, Integer> countMap = new HashMap<>();

        for (ReservationRecord r : history.getAllRecords()) {
            countMap.put(
                    r.getRoomType(),
                    countMap.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\nBookings by Room Type:");
        for (Map.Entry<String, Integer> e : countMap.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }

    // Guest-wise report
    public void bookingsByGuest() {

        Map<String, Integer> guestMap = new HashMap<>();

        for (ReservationRecord r : history.getAllRecords()) {
            guestMap.put(
                    r.getGuestName(),
                    guestMap.getOrDefault(r.getGuestName(), 0) + 1
            );
        }

        System.out.println("\nBookings by Guest:");
        for (Map.Entry<String, Integer> e : guestMap.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

//main
public class BookingHistoryApp {

    public static void main(String[] args) {

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addRecord(new ReservationRecord("SINGLEROOM-1", "Alice", "Single Room"));
        history.addRecord(new ReservationRecord("SINGLEROOM-2", "Bob", "Single Room"));
        history.addRecord(new ReservationRecord("DOUBLEROOM-1", "Charlie", "Double Room"));

        // Display history
        history.displayHistory();

        // Reporting
        BookingReportService reportService = new BookingReportService(history);

        reportService.totalBookingsReport();
        reportService.bookingsByRoomType();
        reportService.bookingsByGuest();
    }
}