public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        //store.listProducts();
        Customer bob = new Customer("Bob");
        bob.beginShopping(store);
        bob.addProduct();
    }
}
