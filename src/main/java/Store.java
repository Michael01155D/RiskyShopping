import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Store {

    private HashMap<String, Double> products;

    public Store() {
        this.products = new HashMap<>();
        populateProducts();
    }

    private void populateProducts() {
        Scanner fileScan;
        try {
            //path valid for running in IDE, not command line;
            File productFile = new File("./products.txt");
            fileScan = new Scanner(productFile);
            ArrayList<String> productNames = new ArrayList<>();
            while (fileScan.hasNextLine()) {
                String productName = fileScan.nextLine();
                productNames.add(productName);
            }
            for (String productName: productNames) {
                double price = generateRandomPrice();
                this.products.put(productName, price);
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //random double between 12.00 to 3.00
    private double generateRandomPrice() {
        double price = Math.random() * (12.0 - 3.0) + 3.0;
        return Math.round(price * 100.0) / 100.0;
    }

    public HashMap<String, Double> getProducts() {
        return this.products;
    }

    public void listProducts () {
        for (String productName: this.products.keySet()) {
            System.out.println("Name: " + productName +" | cost: " + this.products.get(productName));
        }
    }
}
