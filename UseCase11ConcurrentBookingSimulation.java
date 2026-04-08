import java.util.*;

/**
 * UseCase11ConcurrentBookingSimulation
 * 
 * Demonstrates thread-safe booking using synchronization.
 * Prevents race conditions and double booking under concurrent load.
 * 
 * @author Ragul
 * @version 11.0
 */

// ----------- Reservation ------------

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
}

// ----------- Thread-Safe Booking Queue ------------

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized to ensure safe access
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println(Thread.currentThread().getName() +
                " added request for " + r.getGuestName());
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// ----------- Thread-Safe Inventory ------------

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
    }

    // Critical section (only one thread at a time)
    public synchronized boolean allocateRoom(String type) {

        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            // Simulate processing delay (to expose race conditions if unsynchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(type, available - 1);
            return true;
        }

        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// ----------- Booking Processor (Thread) ------------

class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // synchronized queue access
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            System.out.println(getName() + " processing " + r.getGuestName());

            // synchronized allocation
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println("✅ " + r.getGuestName() + " booked successfully");
            } else {
                System.out.println("❌ " + r.getGuestName() + " booking failed (No rooms)");
            }
        }
    }
}

// ----------- Main Class ------------

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v11.0");
        System.out.println("=======================================\n");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Ragul", "Single Room"));
        queue.addRequest(new Reservation("Arun", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room")); // should fail

        // Create multiple threads (simulate concurrency)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        // Start threads
        t1.start();
        t2.start();

        // Wait for threads to finish
        t1.join();
        t2.join();

        // Final state
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely under concurrency.");
    }
}
