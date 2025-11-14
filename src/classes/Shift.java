package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Shift {
    private String title;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfPeopleNeeded;
    private static List<Shift> shifts = new ArrayList<>();

    public Shift(String title, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, int numberOfPeopleNeeded) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfPeopleNeeded = numberOfPeopleNeeded;
    }

    private void manageShift(String action, int numberOfPeopleNeeded, LocalDateTime startTime, LocalDateTime endTime) {
        switch(action) {
            case "addPeople" -> {
                if(numberOfPeopleNeeded < 3 || numberOfPeopleNeeded > 7) {
                    throw new IllegalArgumentException("Number of people needed must be between 3 and 7");
                } else {
                    setNumberOfPeopleNeeded(numberOfPeopleNeeded);
                }
                shifts.add(this);
            }
            case "startTimeEdit" -> {
                this.startTime = startTime;
            }
            case "endTimeEdit" -> {
                this.endTime = endTime;
            }
        }
    }

    public void setNumberOfPeopleNeeded(int numberOfPeopleNeeded) {
        this.numberOfPeopleNeeded = numberOfPeopleNeeded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getNumberOfPeopleNeeded() {
        return numberOfPeopleNeeded;
    }

    public static List<Shift> getShifts() {
        return shifts;
    }

    public static void setShifts(List<Shift> shifts) {
        Shift.shifts = shifts;
    }
}
