import java.io.*;
import java.util.*;

/**
 * UseCase12DataPersistenceRecovery
 * 
 * Demonstrates persistence using serialization and recovery using deserialization.
 * Ensures system state survives application restarts.
 * 
 * @author Ragul
 * @version 12.0
 */

// ----------- Reservation (Serializable) ------------

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
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
}

// ----------- System State (Serializable Wrapper) ------------

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// ----------- Persistence Service ------------

class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state: " + e.getMessage());
        }

        return null;
    }
}

// ----------- Main Class ------------

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v12.0");
        System.out.println("=======================================\n");

        PersistenceService persistence = new PersistenceService();

        // Try loading previous state
        SystemState state = persistence.load();

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        if (state == null) {
            // Fresh start
            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);

            bookings = new ArrayList<>();
            bookings.add(new Reservation("SINGLEROOM-1", "Ragul", "Single Room"));

            System.out.println("Initialized new system state.");

        } else {
            // Restore state
            inventory = state.inventory;
            bookings = state.bookings;

            System.out.println("Restored Inventory: " + inventory);
            System.out.println("Restored Bookings Count: " + bookings.size());
        }

        // Simulate system shutdown (save state)
        System.out.println("\nSaving system state before shutdown...");
        persistence.save(new SystemState(inventory, bookings));

        System.out.println("\nRestart the program to see recovery in action.");
    }
}
