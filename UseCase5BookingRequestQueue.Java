import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase5BookingRequestQueue
 * 
 * Demonstrates booking request handling using Queue (FIFO)
 * to ensure fairness (First-Come-First-Served).
 * 
 * No inventory updates are performed at this stage.
 * 
 * @author Ragul
 * @version 5.0
 */

// ----------- Reservation Class ------------

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
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}

// ----------- Booking Queue ------------

class BookingQueue {

    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n---- Booking Request Queue ----");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
            System.out.println("------------------------------");
        }
    }
}

// ----------- Main Class ------------

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v5.0");
        System.out.println("=======================================\n");

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate guest booking requests
        Reservation r1 = new Reservation("Ragul", "Single Room");
        Reservation r2 = new Reservation("Arun", "Double Room");
        Reservation r3 = new Reservation("Priya", "Suite Room");

        // Add requests to queue (FIFO order)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queued requests
        bookingQueue.displayQueue();

        System.out.println("\nAll requests are stored in arrival order (FIFO).");
        System.out.println("No rooms allocated yet. Waiting for processing...");
    }
}
