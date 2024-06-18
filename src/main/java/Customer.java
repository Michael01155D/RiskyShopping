import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Customer {

    private String name;

    private double startingBudget;

    private double currentBudget;

    private HashSet<String> shoppingList;

    private LinkedList<String> shoppingBasket;

    private boolean isGameOver;

    private Scanner inputReader;

    private HashSet<String> productsPurchased;

    private Store currentStore;

    //number of products in list must be <= 100 (#lines in products.txt)
    private final int LIST_SIZE = 20;
    public Customer(String name, double budget, Store store) {
        this.name = name;
        this.inputReader = new Scanner(System.in);
        this.shoppingList = new HashSet<>();
        this.shoppingBasket = new LinkedList<>();
        //default budget represented by budget arg == -1. 
        this.startingBudget = budget > 0 ? budget : this.LIST_SIZE * 7.5;
        this.currentBudget = startingBudget;
        this.productsPurchased = new HashSet<>();
        this.currentStore = store;
        generateShoppingList();
    }

    //default budget is avg cost of item (7.5$) times list size
    public Customer (String name, Store store) {
        //Store's products cost between 12 to 3$, avg: 7.5$
        this(name, -1, store); //pass -1 to default budget to list size * 7.5$
    }

    public String getName() {
        return this.name;
    }

    public Store getCurrentStore() {
        return currentStore;
    }

    public double getStartingBudget() {
        return this.startingBudget;
    }

    public double getCurrentBudget() {
        return this.currentBudget;
    }

    public void setCurrentBudget(double newBudget) {
        this.currentBudget = newBudget;
    }

    public HashSet<String> getProductsPurchased() {
        return productsPurchased;
    }

    public void addProductPurchased(String productPurchased) {
        this.productsPurchased.add(productPurchased);
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
            while (this.shoppingList.size() < LIST_SIZE) {
                int randomIndex = (int)Math.floor(Math.random() * (productNames.size()));
                this.shoppingList.add(productNames.get(randomIndex));
            }
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addProduct() {
        printShoppingList();
        System.out.println("\nWhich product would you like to add to the basket?");
        //todo, make it case insensitive
        String productName = formatProductNameInput(inputReader.nextLine());
        if (getShoppingList().contains(productName)) {
            this.shoppingBasket.addLast(productName);
            System.out.println("\nAdded " + productName + " to the top of the shopping basket.");
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
        String lastItem = getShoppingBasket().removeLast();
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
        String nextProduct = finalBasket.removeLast();
        buyOrToss(nextProduct);
        
        while (!finalBasket.isEmpty() && !isGameOver) {
            String input = "";
            while (!input.toLowerCase().trim().equals("y") && !input.equals("n")) {
                System.out.println("Would you like to continue buying products in the shopping basket? [y/n]");
                input = inputReader.nextLine();
            }
            if (input.equals("n")) {
                break;
            }
            nextProduct = finalBasket.removeLast();
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

    public void GameOver() {
        this.isGameOver = true;
    }
    public void buyProduct(String product) {
        //todo: get product's price from this.currentStore, decrement budget, if budget negative game over, else add to products purchased
        double price = currentStore.getProducts().get(product);
        System.out.println("the price of " + product + " is : " + price+"$");
        double budget = getCurrentBudget();
        if (price > budget) {
            System.out.println("Uh oh! " + getName() + " went overbudget and left the store. Game over.");
            GameOver();
            return;
        }
        System.out.println(product + " was paid for successfully.");
        setCurrentBudget(budget - price);
        addProductPurchased(product);
    }

    public void finishShopping() {
        int purchased = getProductsPurchased().size();
        int shoppingListSize = getShoppingList().size();
        double currentBudget = getCurrentBudget();
        double startingBudget = getStartingBudget();
        System.out.println(getName() + " finished shopping, let's see the results of today's shopping trip:");
        System.out.println(getName() + " bought " + purchased + " out of " + shoppingListSize + " total.");
        double percent = (purchased * 1.0 / shoppingListSize) * 100.00;
        String evaluation = percent >= 75.00 ? "Well done!" : "Not bad, try to aim for 75% next time!";
        System.out.printf("%.2f%% of products from the shopping list were successfully purchased. %s\n", percent, evaluation);
        System.out.println("Bob ended up spending " + (startingBudget - currentBudget) + "$ of the " +startingBudget+"$ starting budget.");
    }

    public String toString() {
        String output = this.name +"'s starting budget is " + getStartingBudget() + "$ to buy as many of these products as possible:";
        for (String productName: this.shoppingList) {
            output += "\n"+ productName;
        }
        return output;
     }

     public void beginShopping() {
        System.out.println("\n\nWelcome to the store, " + getName() );
        printRules();
        printShoppingList();
        printBudget();
        String input;
        while (true) {
            System.out.println("\nPlease enter a valid command, enter 'options' to view all commands");
            input = this.inputReader.nextLine();
            input = validateShoppingInput(input);
            switch(input.toLowerCase().trim()) {
                case "options":
                    printOptions();
                    break;
                case "'options'":
                    printOptions();
                    break;
                case "list":
                    printShoppingList();
                    break;
                case "budget":
                    printBudget();
                    break;
                case "prices":
                    printPrices();
                    break;
                case "basket":
                    checkTopOfBasket();
                    break;
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

     public void printRules() {
        System.out.println("\n     ***RULES***:\n\nThe goal is to successfully buy as many unique items as possible from the shopping list provided.\nOnce you decide to go to the checkout, the items will be removed from"+
        " the top of shopping basket one at a time (the last item added is the first one taken out).\nEach time an item is removed, you will be given the option to buy it or not, and then to decide whether to take out the next item or finish" +
        " shopping.\nBe aware that spending more money than the initial budget will cause a game over.\nChoose the order in which you add items to the shopping basket wisely to get as many items from the list as possible while staying within the budget!");
        System.out.println("\nOnce you finish reading these rules, please press the Enter key to begin shopping!");
        String hitEnter = this.inputReader.nextLine();
     }

     public void printShoppingList() {
        int lineBreakCounter = 3;
        String outputLine = "";
        System.out.println("\nYour shopping list is:");
        for (String item: getShoppingList()) {
            if (lineBreakCounter == 3 || lineBreakCounter == 2) {
                outputLine += item +", ";
                lineBreakCounter--;
            } else if (lineBreakCounter == 1) {
                outputLine += item + ",";
                System.out.println(outputLine);
                outputLine = "";
                lineBreakCounter = 3;
            }
        }
        if (!outputLine.isEmpty()) {System.out.println(outputLine.substring(0, outputLine.trim().length() -1) + ".");}
        System.out.println("");
     }

     public void printPrices() {
        getCurrentStore().listProducts();
     }

     public void printBudget() {
        System.out.printf("Your starting budget is: %.2f$\n", getCurrentBudget());
     }

     public String validateShoppingInput(String input) {
        HashSet<String> validInputs = new HashSet<>();
        validInputs.add("list");
        validInputs.add("budget");
        validInputs.add("prices");
        validInputs.add("basket");
        validInputs.add("add");
        validInputs.add("remove");
        validInputs.add("checkout");
        while (!validInputs.contains(input.toLowerCase().trim())) {
            printOptions();
            input = inputReader.nextLine().toLowerCase().trim();
        }
        return input;
     }

     public String validateCheckoutInput(String input) {
        while (!input.equals("buy") && !input.equals("toss")) {
            System.out.println("Please enter a valid option (buy or toss)");
            input = inputReader.nextLine().toLowerCase().trim();
        }
        return input;
     }
     

     public void printOptions() {
        System.out.println("\n\n**Valid Commands:**");
        //check list, check budget, check basket, checkout, add item to cart, remove item from cart
        System.out.println("list : check your shopping list");
        System.out.println("budget : check today's budget");
        System.out.println("prices : view the price of each product on your list");
        System.out.println("basket : check the top two items in your shopping basket");
        System.out.println("add : choose a product to add to the top of your shopping basket");
        System.out.println("remove : remove the product at the top of your shopping basket");
        System.out.println("checkout : finish shopping and head to the checkout");
        System.out.println("options: displays this list of options\n");
     }

     public void printCheckoutOptions(String productName) {
        System.out.println(this.name + " took out the " + productName + " from the top off the basket. Please enter what to do with the product: ");
        System.out.println("buy : purchase the product.");
        System.out.println("toss : leave the product to be put back to the appropriate shelf");
     }

     public String formatProductNameInput(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).trim();
     }

}
