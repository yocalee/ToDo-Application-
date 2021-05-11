package com.scalefocus.repository;

import com.scalefocus.model.User;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepository extends com.scalefocus.repository.DataRepository<User> {

    public UserRepository() throws IOException {
        super("src/main/java/com/scalefocus/users.txt");
    }
    @Override
    public User buildEntity(String[] data) {
        User user;
        if (data.length == 8) {
                user = new User(super.getNextId() + 1, data[1].toUpperCase(), data[2], data[3], data[4], data[5], Integer.parseInt(data[6]), Integer.parseInt(data[7]));
                user.setCreationDate(LocalDate.now());
                user.setLastChangeDate(LocalDate.now());
        } else {
            user = new User(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], data[5], Integer.parseInt(data[7]), Integer.parseInt(data[9]));
            user.setCreationDate(LocalDate.parse(data[6]));
            user.setLastChangeDate(LocalDate.parse(data[8]));

            for (int i = 9; i < data.length; i++) {
                String current = data[i];
                if (current.contains("PendingTasks")) {
                    current = current.replace("PendingTasks", "");
                    user.setPendingTasks(validateTypeArray(current));
                }
                if (current.contains("Tasks")) {
                    current = current.replace("Tasks", "");
                    user.setTasks(validateTypeArray(current));
                }
                if (current.contains("Lists")) {
                    current = current.replace("Lists", "");
                    user.setLists(validateTypeArray(current));
                }
            }
        }
        return user;
    }

    private List<Integer> validateTypeArray(String current) {
        return Arrays.stream(current.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
    }

    public User findUserByUsername(String username){
        User user = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/com/scalefocus/users.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split("\\*\\*\\*\\*");

                if (data[2].equals(username)){
                    user = buildEntity(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong with file's path.");
        }
        return user;
    }
}
