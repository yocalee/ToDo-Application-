package com.scalefocus.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class User extends BaseEntity{
    private String privilege;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate creationDate;
    private int creatorId;
    private LocalDate lastChangeDate;
    private int modifiedId;
    private List<Integer> tasks;
    private List<Integer> lists;
    private List<Integer> pendingTasks;

    public User(int id, String privilege, String username, String password, String firstName,
                String lastName, int creatorId, int modifiedId) {
        super(id);
        this.privilege = privilege;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creatorId = creatorId;
        this.modifiedId = modifiedId;
        this.tasks = new LinkedList<>();
        this.lists = new LinkedList<>();
        this.pendingTasks = new LinkedList<>();
    }

    public User (){}

    public List<Integer> getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(List<Integer> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(int modifiedId) {
        this.modifiedId = modifiedId;
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    public List<Integer> getLists() {
        return lists;
    }

    public void setLists(List<Integer> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        String str =  String.format("%d****%s****%s****%s****%s****%s****%s****%d****%s****%d",
                super.getId(), this.privilege, this.username, this.password, this.firstName, this.lastName,
                this.creationDate, this.creatorId, this.lastChangeDate, this.modifiedId);

        if (pendingTasks.size() > 0){
            str += "****PendingTasks" +
                    pendingTasks.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        if (tasks.size() > 0){
            str += "****Tasks" +
                tasks.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        if(lists.size()>0){
            str+= "****Lists"+
                lists.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        return str;
    }
}
