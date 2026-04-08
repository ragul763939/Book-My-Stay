import java.util.HashMap;
import java.util.Map;

/**
 * UseCase4RoomSearch
 * 
 * Demonstrates read-only room search using centralized inventory.
 * Ensures no modification of system state during search.
 * 
 * @author Ragul
 * @version 4.0
 */

// ----------- Domain Model ------------

// Abstract Room class
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type : " + type);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : ₹" + price);
    }

    public String getType() {
        return type;
    }
}

// Concrete Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }
}

// ----------- Inventory (State Holder) ------------

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // Example: unavailable
        inventory.put("Suite Room", 2);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}

// ----------- Search Service ------------

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Read-only search
    public void searchAvailableRooms(Room[] rooms) {

        System.out.println("---- Available Rooms ----\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getType());

            // Validation: show only available rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available : " + available);
                System.out.println("----------------------------------");
            }
        }
    }
}

// ----------- Main Class ------------

public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v4.0");
        System.out.println("=======================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects
        Room[] rooms = {
            new SingleRoom(),
            new DoubleRoom(),
            new SuiteRoom()
        };

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (READ-ONLY)
        searchService.searchAvailableRooms(rooms);

        System.out.println("\nSearch completed. Inventory remains unchanged.");
    }
}
