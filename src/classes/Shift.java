package classes;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Shift {
    private String title;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfPeopleNeeded;

    public Shift(String title, LocalDateTime date, LocalDateTime startTime, LocalDateTime endTime, int numberOfPeopleNeeded) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfPeopleNeeded = numberOfPeopleNeeded;
    }

    ///przykładowy management shiftu - można dopisać ale pytanie co hehe
    private void manageShift(int numberOfPeopleNeeded) {
        if(numberOfPeopleNeeded < 3 || numberOfPeopleNeeded > 7) {
            throw new IllegalArgumentException("No. of people needed must be between 3 and 7");
        } else {
            setNumberOfPeopleNeeded(numberOfPeopleNeeded);
        }
    }

    public void setNumberOfPeopleNeeded(int numberOfPeopleNeeded) {
        this.numberOfPeopleNeeded = numberOfPeopleNeeded;
    }
}
