package com.scalefocus.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoList extends BaseEntity {
    private String title;
    private LocalDate creationDate;
    private int creatorId;
    private LocalDate lastChangeDate;
    private int modifierId;
    private List<Integer> tasks;

    public ToDoList() {
    }

    public ToDoList(int id, String title, int creatorId, int modifierId) {
        super(id);
        this.title = title;
        this.creatorId = creatorId;
        this.modifierId = modifierId;
        tasks = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDate getLastChangeDate() {
        return lastChangeDate;
    }

    public void setLastChangeDate(LocalDate lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public int getModifierId() {
        return modifierId;
    }

    public void setModifierId(int modifierId) {
        this.modifierId = modifierId;
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        String str = String.format("%d****%s****%s****%d****%s****%d",
                super.getId(), title, creationDate, creatorId, lastChangeDate, modifierId);
        if (tasks.size() > 0) {
            str += "****" + tasks.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        return str;
    }

}
