package ProjectRecharge;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by seb on 28/3/17.
 */
public class TaskDataModel {

    private int id;
    private String title;
    private String frequency;
    private String notes;
    private Date lastCompleted;



    public TaskDataModel() {




    }

    public TaskDataModel(int id, String title, String frequency, String notes, Date lastCompleted) {


        this.id = id;
        this.title = title;
        this.frequency = frequency;
        this.notes = notes;
        this.lastCompleted = lastCompleted;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public double calculateCharge() {


        if (frequency.equals("m")) { // If frequency == minutes


            return calculateChargeForDuration(Duration.ofMinutes(1));


        } else if (frequency.equals("h")) { // If frequency == hours

            return calculateChargeForDuration(Duration.ofHours(1));


        } else if (frequency.equals("d")) { // If frequency == daily

            return calculateChargeForDuration(Duration.ofDays(1));

        } else if (frequency.equals("w")) { // If frequency == weekly

            return calculateChargeForDuration(Duration.ofDays(7));

        } else if (frequency.equals("f")) { // If frequency == fortnightly

            return calculateChargeForDuration(Duration.ofDays(14));

      //asdfdgf


        } else {

            return 0.0;


        }


    }

    private double calculateChargeForDuration(Duration duration) {


        // get the due date as an Instant plus the time duration
        Instant dueDate = lastCompleted.toInstant().plus(duration);

        // Get the difference between now and the due date.
        long difference = Duration.between(dueDate,Instant.now()).toMillis();

        // Now divide by one unit of time
        double charge =  - difference / Double.valueOf(duration.toMillis());

        // Make sure charge is not negative.
        if (charge <= 0.0) {

            charge = 0.0;

        }

        return charge;
    }




    public void rechargeTask() {


        lastCompleted = new Date();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getFormattedLastCompleted() {

        String output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(lastCompleted);

        return output;

    }

    public void setLastCompleted(Date lastCompleted) {
        this.lastCompleted = lastCompleted;
    }
}
