package com.scalefocus.constants;

import com.scalefocus.model.Task;
import com.scalefocus.model.ToDoList;
import com.scalefocus.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationConstants {
    public final static String USER_DETAILS_TEMPLATE = "User with id %d, privilege %s, username %s, password %s, first name %s, last name %s.\n" +
                                                        "The user was created on %s by admin with id %d." ;

    public final static String TASK_DETAILS_TEMPLATE = "Task with id %d, title %s, description %s, status %s and list id %d will be completed on %s. \n" +
                                                        "Task was created by user with id %d on %s, modified by %d on %s and has %d users(%s) working on it.";

    public final static String LIST_DETAILS_TEMPLATE = "List with id %d, title %s was created on %s by user with id %d and contains %d tasks(%s). \n";

    public static String listDetails(ToDoList list){
        return String.format(LIST_DETAILS_TEMPLATE, list.getId(), list.getTitle(), list.getCreationDate(),
                list.getCreatorId(), list.getTasks().size(), stream(list.getTasks()));
    }

    public static String taskDetails(Task task){
        return String.format(TASK_DETAILS_TEMPLATE, task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
                task.getListId(), task.getCompletionDate(), task.getCreatorId(), task.getCreationDate(),
                task.getModifierId(), task.getModifiedDate(), task.getUsers().size(), stream(task.getUsers()));
    }
    public static String userDetails(User user){
        return String.format(USER_DETAILS_TEMPLATE, user.getId(), user.getPrivilege(), user.getUsername(),
                user.getPassword(), user.getFirstName(),user.getLastName(), user.getCreationDate(), user.getCreatorId());
    }
    private static String stream(List<Integer> list){
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
