package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;

public class Shift implements Serializable {
    private static List<Shift> extent = new ArrayList<>();

    private String title;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfPeopleNeeded;
    private HashSet<Employee> employees;

    public Shift(){}
    public Shift(String title, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, int numberOfPeopleNeeded) {
        setTitle(title);
        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
        setNumberOfPeopleNeeded(numberOfPeopleNeeded);
        addExtent(this);
    }



    private void manageShift(String action, int numberOfPeopleNeeded, LocalDateTime startTime, LocalDateTime endTime) {
        switch(action) {
            case "addPeople" -> {
                if(numberOfPeopleNeeded < 3 || numberOfPeopleNeeded > 7) {
                    throw new IllegalArgumentException("Number of people needed must be between 3 and 7");
                } else {
                    setNumberOfPeopleNeeded(numberOfPeopleNeeded);
                }
                extent.add(this);
            }
            case "startTimeEdit" -> {
                this.startTime = startTime;
            }
            case "endTimeEdit" -> {
                this.endTime = endTime;
            }
        }
    }

    public void assignEmployee(Employee employee) throws Exception {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (employees.contains(employee)) {
            throw new IllegalArgumentException("This employee is already assigned to this shift");
        }

        if (employees.size() >= numberOfPeopleNeeded) {
            throw new IllegalStateException("This shift has reached the maximum number of employees needed");
        }

        employees.add(employee);
        employee.addWorkedInShift(this); //reverse connection
    }

    public void removeEmployee(Employee employee) throws Exception {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (employees.remove(employee)) {
            employee.removeWorkedInShift(this); //reverse connection
        }
    }

    public void setNumberOfPeopleNeeded(int numberOfPeopleNeeded) {
        if(numberOfPeopleNeeded < 4) {
            throw new IllegalArgumentException("Number of people needed must be greater than 3");
        }
        this.numberOfPeopleNeeded = numberOfPeopleNeeded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public HashSet<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(HashSet<Employee> employees) {
        this.employees = employees;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        if(date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        if(startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if(endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        this.endTime = endTime;
    }

    public int getNumberOfPeopleNeeded() {
        return numberOfPeopleNeeded;
    }

    public static void addExtent(Shift shift){
        if(shift == null){
            throw new IllegalArgumentException("Shift cannot be null");
        }
        if(extent.contains(shift)){
            throw new IllegalArgumentException("Such shift is already in data base");
        }
        extent.add(shift);
    }

    public static List<Shift> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Shift shift) {
        extent.remove(shift);
    }

    public static void clearExtent(){
        extent.clear();
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Shift>) in.readObject();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return numberOfPeopleNeeded == shift.numberOfPeopleNeeded && Objects.equals(title, shift.title) && Objects.equals(date, shift.date) && Objects.equals(startTime, shift.startTime) && Objects.equals(endTime, shift.endTime) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date, startTime, endTime, numberOfPeopleNeeded);
    }
}