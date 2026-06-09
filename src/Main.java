import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserRegistry registry = new UserRegistry();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Відновити базу користувачів з файлу? (yes/no): ");
        String restoreAnswer = scanner.nextLine();

        if (restoreAnswer.equalsIgnoreCase("yes")) {
            System.out.print("Введіть шлях до файлу: ");
            String filePath = scanner.nextLine();

            registry.loadFromFile(filePath);
        }

        int choice;

        do {
            System.out.println("\n--- User Registry Menu ---");
            System.out.println("1. Register user");
            System.out.println("2. Login user");
            System.out.println("3. Logout user");
            System.out.println("4. Check if user registered");
            System.out.println("5. Remove user");
            System.out.println("6. Print total users");
            System.out.println("7. Display all users");
            System.out.println("8. Display users sorted by login");
            System.out.println("9. Display only logged in users");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter login: ");
                    String regLogin = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String regPassword = scanner.nextLine();

                    registry.registerUser(regLogin, regPassword);
                    break;

                case 2:
                    System.out.print("Enter login: ");
                    String login = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    registry.loginUser(login, password);
                    break;

                case 3:
                    System.out.print("Enter user id: ");
                    int logoutId = scanner.nextInt();
                    scanner.nextLine();

                    registry.logoutUser(logoutId);
                    break;

                case 4:
                    System.out.print("Enter login: ");
                    String checkLogin = scanner.nextLine();

                    if (registry.isUserRegistered(checkLogin)) {
                        System.out.println("Користувач зареєстрований");
                    } else {
                        System.out.println("Користувач не зареєстрований");
                    }
                    break;

                case 5:
                    System.out.print("Enter user id: ");
                    int removeId = scanner.nextInt();
                    scanner.nextLine();

                    registry.removeUser(removeId);
                    break;

                case 6:
                    registry.printTotalUniqueUsers();
                    break;

                case 7:
                    registry.displayAllUsers();
                    break;

                case 8:
                    LinkedList<User> sortedUsers = registry.getInOrder(
                            (u1, u2) -> u1.getName().compareTo(u2.getName()) <= 0
                    );

                    for (User user : sortedUsers) {
                        registry.printUser(user);
                    }
                    break;

                case 9:
                    LinkedList<User> loggedUsers = registry.getFiltered(
                            user -> user.isLoggedIn()
                    );

                    for (User user : loggedUsers) {
                        registry.printUser(user);
                    }
                    break;

                case 0:
                    System.out.print("Зберегти користувачів у файл? (yes/no): ");
                    String saveAnswer = scanner.nextLine();

                    if (saveAnswer.equalsIgnoreCase("yes")) {
                        System.out.print("Введіть шлях до файлу: ");
                        String savePath = scanner.nextLine();

                        registry.saveToFile(savePath);
                    }

                    System.out.println("Program finished");
                    break;

                default:
                    System.out.println("Invalid option");
            }

        } while (choice != 0);

        scanner.close();
    }
}
