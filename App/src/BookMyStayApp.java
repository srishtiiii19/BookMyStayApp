import java.util.*;

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // 🔒 Critical Section (synchronized)
    public synchronized boolean bookRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            // simulate delay (to expose race condition if not synchronized)
            try { Thread.sleep(50); } catch (InterruptedException e) {}

            inventory.put(type, available - 1);
            return true;
        }

        return false;
    }

    public synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

//reservation
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

//thread-safe
import java.util.*;

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}

//worker
class ConcurrentBookingProcessor implements Runnable {

    private RoomInventory inventory;
    private BookingRequestQueue queue;

    public ConcurrentBookingProcessor(RoomInventory inventory, BookingRequestQueue queue) {
        this.inventory = inventory;
        this.queue = queue;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // 🔒 synchronized retrieval
            synchronized (queue) {
                r = queue.getNextRequest();
            }

            if (r == null) break;

            boolean success = inventory.bookRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName()
                        + " booked for " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName()
                        + " FAILED for " + r.getGuestName());
            }
        }
    }
}

//main
public class ConcurrentBookingApp {

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        // Create multiple threads (concurrent users)
        Thread t1 = new Thread(new ConcurrentBookingProcessor(inventory, queue), "Thread-1");
        Thread t2 = new Thread(new ConcurrentBookingProcessor(inventory, queue), "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        t1.join();
        t2.join();

        // Final state
        inventory.displayInventory();
    }
}