import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, please enter your shopper's name.");
        String name = scanner.nextLine();   
        Store store = new Store();
        Customer player = new Customer(name, store);
        player.beginShopping();
    }
}
