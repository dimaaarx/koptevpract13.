import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class UserRegistry {
    private Map<UserIdentifier, User> users;
    private int nextId;

    public UserRegistry() {
        users = new HashMap<>();
        nextId = 1;
    }

    public void registerUser(String login, String password) {
        if (isUserRegistered(login)) {
            System.out.println("Користувач " + login + " вже є у списку");
            return;
        }

        UserIdentifier identifier = new UserIdentifier(nextId, login);
        User user = new User(identifier, password);

        users.put(identifier, user);
        nextId++;

        System.out.println("Користувача " + login + " зареєстровано");
    }

    public void loginUser(String login, String password) {
        User user = findByLogin(login);

        if (user == null || !user.checkPassword(password)) {
            System.out.println("Неможливо ідентифікувати або аутентифікувати користувача");
            return;
        }

        user.login();
        System.out.println("Користувач " + login + " увійшов у систему");
    }

    public void logoutUser(int userId) {
        User user = findById(userId);

        if (user == null) {
            System.out.println("Користувача з id " + userId + " не знайдено");
            return;
        }

        user.logout();
        System.out.println("Користувач " + user.getName() + " вийшов із системи");
    }

    public boolean isUserRegistered(String login) {
        return findByLogin(login) != null;
    }

    public void removeUser(int id) {
        UserIdentifier keyToRemove = null;

        for (UserIdentifier identifier : users.keySet()) {
            if (identifier.getId() == id) {
                keyToRemove = identifier;
                break;
            }
        }

        if (keyToRemove == null) {
            System.out.println("Користувача з id " + id + " не знайдено");
            return;
        }

        User removedUser = users.remove(keyToRemove);
        System.out.println("Користувача " + removedUser.getName() + " видалено");
    }

    public void printTotalUniqueUsers() {
        System.out.println("Кількість користувачів: " + users.size());
    }

    public void displayAllUsers() {
        LinkedList<User> list = getUserList();

        if (list.isEmpty()) {
            System.out.println("Список користувачів порожній");
            return;
        }

        for (User user : list) {
            printUser(user);
        }
    }

    public LinkedList<User> getUserList() {
        LinkedList<User> list = new LinkedList<>();

        for (User user : users.values()) {
            list.add(user);
        }

        list.sort((u1, u2) -> u1.getId() - u2.getId());

        return list;
    }

    public LinkedList<User> getInOrder(BiPredicate<User, User> comparator) {
        LinkedList<User> list = getUserList();

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                User first = list.get(j);
                User second = list.get(j + 1);

                if (!comparator.test(first, second)) {
                    list.set(j, second);
                    list.set(j + 1, first);
                }
            }
        }

        return list;
    }

    public LinkedList<User> getFiltered(Predicate<User> predicate) {
        LinkedList<User> result = new LinkedList<>();

        for (User user : users.values()) {
            if (predicate.test(user)) {
                result.add(user);
            }
        }

        return result;
    }

    public void saveToFile(String filePath) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(users);
            objectOutputStream.writeInt(nextId);

            System.out.println("Користувачів збережено у файл: " + filePath);

        } catch (Exception e) {
            System.out.println("Помилка збереження: " + e.getMessage());
        }
    }

    public void loadFromFile(String filePath) {
        try (
                FileInputStream fileInputStream = new FileInputStream(filePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            users = (Map<UserIdentifier, User>) objectInputStream.readObject();
            nextId = objectInputStream.readInt();

            for (User user : users.values()) {
                user.resetLoginState();
            }

            System.out.println("Користувачів відновлено з файлу: " + filePath);

        } catch (Exception e) {
            System.out.println("Помилка відновлення: " + e.getMessage());
        }
    }

    public void printUser(User user) {
        System.out.println(
                "id: " + user.getId()
                        + ", login: " + user.getName()
                        + ", registered: " + user.getRegistrationDate()
                        + ", logged in: " + user.isLoggedIn()
                        + ", last login: " + user.getLastLoginDate()
        );
    }

    private User findByLogin(String login) {
        for (User user : users.values()) {
            if (user.getName().equals(login)) {
                return user;
            }
        }

        return null;
    }

    private User findById(int id) {
        for (User user : users.values()) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }
}
