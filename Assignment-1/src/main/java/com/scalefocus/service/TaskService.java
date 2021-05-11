package com.scalefocus.service;

import com.scalefocus.constants.ApplicationConstants;
import com.scalefocus.model.Task;
import com.scalefocus.model.ToDoList;
import com.scalefocus.model.User;
import com.scalefocus.repository.TaskRepository;
import com.scalefocus.utils.Helper;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TaskService extends AbstractService<Task> {
    private final TaskRepository taskRepository;
    private final Scanner sc = new Scanner(System.in);

    public TaskService(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
    }

    public List<Task> findTasksByUserId(Integer id) {
        return taskRepository.findTasksByUserId(id);
    }

    public void createTask(UserService userService, ToDoListService listService) {
        String[] data = new String[8];
        System.out.println("Enter title: ");
        data[1] = sc.nextLine();
        System.out.println("Enter description: ");
        data[2] = sc.nextLine();
        data[3] = "INPROGRESS";
        System.out.println("Enter completion date in format (y-m-d): ");
        data[4] = sc.nextLine();
        data[5] = String.valueOf(UserService.user.getId());
        data[6] = String.valueOf(UserService.user.getId());

        System.out.println("Enter valid list id: ");
        Integer listId = 0;
        ToDoList list = null;
        try {
            listId = Integer.parseInt(sc.nextLine());
            try {
                list = listService.findById(listId);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Invalid list id.");
            }
            if (!UserService.user.getLists().contains(listId)) {
                throw new IllegalArgumentException("You can not add task to list you are not the creator.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        data[7] = String.valueOf(list.getId());
        Task task = taskRepository.buildEntity(data);
        taskRepository.save(task);

        list.getTasks().add(task.getId());
        listService.update(list);

        UserService.user.getTasks().add(task.getId());
        userService.update(UserService.user);

        System.out.printf("Successfully created task with id %d.%n", task.getId());
    }

    public void editTask(UserService userService) {
        System.out.println("Enter task id: ");
        int taskId = Integer.parseInt(sc.nextLine());
        Task task = findById(taskId);
        if (task == null) {
            System.out.println("Invalid task id.");
            return;
        }
        if (!UserService.user.getTasks().contains(task.getId())) {
            System.out.println("You have no permission to edit this task.");
        }
        Helper.taskMenuEdit();

        String option = sc.nextLine().toUpperCase();
        switch (option) {
            case "T":
                System.out.println("Enter new title: ");
                task.setTitle(sc.nextLine());
                break;
            case "D":
                System.out.println("Enter new description: ");
                task.setDescription(sc.nextLine());
                break;
            case "S":
                System.out.println("Enter task status: ");
                task.setStatus(sc.nextLine());
                break;
            case "O":
                System.out.println("[R]emove user, [A]dd user");
                String op = sc.nextLine();
                Helper.viewUserIds(userService);
                System.out.println("Enter user id: ");
                int userId = Integer.parseInt(sc.nextLine());
                User user = userService.findById(userId);
                if (user == null) {
                    System.out.println("Invalid user id.");
                    return;
                }
                if (op.equalsIgnoreCase("R")) {
                    task.getUsers().remove(Integer.valueOf(user.getId()));
                    user.getTasks().remove(Integer.valueOf(taskId));
                    userService.update(user);
                    System.out.println("Successfully removed user with id " + userId);
                } else if (op.equalsIgnoreCase("A")) {
                    user.getPendingTasks().add(taskId);
                    userService.update(user);
                    System.out.println("Successfully sent request to user with id " + userId);
                    return;
                }
                break;
        }
        this.taskRepository.update(task.getId(), task);
    }

    public void deleteTask(UserService userService, ToDoListService toDoListService) {
        System.out.println("Enter task id to delete: ");
        Integer taskId = Integer.parseInt(sc.nextLine());
        Task task = findById(taskId);
        if (task == null) {
            System.out.println("Invalid task id.");
            return;
        }
        if (task.getCreatorId() != UserService.user.getId()) {
            System.out.println("You have no permission to delete this task.");
            return;
        }

        this.taskRepository.delete(taskId);
        UserService.user.getTasks().remove(taskId);
        userService.update(UserService.user);

        for (ToDoList list : toDoListService.findAll()) {
            if (list.getTasks().contains(taskId)) {
                list.getTasks().remove(taskId);
                toDoListService.update(list);
                break;
            }
        }
    }

    public void acceptTasks(UserService userService) {
        System.out.println("Loading pending tasks...");
        List<Integer> pending = UserService.user.getPendingTasks();
        for (Integer taskId : pending) {
            Task task = this.taskRepository.findById(taskId).get();
            System.out.println(ApplicationConstants.taskDetails(task));
            System.out.println("Do you want to accept it? [Yes] | [No]");
            String option = sc.nextLine();
            if (option.equalsIgnoreCase("yes")) {
                UserService.user.getTasks().add(taskId);
                task.getUsers().add(UserService.user.getId());
                taskRepository.update(task.getId(), task);
                System.out.printf("Task with id %d is added to your tasks.%n", taskId);
            }
        }
        UserService.user.setPendingTasks(new LinkedList<>());
        userService.update(UserService.user);
    }

    public void deleteTasksRelatedToUser(int userId) {
        findTasksByUserId(userId)
                .forEach(t -> {
                    if (t.getCreatorId() == userId) {
                        delete(t);
                    } else {
                        t.getUsers().remove(Integer.valueOf(userId));
                        update(t);
                    }
                });
    }
}
