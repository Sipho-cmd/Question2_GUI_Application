import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProductAppGUI extends JFrame {
    private  JTextField txtName, txtPrice;
    private JComboBox<String> cmbProduct;

    public ProductAppGUI() {
        setTitle("Product App");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Select Product:"));
        cmbProduct = new JComboBox<>(new String[]{"Cozy Nights Monarch", "Restonic Barbaodos", "Sealy Phuket"});
        add(cmbProduct);

        add(new JLabel("Enter Price:"));
        txtPrice = new JTextField();
        add(txtPrice);

        JButton btnProcess = new JButton("PROCESS");
        add(new JLabel("")); // Spacer
        add(btnProcess);

        btnProcess.addActionListener(e -> processSale());
        createMenu();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools"); // Paper says "Tools"
        JMenuItem processItem = new JMenuItem("Process");
        JMenuItem clearItem = new JMenuItem("Clear");

        processItem.addActionListener(e -> processSale());
        clearItem.addActionListener(e -> {
            txtName.setText("");
            txtPrice.setText("");
            cmbProduct.setSelectedIndex(0);
        });

        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    private void processSale() {
        String name = txtName.getText().trim();
        String product = (String) cmbProduct.getSelectedItem();

        try {
            double price = Double.parseDouble(txtPrice.getText().trim());
            ProductCalculations calc = new ProductCalculations(price);

            // Calculate values
            double vat = calc.GetVat();
            double commission = calc.GetCommission();
            double total = calc.GetTotal();

            // 1. Show Popup
            String message = String.format("Customer Name: %s\nProduct: %s\nVAT: R %.1f\nCOMMISSION: R %.1f\nTOTAL: R %.1f",
                    name, product, vat, commission, total);
            JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);

            // 2. Save to File
            saveToFile(name, product, vat, commission, total);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the price.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile(String name, String product, double vat, double comm, double total) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true))) {
            writer.write("DATA LOG\n");
            writer.write("**********************************\n");
            writer.write("Customer Name: " + name + "\n");
            writer.write("Product: " + product + "\n");
            writer.write(String.format("VAT: R %.1f\n", vat));
            writer.write(String.format("COMMISSION: R %.1f\n", comm));
            writer.write(String.format("TOTAL: R %.1f\n", total));
            writer.write("**********************************\n\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductAppGUI().setVisible(true));
    }


}
