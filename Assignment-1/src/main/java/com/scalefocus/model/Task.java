package com.scalefocus.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Task extends BaseEntity {
    private String title;
    private String description;
    private String status;
    private LocalDate completionDate;
    private LocalDate creationDate;
    private LocalDate modifiedDate;
    private int modifierId;
    private int creatorId;
    private List<Integer> users;
    private int listId;

    public Task() {
    }

    public Task(int id, String title, String description, String status, LocalDate completionDate, int modifierId, int creatorId, int listId) {
        super(id);
        this.title = title;
        this.description = description;
        this.status = status.toUpperCase();
        this.completionDate = completionDate;
        this.modifierId = modifierId;
        this.creatorId = creatorId;
        users = new LinkedList<>();
        this.listId = listId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getModifierId() {
        return modifierId;
    }

    public void setModifierId(int modifierId) {
        this.modifierId = modifierId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        String str = String.format("%d****%s****%s****%s****%s****%d****%s****%d****%s****%d",
                super.getId(), title, description, status,completionDate,
                creatorId, creationDate, modifierId, modifiedDate, listId);

        if (users.size() > 0) {
            str += "****" +
                    users.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        return str;
    }
}
