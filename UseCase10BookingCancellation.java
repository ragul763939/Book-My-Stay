import java.util.*;

/**
 * UseCase10BookingCancellation
 * 
 * Demonstrates safe booking cancellation with rollback using Stack (LIFO).
 * Ensures inventory consistency and prevents invalid cancellations.
 * 
 * @author Ragul
 * @version 10.0
 */

// ----------- Reservation ------------

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }
}

// ----------- Inventory ------------

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 0);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Booking History ------------

class BookingHistory {

    private Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// ----------- Cancellation Service ------------

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        Reservation r = history.getReservation(reservationId);

        // Validation
        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation not found");
            return;
        }

        if (!r.isActive()) {
            System.out.println("Cancellation FAILED: Already cancelled");
            return;
        }

        // Step 1: Record rollback
        rollbackStack.push(reservationId);

        // Step 2: Restore inventory
        inventory.increment(r.getRoomType());

        // Step 3: Update booking state
        r.cancel();

        System.out.println("Cancellation SUCCESS for: " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (Recent Cancellations) ---");
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

// ----------- Main Class ------------

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v10.0");
        System.out.println("=======================================");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService service = new CancellationService(inventory, history);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("SINGLEROOM-1", "Ragul", "Single Room");
        Reservation r2 = new Reservation("DOUBLEROOM-1", "Arun", "Double Room");

        history.addReservation(r1);
        history.addReservation(r2);

        // Perform cancellations
        service.cancelBooking("SINGLEROOM-1"); // valid
        service.cancelBooking("SINGLEROOM-1"); // duplicate
        service.cancelBooking("INVALID-1");    // invalid

        // Display results
        inventory.displayInventory();
        service.displayRollbackStack();

        System.out.println("\nSystem state restored safely after cancellation.");
    }
}
