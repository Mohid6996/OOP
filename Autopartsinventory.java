import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Abstract class (Abstraction)
abstract class Part {
    private static int counter = 1000; // Auto part number generator
    private final int partNumber;
    private String name;
    private double price;
    private int quantity;

    public Part(String name, double price, int quantity) {
        this.partNumber = counter++;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters (Encapsulation)
    public int getPartNumber() { return partNumber; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void addStock(int qty) {
        this.quantity += qty;
    }

    public boolean sellStock(int qty) {
        if (qty <= quantity) {
            quantity -= qty;
            return true;
        }
        return false;
    }

    // Abstract method (Abstraction + Polymorphism)
    public abstract String getType();

    // Common string
    public String toString() {
        return "[" + getType() + "] Part#" + partNumber + " | " + name + " | $" + price + " | Qty: " + quantity;
    }
}

// Subclasses (Inheritance)
class EnginePart extends Part {
    public EnginePart(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    public String getType() {
        return "Engine";
    }
}

class BodyPart extends Part {
    public BodyPart(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    public String getType() {
        return "Body";
    }
}

// Main Inventory GUI
public class Autopartsinventory extends JFrame {
    private ArrayList<Part> inventory = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> inventoryList = new JList<>(listModel);

    public Autopartsinventory() {
        setTitle("Auto Parts Inventory");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inventory display
        add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Part");
        JButton sellBtn = new JButton("Sell Part");
        JButton orderBtn = new JButton("Order Stock");
        buttonPanel.add(addBtn);
        buttonPanel.add(sellBtn);
        buttonPanel.add(orderBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addPart());
        sellBtn.addActionListener(e -> sellPart());
        orderBtn.addActionListener(e -> orderStock());

        // Preload sample parts
        inventory.add(new EnginePart("Spark Plug", 15.5, 30));
        inventory.add(new BodyPart("Door Handle", 12.0, 20));
        inventory.add(new EnginePart("forged piston", 190, 40));
        refreshList();
        setVisible(true);
    }

    // Add new part
    private void addPart() {
        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(5);
        JTextField qtyField = new JTextField(5);
        String[] types = {"Engine", "Body"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Type:"));
        panel.add(typeBox);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Qty:"));
        panel.add(qtyField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Part", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String type = (String) typeBox.getSelectedItem();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());

            Part part;
            if (type.equals("Engine")) {
                part = new EnginePart(name, price, qty);
            } else {
                part = new BodyPart(name, price, qty);
            }

            inventory.add(part); // Polymorphism
            refreshList();
        }
    }

    // Sell part by part number
    private void sellPart() {
        String input = JOptionPane.showInputDialog(this, "Enter Part Number:");
        int partNo = Integer.parseInt(input);
        int qty = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity to sell:"));

        for (Part part : inventory) {
            if (part.getPartNumber() == partNo) {
                if (part.sellStock(qty)) {
                    JOptionPane.showMessageDialog(this, "Sold " + qty + " of " + part.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock.");
                }
                refreshList();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Part not found.");
    }

    // Order stock
    private void orderStock() {
        String input = JOptionPane.showInputDialog(this, "Enter Part Number:");
        int partNo = Integer.parseInt(input);
        int qty = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity to add:"));

        for (Part part : inventory) {
            if (part.getPartNumber() == partNo) {
                part.addStock(qty);
                JOptionPane.showMessageDialog(this, "Stock updated.");
                refreshList();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Part not found.");
    }

    // Refresh list
    private void refreshList() {
        listModel.clear();
        for (Part p : inventory) {
            listModel.addElement(p.toString());
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Autopartsinventory());
    }
}