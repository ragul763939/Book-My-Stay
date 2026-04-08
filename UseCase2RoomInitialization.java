/**
 * UseCase2RoomInitialization
 * 
 * This class demonstrates basic room modeling using abstraction,
 * inheritance, and polymorphism along with static availability.
 * 
 * @author Ragul
 * @version 2.1
 */

// Abstract class
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    // Constructor
    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type : " + type);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : ₹" + price);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }
}

// Main class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v2.1");
        System.out.println("=======================================\n");

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        System.out.println("---- Room Details & Availability ----\n");

        single.displayDetails();
        System.out.println("Available : " + singleAvailable);
        System.out.println("-------------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Available : " + doubleAvailable);
        System.out.println("-------------------------------------");

        suite.displayDetails();
        System.out.println("Available : " + suiteAvailable);
        System.out.println("-------------------------------------");
    }
}
