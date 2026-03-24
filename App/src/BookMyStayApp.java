class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

//fifo
import java.util.LinkedList;
import java.util.Queue;

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View next request (peek)
    public Reservation viewNextRequest() {
        return queue.peek();
    }

    // Remove request (dequeue)
    public Reservation getNextRequest() {
        return queue.poll();
    }

    // Display all requests
    public void displayQueue() {
        System.out.println("\n===== Booking Request Queue =====");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }
}

//main
public class HotelBookingApp {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulating guest requests (arrival order)
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add to queue (FIFO order)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Display queue
        requestQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.viewNextRequest();
        if (next != null) {
            next.display();
        }
    }
}