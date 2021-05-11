package com.scalefocus.service;

import com.scalefocus.model.Task;
import com.scalefocus.model.ToDoList;
import com.scalefocus.model.User;
import com.scalefocus.repository.ToDoListRepository;
import com.scalefocus.utils.Helper;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ToDoListService extends AbstractService<ToDoList>{
    private final ToDoListRepository toDoListRepository;
    private final Scanner sc = new Scanner(System.in);

    public ToDoListService(ToDoListRepository toDoListRepository) {
        super(toDoListRepository);
        this.toDoListRepository = toDoListRepository;
    }

    public ToDoList findListByTaskId(Integer taskId){
        return toDoListRepository.findListByTaskId(taskId);
    }

    public boolean validIdList(int listId) {
        return findById(listId) != null;
    }

    public List<ToDoList> findListByCreatorId(Integer userId) {
        return findAll().stream().filter(l -> l.getCreatorId()==userId).collect(Collectors.toList());
    }

    public void createList(UserService userService) {
        String[] data = new String[4];

        System.out.println("Enter title: ");
        data[1] = sc.nextLine();
        data[2] = String.valueOf(UserService.user.getId());
        data[3] = String.valueOf(UserService.user.getId());

        ToDoList toDoList = toDoListRepository.buildEntity(data);
        toDoListRepository.save(toDoList);

        UserService.user.getLists().add(toDoList.getId());
        userService.update(UserService.user);
        System.out.println("Successfully created list with id " + toDoList.getId());
    }

    public void editList(TaskService taskService, UserService userService){
        System.out.println("Enter list id :");
        int listId = Integer.parseInt(sc.nextLine());
        ToDoList list = findById(listId);
        if (list == null){
            System.out.println("Invalid list id.");
            return;
        }
        if (list.getCreatorId() != UserService.user.getId()){
            System.out.println("Only creators are allowed to modify the list.");
            return;
        }

        Helper.userListEditMenu();
        String option = sc.nextLine().toUpperCase();
        switch (option){
            case "T":
                System.out.println("Enter new title: ");
                list.setTitle(sc.nextLine());
                update(list);
                break;
            case "R":
                System.out.println("Enter task id to remove from the list: ");
                Integer taskId = Integer.parseInt(sc.nextLine());
                Task task = taskService.findById(taskId);
                if (task.getListId() != listId){
                    System.out.println("The list does not contains task with this id.");
                    return;
                }
                userService.findAll()
                        .forEach(u -> {
                            if (u.getTasks().contains(taskId)){
                                u.getTasks().remove(taskId);
                                userService.update(u);
                            }
                        });
                list.getTasks().remove(taskId);
                update(list);
                taskService.delete(task);
                System.out.println("Successfully removed task with id " + taskId+ " from the list.");
                break;
        }
        System.out.println("Successfully edited list with id " + listId);
    }

    public void deleteList(TaskService taskService, UserService userService) {
        System.out.println("Enter list id: ");
        int listId = Integer.parseInt(sc.nextLine());

        ToDoList list = findById(listId);

        if (list == null){
            System.out.println("Invalid list id.");
            return;
        }
        if (list.getCreatorId() != UserService.user.getId()){
            System.out.println("Only creator of the list can delete it.");
            return;
        }
        this.toDoListRepository.delete(listId);
        for (Task task : taskService.findAll()) {

            if (task.getListId() == listId){

                User creator = userService.findById(task.getCreatorId());
                creator.getTasks().remove(Integer.valueOf(task.getId()));
                userService.update(creator);

                for (Integer user : task.getUsers()) {
                    User u = userService.findById(user);
                    u.getTasks().remove(Integer.valueOf(task.getId()));
                    userService.update(u);
                }

                taskService.delete(task);
            }
        }
        UserService.user.getLists().remove(Integer.valueOf(listId));
        userService.update(UserService.user);
        System.out.println("Successfully deleted list with id " + listId);
    }

    public void deleteListsRelatedToUser(int userId) {
        findListByCreatorId(userId).forEach(this::delete);
    }
}
