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

    private Scanner inputReader;

    private int productsPurchased;

    private Store currentStore;

    public Customer(String name, double budget) {
        this.name = name;
        this.inputReader = new Scanner(System.in);
        this.shoppingList = new HashSet<>();
        this.shoppingBasket = new LinkedList<>();
        this.budget = budget;
        this.productsPurchased = 0;
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
                int randomIndex = (int)Math.floor(Math.random() * (productNames.size()));
                this.shoppingList.add(productNames.get(randomIndex));
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addProduct() {
        System.out.println(getShoppingList()+"\n");
        System.out.println("Which product would you like to add to the basket?");
        String productName = inputReader.nextLine();
        if (getShoppingList().contains(productName)) {
            this.shoppingBasket.addLast(productName);
            System.out.println("Added " + productName + " to the top of the shopping basket.");
        } else {
            System.out.println(productName +" is not on the shopping list!");
        }
    }

    public void removeProduct() {
        String item = getShoppingBasket().pop();
        System.out.println(item + " was removed from the top of the shopping basket and placed back on the proper shelf.");
    }

    public void checkTopOfBasket() {
        if (getShoppingBasket().isEmpty()){
            System.out.println("The basket is currently empty!");
            return;
        }
        System.out.println("The product at the top of the shopping basket is: " + getShoppingBasket().getLast());
        //temporarily remove top of stack to show 2nd to last item (if there is one)
        String lastItem = getShoppingBasket().pop();
        if (!getShoppingBasket().isEmpty()) {
            System.out.println("The second product from the top of the shopping basket is: " + getShoppingBasket().getLast());
        } else {
            System.out.println("There are no products in the shopping basket underneath " + lastItem);
        }
        //return top item to stack
        getShoppingBasket().addLast(lastItem);
    }

    public void checkout() {
        LinkedList<String> finalBasket = getShoppingBasket();
        if (finalBasket.isEmpty()) {
            System.out.println(this.name + " left the store without putting anything in the shopping basket. Come again!");
            return;
        }
        System.out.println("Now arriving at checkout, buy as many items as you can without going overbudget!");
        String nextProduct = finalBasket.pop();
        buyOrToss(nextProduct);
        
        while (!finalBasket.isEmpty()) {
            String input = "";
            while (!input.toLowerCase().trim().equals("y") && !input.equals("n")) {
                System.out.println("Would you like to continue buying products in the shopping basket? [y/n]");
                input = inputReader.nextLine();
            }
            if (input.equals("n")) {
                break;
            }
            nextProduct = finalBasket.pop();
            buyOrToss(nextProduct);
        }
        finishShopping();
    }

    public void buyOrToss(String product) {
        printCheckoutOptions(product);
        String input = inputReader.nextLine();
        input = validateCheckoutInput(input.toLowerCase().trim());
        switch (input) {
            case "buy":
                buyProduct(product);
                break;
            case "toss":
                System.out.println(product + " was set aside and not purchased.");
                break;
            default:
                break;
        }
    }

    public void buyProduct(String product) {
        //todo: get product's price from this.currentStore, decrement budget, if budget negative game over, else increment products purchased

    }

    public void finishShopping() {
        //todo: 
    }

    public String toString() {
        String output = this.name +"'s starting budget is " + this.budget + "$ to buy as many of these products as possible:";
        for (String productName: this.shoppingList) {
            output += "\n"+ productName;
        }
        return output;
     }

     public void beginShopping(Store store) {
        System.out.println("Welcome to the store, " + getName() );
        System.out.println("Your starting budget is, " + getBudget() + "$");
        System.out.println("Your shopping list for today is: " + String.join(", ", getShoppingList()) + ".");
        String input;
        this.currentStore = store;
        while (true) {
            printOptions();
            input = this.inputReader.nextLine();
            input = validateShoppingInput(input);
            switch(input.toLowerCase().trim()) {
                case "list":
                    System.out.println(getShoppingList());
                    break;
                case "budget":
                    System.out.println(getBudget());
                    break;
                case "basket":
                    checkTopOfBasket();
                case "add":
                    addProduct();
                    break;
                case "remove":
                    removeProduct();
                    break;
                case "checkout":
                    checkout();
                    return;
                default:
                    System.out.println("Error: Unidentified command " + input.toLowerCase().trim());
                    break;
            }
        } 
     }


     public String validateShoppingInput(String input) {
        HashSet<String> validInputs = new HashSet<>();
        validInputs.add("list");
        validInputs.add("budget");
        validInputs.add("basket");
        validInputs.add("add");
        validInputs.add("remove");
        validInputs.add("checkout");
        while (!validInputs.contains(input)) {
            printOptions();
            input = inputReader.nextLine();
        }
        return input;
     }

     public String validateCheckoutInput(String input) {
        while (!input.equals("buy") && !input.equals("toss")) {
            System.out.println("Please enter a valid option (buy or toss)");
            input = inputReader.nextLine();
        }
        return input;
     }
     

     public void printOptions() {
        System.out.println("Please select one of the following options:");
        //check list, check budget, check basket, checkout, add item to cart, remove item from cart
        System.out.println("list : check your shopping list");
        System.out.println("budget : check your total budget");
        System.out.println("basket : check the top two items in your shopping basket");
        System.out.println("add : choose a product to add to the top of your shopping basket");
        System.out.println("remove : remove the product at the top of your shopping basket");
        System.out.println("checkout : finish shopping and head to the checkout");
     }

     public void printCheckoutOptions(String productName) {
        System.out.println(this.name + "took out the " + productName + " from the top off the basket. Please select an option: ");
        System.out.println("buy : purchase the product.");
        System.out.println("toss : leave the product to be put back to the appropriate shelf");
     }

}
