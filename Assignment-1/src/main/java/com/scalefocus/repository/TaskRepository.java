package com.scalefocus.repository;

import com.scalefocus.model.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRepository extends DataRepository<Task> {

    public TaskRepository() throws IOException {
        super("src/main/java/com/scalefocus/tasks.txt");
    }


    @Override
    public Task buildEntity(String[] data) {
        Task task;
        if (data.length == 8){
            task = new Task(super.getNextId(), data[1], data[2], data[3], LocalDate.parse(data[4]), Integer.parseInt(data[5]),
                    Integer.parseInt(data[6]), Integer.parseInt(data[7]));
            task.setCreationDate(LocalDate.now());
            task.setModifiedDate(LocalDate.now());
        }else{
            task = new Task(Integer.parseInt(data[0]), data[1], data[2],data[3], LocalDate.parse(data[4]), Integer.parseInt(data[5]),
                    Integer.parseInt(data[7]), Integer.parseInt(data[9]));
            task.setCreationDate(LocalDate.parse(data[6]));
        task.setModifiedDate(LocalDate.parse(data[8]));

            if (data.length == 11){
                List<Integer> users = Arrays.stream(data[10].split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                task.setUsers(users);
            }
        }
        task.setModifiedDate(LocalDate.now());
        return task;
    }
    public List<Task> findTasksByUserId(Integer id){
        List<Task> out = new LinkedList<>();
        for (Task task : findAll()) {
            if (task.getCreatorId() == id || task.getUsers().contains(id)){
                out.add(task);
            }
        }
        return out;
    }
}
