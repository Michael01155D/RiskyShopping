public class Main {
    public static void main(String[] args) {

        double percent = (7635.52 * 1.0/ 20) * 100.00;
        String evaluation = percent >= 75.00 ? "Well done!" : "Not bad, try to aim for 75% next time!";
        System.out.printf("%.2f%% of products from the shopping list were successfully purchased. %s ", percent, evaluation);
        // Store store = new Store();
        // //store.listProducts();
        // Customer bob = new Customer("Bob", store);
        // bob.beginShopping();
    }
}
