package com.scalefocus.service;

import com.scalefocus.constants.ApplicationConstants;
import com.scalefocus.model.ToDoList;
import com.scalefocus.model.User;
import com.scalefocus.repository.UserRepository;
import com.scalefocus.utils.Helper;

import java.util.List;
import java.util.Scanner;

public class UserService extends AbstractService<User> {
    private final UserRepository userRepository;
    public static User user;
    private Scanner sc;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        sc = new Scanner(System.in);
        user = null;
    }
    public void setScanner(Scanner sc){
        this.sc = sc;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Username and password must not be empty or null.");
            return null;
        }
        try {
            user = userRepository.findUserByUsername(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        if (user == null) {
            System.out.println("Invalid credentials.");
            return null;
        }
        if (!user.getPassword().equals(password)) {
            System.out.println("Password incorrect.");
            return null;
        }
        return user;
    }

    public String login() {
        System.out.println("Enter credentials to login.");
        while (user == null) {
            System.out.println("Enter username: ");
            String username = sc.nextLine();
            System.out.println("Enter password: ");
            String password = sc.nextLine();

            user = getUserByUsernameAndPassword(username, password);
            if (user == null) {
                System.out.println("Try again.");
            } else {
                break;
            }
        }
        return user.getPrivilege();
    }

    public void registerUser() {
        String[] data = new String[8];
        System.out.println("Enter privilege (ADMIN|USER): ");
        data[1] = sc.nextLine();
        System.out.println("Enter username: ");
        data[2] = sc.nextLine();
        System.out.println("Enter password: ");
        data[3] = sc.nextLine();
        System.out.println("Enter first name: ");
        data[4] = sc.nextLine();
        System.out.println("Enter last name: ");
        data[5] = sc.nextLine();
        data[6] = String.valueOf(UserService.user.getId());
        data[7] = String.valueOf(UserService.user.getId());

        User user = userRepository.buildEntity(data);
        userRepository.save(user);
        System.out.println("Successfully created user with id " + user.getId() + " and username " + data[2]);
    }

    public void editUser() {
        Helper.adminMenuEditUser();
        String option = sc.nextLine();
        System.out.println("Enter user id: ");
        int userId = Integer.parseInt(sc.nextLine());
        User user = findById(userId);
        if (user == null) {
            System.out.println("Invalid user id.");
            return;
        }

        switch (option) {
            case "U":
                System.out.println("Enter new username: ");
                user.setUsername(sc.nextLine());
                break;
            case "P":
                System.out.println("Enter new password: ");
                user.setPassword(sc.nextLine());
                break;
            case "R":
                System.out.println("Enter privilege (ADMIN|USER): ");
                user.setPrivilege(sc.nextLine().toUpperCase());
                break;
        }
        update(user);
        System.out.println("Successfully edited user with id " + userId);
    }

    public void deleteUser(TaskService taskService, ToDoListService toDoListService) {
        System.out.println("Enter user id: ");
        int userId = Integer.parseInt(sc.nextLine());
        User user = findById(userId);
        if (user == null) {
            System.out.println("Invalid user id.");
            return;
        }
        taskService.deleteTasksRelatedToUser(userId);
        toDoListService.deleteListsRelatedToUser(userId);

        delete(user);

        System.out.println("Successfully deleted user with id " + userId);
    }

    public void viewAllUsers() {
        findAll().forEach( u -> System.out.println(ApplicationConstants.userDetails(u)));
    }

    public void viewById() {
        System.out.println("Enter user id: ");
        int userId = Integer.parseInt(sc.nextLine());
        User user = findById(userId);
        if (user == null){
            System.out.println("Invalid user id.");
            return;
        }
        System.out.println(ApplicationConstants.userDetails(user));
    }

    public void logout(){
        user=null;
        System.out.println("Logging out...");
    }
}
