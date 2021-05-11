package com.scalefocus.repository;

import com.scalefocus.model.ToDoList;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ToDoListRepository extends com.scalefocus.repository.DataRepository<ToDoList> {

    public ToDoListRepository() throws IOException {
        super("src/main/java/com/scalefocus/todo.txt");
    }

    @Override
    public ToDoList buildEntity(String[] data) {
        ToDoList list;
        if (data.length == 4) {
            list = new ToDoList(super.getNextId(), data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]));
            list.setCreationDate(LocalDate.now());
            list.setLastChangeDate(LocalDate.now());
        } else {
            list = new ToDoList(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[3]), Integer.parseInt(data[5]));
            list.setCreationDate(LocalDate.parse(data[2]));
            list.setLastChangeDate(LocalDate.parse(data[4]));
            if (data.length == 7) {
                List<Integer> tasks = Arrays.stream(
                                            data[6].split(","))
                                            .mapToInt(Integer::parseInt)
                                            .boxed()
                                            .collect(Collectors.toList());
                list.setTasks(tasks);
            }
        }
        return list;
    }

    public ToDoList findListByTaskId(Integer taskId){
        return findAll()
                .stream()
                .filter(t -> t.getTasks().contains(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id."));
    }
}
