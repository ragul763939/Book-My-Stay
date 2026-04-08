import java.util.*;

/**
 * UseCase8BookingHistoryReport
 * 
 * Demonstrates booking history storage and reporting using List.
 * Maintains chronological order and supports reporting without
 * modifying stored data.
 * 
 * @author Ragul
 * @version 8.0
 */

// ----------- Reservation ------------

class Reservation {
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

    public void display() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
    }
}

// ----------- Booking History ------------

class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("Reservation stored: " + r.getReservationId());
    }

    // Get all bookings (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ----------- Reporting Service ------------

class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {

        System.out.println("\n---- Booking History ----");

        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
            System.out.println("--------------------------");
        }
    }

    // Generate summary report
    public void generateSummary() {

        System.out.println("\n---- Booking Summary Report ----");

        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String type = r.getRoomType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + " Bookings : " + entry.getValue());
        }
    }
}

// ----------- Main Class ------------

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Book My Stay App - v8.0");
        System.out.println("=======================================\n");

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("SINGLEROOM-1", "Ragul", "Single Room"));
        history.addReservation(new Reservation("DOUBLEROOM-1", "Arun", "Double Room"));
        history.addReservation(new Reservation("SUITEROOM-1", "Priya", "Suite Room"));
        history.addReservation(new Reservation("SINGLEROOM-2", "Kiran", "Single Room"));

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Display all bookings
        reportService.displayAllBookings();

        // Generate summary report
        reportService.generateSummary();

        System.out.println("\nReporting completed. No data was modified.");
    }
}
