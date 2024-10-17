import java.util.*;

class FashionShop {
    static int[] T_SHIRT_PRICES = {600, 800, 900, 1000, 1100, 1200};
    static String[] SIZES = {"XS", "S", "M", "L", "XL", "XXL"};

    static int PROCESSING = 0;
    static int DELIVERING = 1;
    static int DELIVERED = 2;
    static String[] ORDER_STATUSES = {"PROCESSING", "DELIVERING", "DELIVERED"};

    static String[] orderIDs = new String[100];
    static String[] customerContacts = new String[100];
    static String[] tShirtSizes = new String[100];
    static int[] quantities = new int[100];
    static double[] orderAmounts = new double[100];
    static int[] orderStatuses = new int[100];

    static int orderCount = 0;

    static Map<String, Double> customerTotalAmounts = new HashMap<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) {
        while (true) {
            clearConsole();
            showMainMenu();
        }
    }

    public static void showMainMenu() {
        System.out.println(
                " /$$$$$$$$                 /$$       /$$                            /$$$$$$  /$$    \n" +
                "| $$_____/                | $$      |__/                           /$$__  $$| $$    \n" +
                "| $$    /$$$$$$   /$$$$$$$| $$$$$$$  /$$  /$$$$$$  /$$$$$$$       | $$  \\__/| $$$$$$$   /$$$$$$   /$$$$$$ \n" +
                "| $$$$$|____  $$ /$$_____/| $$__  $$| $$ /$$__  $$| $$__  $$      |  $$$$$$ | $$__  $$ /$$__  $$ /$$__  $$ \n" +
                "| $$__/ /$$$$$$$|  $$$$$$ | $$  \\ $$| $$| $$  \\ $$| $$  \\ $$       \\____  $$| $$  \\ $$| $$  \\ $$| $$  \\ $$ \n" +
                "| $$   /$$__  $$ \\____  $$| $$  | $$| $$| $$  | $$| $$  | $$       /$$  \\ $$| $$  | $$| $$  | $$| $$  | $$  \n" +
                "| $$  |  $$$$$$$ /$$$$$$$/| $$  | $$| $$|  $$$$$$/| $$  | $$      |  $$$$$$/| $$  | $$|  $$$$$$/| $$$$$$$/  \n" +
                "|__/   \\_______/|_______/ |__/  |__/|__/ \\______/ |__/  |__/       \\______/ |__/  |__/ \\______/ | $$____/   \n" +
                "                                                                                                | $$       \n" +
                "                                                                                                | $$       \n" +
                "                                                                                                |__/      \n");
        System.out.println("________________________________________________________________________________________________________\n");
        System.out.println("\n\t[1]Place Order \t\t\t\t\t   [2]Search Customer\n");
        System.out.println("\t[3]Search Order \t\t\t\t   [4]View Reports\n");
        System.out.println("\t[5]Set Order Status \t\t\t\t   [6]Delete Order\n");
        System.out.print("\n\tInput Option : ");
        int option = sc.nextInt();
        sc.nextLine();
        switch (option) {
            case 1:
                placeOrder();
                break;
            case 2:
                searchCustomer();
                break;
            case 3:
                searchOrder();
                break;
            case 4:
                viewReports();
                break;
            case 5:
                setOrderStatus();
                break;
            case 6:
                deleteOrder();
                break;
            default:
                System.out.println("Invalid Option! Please try again.");
        }
    }

    public static void placeOrder() {
        clearConsole();
        System.out.println("  _____  _                   ____          _           ");
        System.out.println(" |  __ \\| |                 / __ \\        | |          ");
        System.out.println(" | |__) | | __ _  ___ ___  | |  | |_ __ __| | ___ _ __ ");
        System.out.println(" |  ___/| |/ _` |/ __/ _ \\ | |  | | '__/ _` |/ _ \\ '__|");
        System.out.println(" | |    | | (_| | (_|  __/ | |__| | | | (_| |  __/ |   ");
        System.out.println(" |_|    |_|\\__,_|\\ ___\\___|  \\____/|_|  \\__,_|\\___|_|   ");
        System.out.println("                                                     ");
        System.out.println("_______________________________________________________________\n");
        String orderID = generateOrderID();

        String contactNumber;
        while (true) {
            System.out.println("Enter Order ID: " + orderID);
            System.out.println("\nEnter Customer Phone Number: ");
            contactNumber = sc.nextLine();
            if (contactNumber.length() == 10 && contactNumber.startsWith("0")) {
                break;
            } else {
                System.out.println("Invalid Contact Number. It should start with '0' and be 10 digits long.");
            }
        }

        String tShirtSize = null;
        while (true) {
            System.out.println("\nEnter T-Shirt Size (XS/S/M/L/XL/XXL): ");
            tShirtSize = sc.nextLine();
            if (isValidSize(tShirtSize)) {
                break;
            } else {
                System.out.println("\nInvalid T-Shirt Size! Please enter a valid size.");
            }
        }

        int quantity;
        while (true) {
            System.out.println("\nEnter QTY: ");
            quantity = sc.nextInt();
            if (quantity > 0) {
                break;
            } else {
                System.out.println("\nQuantity must be greater than 0.");
            }
        }

        int sizeIndex = getSizeIndex(tShirtSize);
        double amount = quantity * T_SHIRT_PRICES[sizeIndex];
        System.out.println("\nAmount: " + amount + " LKR");

        System.out.println("\nDo you want to place this order? (Y/N): ");
        sc.nextLine();
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            saveOrder(orderID, contactNumber, tShirtSize, quantity, amount, PROCESSING);
            System.out.println("\nOrder placed successfully!");
        } else {
            System.out.println("\nOrder not placed.");
        }

        System.out.print("\nDo you want to place another order? (Y/N): ");
        String anotherOrder = sc.nextLine();
        if (anotherOrder.equalsIgnoreCase("Y")) {
            clearConsole();
            placeOrder();
        } else {
            clearConsole();
            showMainMenu();
        }
    }

    public static String generateOrderID() {
        return "ODR#" + String.format("%05d", orderCount + 1);
    }

    public static void saveOrder(String orderID, String contactNumber, String tShirtSize, int quantity, double amount, int status) {
        orderIDs[orderCount] = orderID;
        customerContacts[orderCount] = contactNumber;
        tShirtSizes[orderCount] = tShirtSize;
        quantities[orderCount] = quantity;
        orderAmounts[orderCount] = amount;
        orderStatuses[orderCount] = status;
        orderCount++;
    }

    public static boolean isValidSize(String size) {
        for (String s : SIZES) {
            if (s.equals(size)) {
                return true;
            }
        }
        return false;
    }

    public static int getSizeIndex(String size) {
        for (int i = 0; i < SIZES.length; i++) {
            if (SIZES[i].equals(size)) {
                return i;
            }
        }
        return -1;
    }

    public static void searchCustomer() {
        clearConsole();
        System.out.println("   _____                     _        _____          _                            ");
        System.out.println("  / ____|                   | |      / ____|        | |                           ");
        System.out.println(" | (___   ___  __ _ _ __ ___| |__   | |    _   _ ___| |_ ___  _ __ ___   ___ _ __ ");
        System.out.println("  \\___ \\ / _ \\/ _` | '__/ __| '_ \\  | |   | | | / __| __/ _ \\| '_ ` _ \\ / _ \\ '__|");
        System.out.println("  ____) |  __/ (_| | | | (__| | | | | |___| |_| \\__ \\ || (_) | | | | | |  __/ |   ");
        System.out.println(" |_____/ \\___|\\__,_|_|  \\___|_| |_|  \\_____\\__,_|___/\\__\\___/|_| |_| |_|\\___|_|   ");
        System.out.println("                                                                                 ");
        System.out.println("___________________________________________________________________________________________\n");
        System.out.println("\nEnter Customer Contact Number: ");
        String contactNumber = sc.nextLine();
        boolean found = false;

        String tableHeader = String.format("| %-5s | %-3s | %-8s |", "Size", "QTY", "Amount");
        System.out.println("+-------+-----+----------+");
        System.out.println(tableHeader);
        System.out.println("+ -------+-----+----------+");

        double totalAmount = 0.0;
        for (int i = 0; i < orderCount; i++) {
            if (customerContacts[i].equals(contactNumber)) {
                found = true;
                totalAmount += orderAmounts[i];
                String row = String.format("| %-5s | %-3d | %8.2f |", tShirtSizes[i], quantities[i], orderAmounts[i]);
                System.out.println(row);
            }
        }
        System.out.println("+-------+-----+----------+");

        if (found) {
            System.out.printf("| %-5s   %-3s | %8.2f |\n", "Total", "", totalAmount);
            System.out.println("+-------------+----------+");
        } else {
            System.out.println("No orders found for this contact number.");
        }

        System.out.println("\nDo you want to search another customer report? (Y/N): ");
        String anotherCustomer = sc.nextLine();
        if (anotherCustomer.equalsIgnoreCase("Y")) {
            clearConsole();
            searchCustomer();
        } else {
            clearConsole();
            showMainMenu();
        }
    }

    public static void searchOrder() {
        clearConsole();
        System.out.println("   _____                     _        ____          _           ");
        System.out.println("  / ____|                   | |      / __ \\        | |          ");
        System.out.println(" | (___   ___  __ _ _ __ ___| |__   | |  | |_ __ __| | ___ _ __ ");
        System.out.println("  \\___ \\ / _ \\/ _` | '__/ __| '_ \\  | |  | | '__/ _` |/ _ \\ '__|");
        System.out.println("  ____) |  __/ (_| | | | (__| | | | | |__| | | | (_| |  __/ |   ");
        System.out.println(" |_____/ \\___|\\__,_|_|  \\___|_| |_|  \\____/|_|  \\__,_|\\___|_|   ");
        System.out.println("                                                               ");
        System.out.println("_______________________________________________________________________\n");
        System.out.println("Enter Order ID: ");
        String orderID = sc.nextLine();
        boolean found = false;

        for (int i = 0; i < orderCount; i++) {
            if (orderIDs[i].equals(orderID)) {
                found = true;
                System.out.println("\n Contact: " + customerContacts[i] + ",\n Size: " + tShirtSizes[i] +
                        ",\n QTY: " + quantities[i] + ",\n Amount: " + orderAmounts[i] + ",\n Status: " + ORDER_STATUSES[orderStatuses[i]]);
            }
        }

        if (!found) {
            System.out.println("Invalid ID");
        }

        System.out.println("\nDo you want to search another order? (Y/N): ");
        String anotherOrder = sc.nextLine();
        if (anotherOrder.equalsIgnoreCase("Y")) {
            clearConsole();
            searchOrder();
        } else {
            clearConsole();
            showMainMenu();
        }
    }

    public static void viewReports() {
        clearConsole();
        System.out.println("  _____                       _       ");
        System.out.println(" |  __ \\                     | |      ");
        System.out.println(" | |__) |___ _ __   ___  _ __| |_ ___ ");
        System.out.println(" |  _  // _ \\ '_ \\ / _ \\| '__| __/ __|");
        System.out.println(" | | \\ \\  __/ |_) | (_) | |  | |_\\__ \\");
        System.out.println(" |_|  \\_\\___| .__/ \\___/|_|   \\__|___/");
        System.out.println("            | |                      ");
        System.out.println("            |_|                      ");
        System.out.println("___________________________________________");
        System.out.println();
        System.out.println("[1] Customer Reports");
        System.out.println("[2] Item Reports");
        System.out.println("[3] Order Reports");
        System.out.println();
        System.out.print("Enter option: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                customerReports();
                break;
            case 2:
                itemReports();
                break;
            case 3:
                orderReports();
                break;
            default:
                System.out.println("Invalid option! Please select a valid report option.");
                break;
        }
    }

    public static void customerReports() {
        clearConsole();
        System.out.println("   _____          _                              _____                       _       ");
        System.out.println("  / ____|        | |                            |  __ \\                     | |      ");
        System.out.println(" | |    _   _ ___| |_ ___  _ __ ___   ___ _ __  | |__) |___ _ __   ___  _ __| |_ ___ ");
        System.out.println(" | |   | | | / __| __/ _ \\| '_ ` _ \\ / _ \\ '__| |  _  // _ \\ '_ \\ / _ \\| '__| __/ __|");
        System.out.println(" | |___| |_| \\__ \\ || (_) | | | | | |  __/ |    | | \\ \\  __/ |_) | (_) | |  | |_\\__ \\ ");
        System.out.println("  \\_____\\__,_|___/\\__\\___/|_| |_| |_|\\___|_|    |_|  \\_\\___| .__/ \\___/|_|   \\__|___/ ");
        System.out.println("                                                           | |                       ");
        System.out.println("                                                           |_|                       ");
        System.out.println("________________________________________________________________________________________________");
        System.out.println();
        System.out.println("[1] Best in Customers");
        System.out.println("[2] View Customers");
        System.out.println("[3] All Customer Report");
        System.out.println();
        System.out.print("Enter option: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                bestInCustomers();
                break;
            case 2:
                viewCustomers();
                break;
            case 3:
                allCustomerReports();
                break;
            default:
                System.out.println("Invalid option! Please select a valid report option.");
                break;
        }
    }

    public static void bestInCustomers() {
        clearConsole();
        System.out.println("  ____            _     _____          _____          _                                 ");
        System.out.println(" |  _ \\          | |   |_   _|        / ____|        | |                                ");
        System.out.println(" | |_) | ___  ___| |_    | |  _ __   | |    _   _ ___| |_ ___  _ __ ___   ___ _ __ ___ ");
        System.out.println(" |  _ < / _ \\/ __| __|   | | | '_ \\  | |   | | | / __| __/ _ \\| '_ ` _ \\ / _ \\ '__/ __|");
        System.out.println(" | |_) |  __/\\__ \\ |_   _| |_| | | | | |___| |_| \\__ \\ || (_) | | | | | |  __/ |  \\__ \\ ");
        System.out.println(" |____/ \\___||___/\\__| |_____|_| |_|  \\_____\\__,_|___/\\__\\___/|_| |_| |_|\\___|_|  |___/ ");
        System.out.println("________________________________________________________________________________________________");
        System.out.println();

        List<Map.Entry<String, Double>> sortedCustomers = new ArrayList<>(customerTotalAmounts.entrySet());
        sortedCustomers.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        System.out.println("Customer ID | All QTY | Total Amount");
        System.out.println("______________________________________________________________");
        for (Map.Entry<String, Double> entry : sortedCustomers) {
            System.out.printf("| %-15s | %-3d | %-12.2f |\n", entry.getKey(), entry.getValue(), entry.getValue());
        }
    }

    public static void viewCustomers() {
        clearConsole();
        System.out.println(" __      ___                  _____          _                                 ");
        System.out.println(" \\ \\    / (_)                / ____|        | |                                ");
        System.out.println("  \\ \\  / / _  _____      __ | |    _   _ ___| |_ ___  _ __ ___   ___ _ __ ___ ");
        System.out.println("   \\ \\/ / | |/ _ \\ \\ /\\ / / | |   | | | / __| __/ _ \\| '_ ` _ \\ / _ \\ '__/ __|");
        System.out.println("    \\  /  | |  __/\\ V  V /  | |___| |_| \\__ \\ || (_) | | | | | |  __/ |  \\__ \\");
        System.out.println("     \\/   |_|\\___| \\_/\\_/    \\_____\\__,_|___/\\__\\___/|_| |_| |_|\\___|_|  |___/");
        System.out.println("                                                                               ");
        System.out.println("______________________________________________________________________________________\n");

        System.out.println("Customer ID | All QTY | Total Amount");
        System.out.println("______________________________________________________________");
        for (Map.Entry<String, Double> entry : customerTotalAmounts.entrySet()) {
            System.out.printf("| %-15s | %-3d | %-12.2f |\n", entry.getKey(), entry.getValue(), entry.getValue());
        }
    }

    public static void allCustomerReports() {
        clearConsole();
        System.out.println("           _ _    _____          _                              _____                       _   ");
        System.out .println("     /\\   | | |  / ____|        | |                            |  __ \\                     | |  ");
        System.out.println("    /  \\  | | | | |    _   _ ___| |_ ___  _ __ ___   ___ _ __  | |__) |___ _ __   ___  _ __| |_ ");
        System.out.println("   / /\\ \\ | | | | |   | | | / __| __/ _ \\| '_ ` _ \\ / _ \\ '__| |  _  // _ \\ '_ \\ / _ \\| '__| __|");
        System.out.println("  / ____ \\| | | | |___| |_| \\__ \\ || (_) | | | | | |  __/ |    | | \\ \\  __/ |_) | (_) | |  | |_ ");
        System.out.println(" /_/    \\_\\_|_|_|\\_____\\__,_|___/\\__\\___/|_| |_| |_|\\___|_|    |_|  \\_\\___| .__/ \\___/|_|   \\__|");
        System.out.println("                                                                          | |                   ");
        System.out.println("                                                                          |_|                   ");
        System.out.println("______________________________________________________________________________________________________________\n");

        System.out.println("Phone Number | Size | Total");
        System.out.println("______________________________________________________________");
        for (int i = 0; i < orderCount; i++) {
            System.out.printf("| %-15s | %-4s | %-8.2f |\n", customerContacts[i], tShirtSizes[i], orderAmounts[i]);
        }
    }

    public static void itemReports() {
        clearConsole();
        System.out.println("  ___ _                   ____                       _       ");
        System.out.println(" |_ _| |_ ___ _ __ ___   |  _ \\ ___ _ __   ___  _ __| |_ ___ ");
        System.out.println("  | || __/ _ \\ '_ ` _ \\  | |_) / _ \\ '_ \\ / _ \\| '__| __/ __|");
        System.out.println("  | || ||  __/ | | | | | |  _ <  __/ |_) | (_) | |  | |_\\__ \\");
        System.out.println(" |___|\\__\\___|_| |_| |_| |_| \\_\\___| .__/ \\___/|_|   \\__|___/ ");
        System.out.println("                                   |_|                            ");
        System.out.println("______________________________________________________________________");
        System.out.println();
        System.out.println("[1] Best Selling Categories Sorted by QTY");
        System.out.println("[2] Best Selling Categories Sorted by Amount");
        System.out.println();
        System.out.print("Enter option: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                bestSellingCategoriesSortedByQty();
                break;
            case 2:
                bestSellingCategoriesSortedByAmount();
                break;
            default:
                System.out.println("Invalid option! Please select a valid report option.");
                break;
        }
    }

    public static void bestSellingCategoriesSortedByQty() {
        clearConsole();
        System.out.println("   _____            _           _   ____           ____ _________     __");
        System.out.println("  / ____|          | |         | | |  _ \\         / __ \\__   __\\ \\   / /");
        System.out.println(" | (___   ___  _ __| |_ ___  __| | | |_) |_   _  | |  | | | |   \\ \\_/ / ");
        System.out.println("  \\___ \\ / _ \\| '__| __/ _ \\/ _` | |  _ <| | | | | |  | | | |    \\   /  ");
        System.out.println("  ____) | (_) | |  | ||  __/ (_| | | |_) | |_| | | |__| | | |     | |   ");
        System.out.println(" |_____/ \\___/|_|   \\__\\___|\\__,_| |____/ \\__, |  \\___\\_\\ |_|     |_|   ");
        System.out.println("                                           __/ |                        ");
        System.out.println("                                          |___/                         ");
        System.out.println("_______________________________________________________________________________\n");

        Map<String, Integer> qtyMap = new HashMap<>();
        Map<String, Double> amountMap = new HashMap<>();

        for (int i = 0; i < orderCount; i++) {
            String size = tShirtSizes[i];
            qtyMap.put(size, qtyMap.getOrDefault(size, 0) + quantities[i]);
            amountMap.put(size, amountMap.getOrDefault(size, 0.0) + orderAmounts[i]);
        }

        List<Map.Entry<String, Integer>> sortedSizes = new ArrayList<>(qtyMap.entrySet());
        sortedSizes.sort(( e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        System.out.println("Size | QTY | Total Amount");
        System.out.println("______________________________________________________________");
        for (Map.Entry<String, Integer> entry : sortedSizes) {
            System.out.printf("| %-4s | %-9d | %-12.2f |\n", entry.getKey(), entry.getValue(), amountMap.get(entry.getKey()));
        }
    }

    public static void bestSellingCategoriesSortedByAmount() {
        clearConsole();
        System.out.println("   _____            _           _   ____                                               _   ");
        System.out.println("  / ____|          | |         | | |  _ \\            /\\                               | |  ");
        System.out.println(" | (___   ___  _ __| |_ ___  __| | | |_) |_   _     /  \\   _ __ ___   ___  _   _ _ __ | |_ ");
        System.out.println("  \\___ \\ / _ \\| '__| __/ _ \\/ _` | |  _ <| | | |   / /\\ \\ | '_ ` _ \\ / _ \\| | | | '_ \\| __|");
        System.out.println("  ____) | (_) | |  | ||  __/ (_| | | |_) | |_| |  / ____ \\| | | | | | (_) | |_| | | | | |_ ");
        System.out.println(" |_____/ \\___/|_|   \\__\\___|\\__,_| |____/ \\__, | /_/    \\_\\_| |_| |_|\\___/ \\__,_|_| |_|\\__|");
        System.out.println("                                           __/ |                                           ");
        System.out.println("                                          |___/                                            ");
        System.out.println("_____________________________________________________________________________________________________\n");

        Map<String, Double> amountMap = new HashMap<>();
        for (int i = 0; i < orderCount; i++) {
            String size = tShirtSizes[i];
            amountMap.put(size, amountMap.getOrDefault(size, 0.0) + orderAmounts[i]);
        }

        List<Map.Entry<String, Double>> sortedAmounts = new ArrayList<>(amountMap.entrySet());
        sortedAmounts.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        System.out.println("Size | QTY | Total Amount");
        System.out.println("______________________________________________________________");
        for (Map.Entry<String, Double> entry : sortedAmounts) {
            int qty = 0;
            for (int i = 0; i < orderCount; i++) {
                if (tShirtSizes[i].equals(entry.getKey())) {
                    qty += quantities[i];
                }
            }
            System.out.printf("| %-4s | %-9d | %-12.2f |\n", entry.getKey(), qty, entry.getValue());
        }
    }

    public static void orderReports() {
        clearConsole();
        System.out.println("   ____          _             _____                       _       ");
        System.out.println("  / __ \\        | |           |  __ \\                     | |      ");
        System.out.println(" | |  | |_ __ __| | ___ _ __  | |__) |___ _ __   ___  _ __| |_ ___ ");
        System.out.println(" | |  | | '__/ _` |/ _ \\ '__| |  _  // _ \\ '_ \\ / _ \\| '__| __/ __|");
        System.out.println(" | |__| | | | (_| |  __/ |    | | \\ \\  __/ |_) | (_) | |  | |_\\__ \\");
        System.out.println("  \\____/|_|  \\__,_|\\___|_|    |_|  \\_\\___| .__/ \\___/|_|   \\__|___/");
        System.out.println("                                         | |                       ");
        System.out.println("                                         |_|                       ");
        System.out.println("______________________________________________________________________");
        System.out.println();
        System.out.println("[1] All Orders");
        System.out.println("[2] Orders By Amount");
        System.out.println();
        System.out.print("Enter option: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                allOrders();
                break;
            case 2:
                ordersByAmount();
                break;
            default:
                System.out.println("Invalid option! Please select a valid report option.");
                break;
        }
    }

    public static void allOrders() {
        System.out.println(" __      ___                  ____          _               ");
        System.out.println(" \\ \\    / (_)                / __ \\        | |              ");
        System.out.println("  \\ \\  / / _  _____      __ | |  | |_ __ __| | ___ _ __ ___ ");
        System.out.println("   \\ \\/ / | |/ _ \\ \\ /\\ / / | |  | | '__/ _` |/ _ \\ '__/ __|");
        System.out.println("    \\  /  | |  __/\\ V  V /  | |__| | | | (_| |  __/ |  \\__ \\");
        System.out.println("     \\/   |_|\\___| \\_/\\_/    \\____/|_|  \\__,_|\\___|_|  |___/");
        System.out.println("                                                           ");
        System.out.println("                                                           ");
        System.out.println("____________________________________________________________________\n");

        System.out.println("Order ID | Customer ID | Size | QTY | Amount | Status");
        System.out.println("______________________________________________________________");
        for (int i = 0; i < orderCount; i++) {
            System.out.printf("| %-8s | %-15s | %-4s | %-3d | %-8.2f | %-8s |\n",
                    orderIDs[i], customerContacts[i], tShirtSizes[i], quantities[i], orderAmounts[i], ORDER_STATUSES[orderStatuses[i]]);
        }
    }

    public static void ordersByAmount() {
        clearConsole();
        System.out.println("    ____          _                 ____                                               _   ");
        System.out.println("   / __ \\        | |               |  _ \\            /\\                               | |  ");
        System.out.println("  | |  | |_ __ __| | ___ _ __ ___  | |_) |_   _     /  \\   _ __ ___   ___  _   _ _ __ | |_ ");
        System.out.println("  | |  | | '__/ _` |/ _ \\ '__/ __| |  _ <| | | |   / /\\ \\ | '_ ` _ \\ / _ \\| | | | '_ \\| __|");
        System.out.println("  | |__| | | | (_| |  __/ |  \\__ \\ | |_) | |_| |  / ____ \\| | | | | | (_) | |_| | | | | |_ ");
        System.out.println("   \\____/|_|  \\__,_|\\___|_|  |___/ |____/ \\__, | /_/    \\_\\_| |_| |_|\\___/ \\__,_|_| |_|\\__|");
        System.out.println("                                           __/ |                                           ");
        System.out.println("                                          |___/                                            ");
        System.out.println("_____________________________________________________________________________________________________\n");

        List<Integer> orderIndices = new ArrayList<>();
        for (int i = 0; i < orderCount; i++) {
            orderIndices.add(i);
        }
        orderIndices.sort((i1, i2) -> Double.compare(orderAmounts[i2], orderAmounts[i1]));

        System.out.println("Order ID | Customer ID | Size | QTY | Amount | Status");
        System.out.println("______________________________________________________________");
        for (int index : orderIndices) {
            System.out.printf("| %-8s | %-15s | %-4s | %-3d | %-8.2f | %-8s |\n",
                    orderIDs[index], customerContacts[index], tShirtSizes[index], quantities[index], orderAmounts[index], ORDER_STATUSES[orderStatuses[index]]);
        }
    }

    public static void setOrderStatus() {
        clearConsole();
        System.out.println("   ____          _              _____ _        _             ");
        System.out.println("  / __ \\        | |            / ____| |      | |            ");
        System.out.println(" | |  | |_ __ __| | ___ _ __  | (___ | |_ __ _| |_ _   _ ___ ");
        System.out.println(" | |  | | '__/ _` |/ _ \\ '__|  \\___ \\| __/ _` | __| | | / __|");
        System.out.println(" | |__| | | | (_| |  __/ |     ____) | || (_| | |_| |_| \\__ \\");
        System.out.println("  \\____/|_|  \\__,_|\\___|_|    |_____/ \\__\\__,_|\\__|\\__,_|___/");
        System.out.println("                                                            ");
        System.out.println("                                                            ");
        System.out.println("____________________________________________________________________\n");
        System.out.println("Enter Order ID: ");
        System.out.println(" ");

        String orderID = sc.nextLine();
        boolean found = false;

        for (int i = 0; i < orderCount; i++) {
            if (orderIDs[i].equals(orderID)) {
                found = true;
                System.out.println("\n Phone Number: " + customerContacts[i] + ",\n Size: " + tShirtSizes[i] +
                        ",\n QTY: " + quantities[i] + ",\n Amount: " + orderAmounts[i] + ",\n Status: " + ORDER_STATUSES[orderStatuses[i]]);
                System.out.println("Do you want to change the order status? (Y/N): ");
                String confirm = sc.nextLine();

                if (confirm.equalsIgnoreCase("Y")) {
                    if (orderStatuses[i] != DELIVERED) {
                        orderStatuses[i]++;
                        System.out.println("[1]Order Delivering");
                    } else {
                        System.out.println("Can't change this order status, Order already delivered..!");
                    }
                }
                break;
            }
        }
        if (!found) {
            System.out.println("No order found with this Order ID.");
        }

        System.out.print("\nDo you want to change another order status? (Y/N): ");
        String anotherChange = sc.nextLine();
        if (anotherChange.equalsIgnoreCase("Y")) {
            clearConsole();
            setOrderStatus();
        } else {
            clearConsole();
            showMainMenu();
        }
    }

    public static void deleteOrder() {
        clearConsole();
        System.out.println("  _____       _      _          ____          _           ");
        System.out.println(" |  __ \\     | |    | |        / __ \\        | |          ");
        System.out.println(" | |  | | ___| | ___| |_ ___  | |  | |_ __ __| | ___ _ __ ");
        System.out.println(" | |  | |/ _ \\ |/ _ \\ __/ _ \\ | |  | | '__/ _` |/ _ \\ '__|");
        System.out.println(" | |__| |  __/ |  __/ ||  __/ | |__| | | | (_| |  __/ |   ");
        System.out.println(" |_____/ \\___|_|\\___|\\__\\___|  \\____/|_|  \\__,_|\\___|_|   ");
        System.out.println("                                                         ");
        System.out.println("                                                         ");
        System.out.println("__________________________________________________________________\n");
        System.out.println("Enter Order ID: ");
        String orderID = sc.nextLine();
        boolean found = false;

        for (int i = 0; i < orderCount; i++) {
            if (orderIDs[i].equals(orderID)) {
                found = true;
                System.out.println("\n Contact: " + customerContacts[i] + ",\n Size: " + tShirtSizes[i] +
                        ",\n Quantity: " + quantities[i] + ",\n Amount: " + orderAmounts[i] + ",\n Status: " + ORDER_STATUSES[orderStatuses[i]]);
                System.out.println("\nDo you want to delete this order? (Y/N): ");
                String confirm = sc.nextLine();
                if (confirm.equalsIgnoreCase("Y")) {
                    deleteOrderAt(i);
                    System.out.println("\nOrder Deleted!");
                } else {
                    System.out.println("\nOrder not Deleted!");
                }
            }
        }

        if (!found) {
            System.out.println("Invalid ID!");
        }

        System.out.println("\nDo you want to delete another order? (Y/N): ");
        String anotherOrder = sc.nextLine();
        if (anotherOrder.equalsIgnoreCase("Y")) {
            clearConsole();
            deleteOrder();
        } else {
            clearConsole();
            showMainMenu();
        }
    }

    public static void deleteOrderAt(int index) {
        for (int i = index; i < orderCount - 1; i++) {
            orderIDs[i] = orderIDs[i + 1];
            customerContacts[i] = customerContacts[i + 1];
            tShirtSizes[i] = tShirtSizes[i + 1];
            quantities[i] = quantities[i + 1];
            orderAmounts[i] = orderAmounts[i + 1];
            orderStatuses[i] = orderStatuses[i + 1];
        }
        orderCount--;
    }

    public static void clearConsole() {

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.println("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ex) {
            System.out.println("Error clearing console.");
        }
    }
}
