import java.util.HashMap;
import java.util.Map;

/**
 * UseCase9ErrorHandlingValidation
 * 
 * Demonstrates validation and error handling using custom exceptions.
 * Ensures system stability by preventing invalid operations.
 * 
 * @author Ragul
 * @version 9.0
 */

// ----------- Custom Exception ------------

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// ----------- Inventory ------------

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 0); // unavailable
        inventory.put("Suite Room", 1);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) throws InvalidBookingException {
        int available = getAvailability(type);

        if (available <= 0) {
            throw new InvalidBookingException("No availability for " + type);
        }

        inventory.put(type, available - 1);
    }
}

// ----------- Validator ------------

class BookingValidator {

    private RoomInventory inventory;

    public BookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void validate(Reservation r) throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + r.getRoomType());
        }
    }
}

// ----------- Booking Service ------------

class BookingService {

    private RoomInventory inventory;
    private BookingValidator validator;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator(inventory);
    }

    public void processBooking(Reservation r) {

        try {
            // Step 1: Validate (Fail-Fast)
            validator.validate(r);

            // Step 2: Allocate (safe)
            inventory.decrement(r.getRoomType());

            System.out.println("Booking SUCCESS for " + r.getGuestName() +
                    " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

// ----------- Main Class ------------

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v9.0");
        System.out.println("=======================================\n");

        // Initialize system
        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Test cases
        Reservation r1 = new Reservation("Ragul", "Single Room"); // valid
        Reservation r2 = new Reservation("", "Suite Room");       // invalid name
        Reservation r3 = new Reservation("Arun", "Deluxe Room");  // invalid type
        Reservation r4 = new Reservation("Priya", "Double Room"); // no availability

        // Process bookings
        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4);

        System.out.println("\nSystem continues running safely after errors.");
    }
}
