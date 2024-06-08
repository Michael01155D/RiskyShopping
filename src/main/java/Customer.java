import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Customer {

    private String name;

    private double budget;

    private HashSet<String> shoppingList;

    private LinkedList<String> shoppingBasket;

    public Customer(String name, double budget) {
        this.name = name;
        this.shoppingList = new HashSet<>();
        this.shoppingBasket = new LinkedList<>();
        this.budget = budget;
        generateShoppingList();
    }

    //default budget is 150$
    public Customer (String name) {
        //Store's products cost between 12 to 3$, if shopping list is 20 items large
        //avg cost is then 7.5$. so avg total cost 20 * 7.5 = 150$
        this(name, 150);
    }

    public String getName() {
        return this.name;
    }

    public double getBudget() {
        return this.budget;
    }

    public LinkedList<String> getShoppingBasket() {
        return this.shoppingBasket;
    }

    public HashSet<String> getShoppingList() {
        return this.shoppingList;
    }

    private void generateShoppingList() {
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
            while (this.shoppingList.size() < 20) {
                
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
