import java.io.*;
import java.util.*;

// ABSTRACT
abstract class User {
    protected String username, password;

    public User(String u, String p) {
        username = u;
        password = p;
    }

    public abstract void menu(Scanner sc);
}

// ADMIN
class Admin extends User {
    public Admin(String u, String p) {
        super(u, p);
    }

    public void menu(Scanner sc) {
        while (true) {
            Main.printCenter("\n--- ADMIN PANEL ---");
            Main.printCenter("1. Add Medicine");
            Main.printCenter("2. View Medicines");
            Main.printCenter("3. Delete Medicine");
            Main.printCenter("4. Update Medicine");
            Main.printCenter("5. Logout");

            int ch = Main.inputInt(sc, "Enter Choice: ");

            switch (ch) {
                case 1: Main.addMedicine(sc); break;
                case 2: Main.viewMedicine(); break;
                case 3: Main.deleteMedicine(sc); break;
                case 4: Main.updateMedicine(sc); break;
                case 5: return;
            }
        }
    }
}

// EMPLOYEE
class Employee extends User {
    public Employee(String u, String p) {
        super(u, p);
    }

    public void menu(Scanner sc) {
        while (true) {
            Main.printCenter("\n--- EMPLOYEE PANEL ---");
            Main.printCenter("1. View Medicines");
            Main.printCenter("2. Sell Medicine");
            Main.printCenter("3. Search Medicine");
            Main.printCenter("4. Low Stock");
            Main.printCenter("5. Logout");

            int ch = Main.inputInt(sc, "Enter Choice: ");

            switch (ch) {
                case 1: Main.viewMedicine(); break;
                case 2: Main.sellMedicine(sc); break;
                case 3: Main.searchMedicine(sc); break;
                case 4: Main.lowStock(); break;
                case 5: return;
            }
        }
    }
}

// MEDICINE
class Medicine {
    private String name;
    private double price;
    private int quantity;

    public Medicine(String n, double p, int q) {
        name = n;
        price = p;
        quantity = q;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int q) { quantity = q; }
    public void setPrice(double p) { price = p; }

    public String toString() {
        return name + "," + price + "," + quantity;
    }

    public static Medicine fromString(String line) {
        String[] d = line.split(",");
        return new Medicine(d[0], Double.parseDouble(d[1]), Integer.parseInt(d[2]));
    }

    public void display() {
        Main.printCenter("Name: " + name + " | Price: " + price + " | Qty: " + quantity);
    }
}

// MAIN
class Main {

    static ArrayList<Medicine> list = new ArrayList<>();
    static final String FILE = "medicine.txt";

    // CENTER PRINT
    static void printCenter(String text) {
        int width = 80;
        int pad = (width - text.length()) / 2;
        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.println(text);
    }

    // CENTER INPUT STRING
    static String inputString(Scanner sc, String prompt) {
        int width = 80;
        int pad = (width - prompt.length()) / 2;
        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.print(prompt);
        return sc.next();
    }

    // CENTER INPUT INT
    static int inputInt(Scanner sc, String prompt) {
        int width = 80;
        int pad = (width - prompt.length()) / 2;
        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.print(prompt);
        return sc.nextInt();
    }

    // CENTER INPUT DOUBLE
    static double inputDouble(Scanner sc, String prompt) {
        int width = 80;
        int pad = (width - prompt.length()) / 2;
        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.print(prompt);
        return sc.nextDouble();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadData();

        Admin admin = new Admin("admin", "1234");
        Employee emp = new Employee("emp", "1111");

        while (true) {
            printCenter("==================================");
            printCenter("   PharmaPoint Management System  ");
            printCenter("==================================");
            printCenter("1. Admin Login");
            printCenter("2. Employee Login");
            printCenter("3. Exit");

            int ch = inputInt(sc, "Enter Choice: ");

            switch (ch) {
                case 1: if (login(sc, admin)) admin.menu(sc); break;
                case 2: if (login(sc, emp)) emp.menu(sc); break;
                case 3: saveData(); System.exit(0);
            }
        }
    }

    static boolean login(Scanner sc, User user) {
        String u = inputString(sc, "Username: ");
        String p = inputString(sc, "Password: ");

        if (u.equals(user.username) && p.equals(user.password)) {
            printCenter("Login Success!");
            return true;
        } else {
            printCenter("Invalid Login!");
            return false;
        }
    }

    static void addMedicine(Scanner sc) {
        String n = inputString(sc, "Name: ");
        double p = inputDouble(sc, "Price: ");
        int q = inputInt(sc, "Quantity: ");

        list.add(new Medicine(n, p, q));
        saveData();
        printCenter("Medicine Added!");
    }

    static void viewMedicine() {
        if (list.isEmpty()) {
            printCenter("No Medicine Available!");
            return;
        }
        for (Medicine m : list) m.display();
    }

    static void deleteMedicine(Scanner sc) {
        String name = inputString(sc, "Enter name: ");

        Iterator<Medicine> it = list.iterator();
        while (it.hasNext()) {
            Medicine m = it.next();
            if (m.getName().equalsIgnoreCase(name)) {
                it.remove();
                saveData();
                printCenter("Deleted!");
                return;
            }
        }
        printCenter("Not found!");
    }

    static void updateMedicine(Scanner sc) {
        String name = inputString(sc, "Enter name: ");

        for (Medicine m : list) {
            if (m.getName().equalsIgnoreCase(name)) {

                double price = inputDouble(sc, "New Price: ");
                int qty = inputInt(sc, "New Quantity: ");

                m.setPrice(price);
                m.setQuantity(qty);

                saveData();
                printCenter("Updated!");
                return;
            }
        }
        printCenter("Not found!");
    }

    static void sellMedicine(Scanner sc) {
        String name = inputString(sc, "Name: ");

        for (Medicine m : list) {
            if (m.getName().equalsIgnoreCase(name)) {

                int q = inputInt(sc, "Quantity: ");

                if (q <= m.getQuantity()) {
                    double total = q * m.getPrice();
                    double vat = total * 0.05;

                    m.setQuantity(m.getQuantity() - q);

                    printCenter("--- BILL ---");
                    printCenter("Total: " + total);
                    printCenter("VAT: " + vat);
                    printCenter("Final: " + (total + vat));

                    saveData();
                    return;
                } else {
                    printCenter("Not enough stock!");
                    return;
                }
            }
        }
        printCenter("Medicine not found!");
    }

    static void searchMedicine(Scanner sc) {
        String name = inputString(sc, "Enter name: ");

        for (Medicine m : list) {
            if (m.getName().equalsIgnoreCase(name)) {
                m.display();
                return;
            }
        }
        printCenter("Not found!");
    }

    static void lowStock() {
        for (Medicine m : list) {
            if (m.getQuantity() < 5) {
                m.display();
            }
        }
    }

    static void saveData() {
        try {
            FileWriter fw = new FileWriter(FILE);
            for (Medicine m : list)
                fw.write(m.toString() + "\n");
            fw.close();
        } catch (Exception e) {
            printCenter("Save Error");
        }
    }

    static void loadData() {
        try {
            File f = new File(FILE);
            if (!f.exists()) return;

            Scanner sc = new Scanner(f);
            while (sc.hasNextLine())
                list.add(Medicine.fromString(sc.nextLine()));
            sc.close();
        } catch (Exception e) {
            printCenter("Load Error");
        }
    }
}