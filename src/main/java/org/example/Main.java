package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.DBException;
import org.example.model.User;
import org.example.service.Service;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    static Service service = new Service();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("start main");
        boolean running = true;
        while (running) {
            showMenu();
            String command = scanner.nextLine().trim();
            switch (command) {
                case "1":
                    createUser();
                    break;
                case "2":
                    updateUser();
                    break;
                case "3":
                    deleteUser();
                    break;
                case "4":
                    findUser();
                    break;
                case "5":
                    getAllUsers();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Wrong command");
                    break;
            }
        }
        scanner.close();
        System.out.println("Thank you for using my program.");
        logger.info("end main");
    }

    private static void showMenu() {
        System.out.println("""
                      Menu
                1. Create User
                2. Update User
                3. Delete User
                4. Find User by ID
                5. Find All Users
                0. Escape
                """);
        System.out.print("Enter choice: ");
    }

    private static void createUser() {
        try {
            System.out.print("Enter user name: ");
            String userName = scanner.nextLine();
            System.out.print("Enter the user's age: ");
            int userAge = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter user email: ");
            String userEmail = scanner.nextLine();
            User user = new User(userName, userAge, userEmail);
            service.saveUser(user);
            System.out.println("User " + user.getName() + " has been created");
        } catch (DBException e) {
            logger.error("Database error", e);
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void findUser() {
        try {
            System.out.print("Enter user id: ");
            Long userId = Long.valueOf(scanner.nextLine());
            User user = service.findUser(userId);
            if (user != null) System.out.println("User found: " + user.getName());
            else System.out.println("User not found");
        } catch (DBException e) {
            logger.error("Database error", e);
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void updateUser() {
        try {
            System.out.print("Enter user id: ");
            Long userId = Long.valueOf(scanner.nextLine());
            User user = service.findUser(userId);
            if (user == null) {
                System.out.println("User not found");
                return;
            }
            String oldName = user.getName();
            System.out.println("User found: " + oldName);
            System.out.println("Enter new name: ");
            String newName = scanner.nextLine();
            if (!newName.isBlank()) user.setName(newName);
            System.out.println("Enter new age: ");
            int newAge = Integer.parseInt(scanner.nextLine());
            if (newAge > 0) user.setAge(newAge);
            System.out.print("Enter new email: ");
            String newEmail = scanner.nextLine();
            if (!newEmail.isBlank()) user.setEmail(newEmail);
            service.updateUser(user);
            System.out.println("User " + oldName + " has been updated");
        } catch (DBException e) {
            logger.error("Database error", e);
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try {
            System.out.print("Enter user id: ");
            Long userId = Long.valueOf(scanner.nextLine());
            User user = service.findUser(userId);
            if (user == null) {
                System.out.println("User not found");
                return;
            }
            System.out.println("User found: " + user.getName());
            service.deleteUser(userId);
            System.out.println("User " + user.getName() + " has been deleted");
        } catch (DBException e) {
            logger.error("Database error", e);
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        try {
            List<User> users = service.findAllUsers();
            if (users.isEmpty()) System.out.println("No users found");
            else {
                for (User user : users) {
                    System.out.println(user.getId() + "|" + user.getName() + "|" + user.getAge()
                            + "|" + user.getEmail() + "|" + user.getCreatedAt());
                }
            }
        } catch (DBException e) {
            logger.error("Database error", e);
            System.out.println("Database error: " + e.getMessage());
        }
    }
}