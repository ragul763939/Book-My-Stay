import java.util.*;

/**
 * UseCase6RoomAllocationService
 * 
 * Demonstrates booking confirmation with safe room allocation.
 * Prevents double-booking using Set and maintains consistency
 * with centralized inventory updates.
 * 
 * @author Ragul
 * @version 6.0
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

// ----------- Booking Queue ------------

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ----------- Inventory Service ------------

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Updated Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Booking Service ------------

class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs per type
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Global set to ensure uniqueness
    private Set<String> allRoomIds = new HashSet<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String type, int number) {
        return type.replace(" ", "").toUpperCase() + "-" + number;
    }

    // Process booking
    public void processRequest(Reservation r) {

        String type = r.getRoomType();

        System.out.println("\nProcessing request for " + r.getGuestName());

        if (inventory.getAvailability(type) > 0) {

            // Initialize set if not present
            allocatedRooms.putIfAbsent(type, new HashSet<>());

            int roomNumber = allocatedRooms.get(type).size() + 1;
            String roomId = generateRoomId(type, roomNumber);

            // Ensure uniqueness
            while (allRoomIds.contains(roomId)) {
                roomNumber++;
                roomId = generateRoomId(type, roomNumber);
            }

            // Allocate
            allocatedRooms.get(type).add(roomId);
            allRoomIds.add(roomId);

            // Update inventory immediately
            inventory.decrement(type);

            System.out.println("Booking CONFIRMED for " + r.getGuestName());
            System.out.println("Room Type : " + type);
            System.out.println("Assigned Room ID : " + roomId);

        } else {
            System.out.println("Booking FAILED for " + r.getGuestName() + " (No availability)");
        }
    }
}

// ----------- Main Class ------------

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v6.0");
        System.out.println("=======================================");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingService service = new BookingService(inventory);

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Ragul", "Single Room"));
        queue.addRequest(new Reservation("Arun", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room")); // should fail

        // Process queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            service.processRequest(r);
        }

        // Show final inventory
        inventory.displayInventory();
    }
}
