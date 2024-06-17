public class Main {
    public static void main(String[] args) {

         Store store = new Store();
         Customer bob = new Customer("Bob", store);
         bob.beginShopping();
    }
}
