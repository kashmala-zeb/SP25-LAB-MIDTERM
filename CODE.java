import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// ------------------------ MAIN APP ------------------------
public class BookingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingUI().setVisible(true));
    }
}

// ------------------------ PRESENTATION LAYER (UI) ------------------------
class BookingUI extends JFrame {
    private JTextField nameField;
    private JComboBox<String> transportTypeBox;
    private JComboBox<String> paymentMethodBox;
    private JTextArea outputArea;
    private JButton bookButton;

    public BookingUI() {
        setTitle("Smart Travel - Book a Ticket");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel for form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        formPanel.setBackground(new Color(245, 245, 245));

        formPanel.add(new JLabel("Traveler Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Transport Type:"));
        transportTypeBox = new JComboBox<>(new String[]{"Bus", "Train", "Airline"});
        formPanel.add(transportTypeBox);

        formPanel.add(new JLabel("Payment Method:"));
        paymentMethodBox = new JComboBox<>(new String[]{"Credit Card", "Wallet", "Bank Transfer"});
        formPanel.add(paymentMethodBox);

        bookButton = new JButton("Book Ticket");
        bookButton.setBackground(new Color(33, 150, 243));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(new BookActionListener());
        formPanel.add(new JLabel()); // spacer
        formPanel.add(bookButton);

        // Output area
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Booking Output"));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }

    class BookActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            outputArea.setText("");

            String name = nameField.getText().trim();
            String transport = transportTypeBox.getSelectedItem().toString();
            String payment = paymentMethodBox.getSelectedItem().toString();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(BookingUI.this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Traveler traveler = new Traveler(name);
            TransportProvider provider = TransportFactory.createProvider(transport);
            PaymentStrategy paymentStrategy = PaymentFactory.getStrategy(payment);

            BookingManager bookingManager = new BookingManager();
            bookingManager.addObserver(new EmailNotification());
            bookingManager.addObserver(new AdminLogger());

            String result = bookingManager.bookTicket(traveler, provider, paymentStrategy);
            outputArea.append(result);
        }
    }
}

// ------------------------ DOMAIN ENTITY ------------------------
class Traveler {
    private String name;

    public Traveler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// ------------------------ CONTROLLER (GRASP) ------------------------
class BookingManager {
    private List<BookingObserver> observers = new ArrayList<>();

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public String bookTicket(Traveler traveler, TransportProvider provider, PaymentStrategy paymentStrategy) {
        StringBuilder output = new StringBuilder();

        output.append(" Booking Ticket for: ").append(traveler.getName()).append("\n");
        output.append(" Reserving seat with ").append(provider.getType()).append(" provider...\n");
        provider.reserveSeat();

        output.append(" Processing payment via ").append(paymentStrategy.getType()).append("...\n");
        paymentStrategy.pay();

        String message = " Ticket successfully booked for " + traveler.getName() + " using " +
                provider.getType() + " and paid via " + paymentStrategy.getType() + ".\n";

        output.append(message);
        notifyObservers(message);

        return output.toString();
    }

    private void notifyObservers(String message) {
        for (BookingObserver observer : observers) {
            observer.update(message);
        }
    }
}

// ------------------------ FACTORY PATTERN ------------------------
class TransportFactory {
    public static TransportProvider createProvider(String type) {
        switch (type.toLowerCase()) {
            case "bus": return new BusProvider();
            case "train": return new TrainProvider();
            case "airline": return new AirlineProvider();
            default: return null;
        }
    }
}

class PaymentFactory {
    public static PaymentStrategy getStrategy(String type) {
        switch (type.toLowerCase()) {
            case "credit card": return new CreditCardPayment();
            case "wallet": return new WalletPayment();
            case "bank transfer": return new BankTransferPayment();
            default: return null;
        }
    }
}

// ------------------------ POLYMORPHISM: TRANSPORT PROVIDERS ------------------------
interface TransportProvider {
    void reserveSeat();
    String getType();
}

class BusProvider implements TransportProvider {
    public void reserveSeat() {
        System.out.println("[BusProvider] Seat reserved.");
    }

    public String getType() {
        return "Bus";
    }
}

class TrainProvider implements TransportProvider {
    public void reserveSeat() {
        System.out.println("[TrainProvider] Seat reserved.");
    }

    public String getType() {
        return "Train";
    }
}

class AirlineProvider implements TransportProvider {
    public void reserveSeat() {
        System.out.println("[AirlineProvider] Seat reserved.");
    }

    public String getType() {
        return "Airline";
    }
}

// ------------------------ STRATEGY PATTERN: PAYMENT METHODS ------------------------
interface PaymentStrategy {
    void pay();
    String getType();
}

class CreditCardPayment implements PaymentStrategy {
    public void pay() {
        System.out.println("[CreditCardPayment] Payment processed.");
    }

    public String getType() {
        return "Credit Card";
    }
}

class WalletPayment implements PaymentStrategy {
    public void pay() {
        System.out.println("[WalletPayment] Payment processed.");
    }

    public String getType() {
        return "Wallet";
    }
}

class BankTransferPayment implements PaymentStrategy {
    public void pay() {
        System.out.println("[BankTransferPayment] Payment processed.");
    }

    public String getType() {
        return "Bank Transfer";
    }
}

// ------------------------ OBSERVER PATTERN ------------------------
interface BookingObserver {
    void update(String message);
}

class EmailNotification implements BookingObserver {
    public void update(String message) {
        System.out.println("[EmailNotification] Email sent: " + message);
    }
}

class AdminLogger implements BookingObserver {
    public void update(String message) {
        System.out.println("[AdminLogger] Logged booking: " + message);
    }
}


