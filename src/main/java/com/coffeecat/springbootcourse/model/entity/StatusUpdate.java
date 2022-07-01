package com.coffeecat.springbootcourse.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity //marks class as important for saving into DB!
@Table(name="status_update") //name of table to save to. -automatically created @runtime
public class StatusUpdate {

    @Id //specify id as the PK:
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO) //pick generation-method automatically
    private Long id; //better class type, not primitive type

    //adding constraints for valid input:
    @Size(min=5, max=255, message="{addstatus.text.size}") //msg loads property from properties file!
    @Column(name="text")
    private String text;

    @Column(name="added")
    @Temporal(TemporalType.TIMESTAMP) //type storing DATE/TIME
    //to access date that was converted .toString() -> Turn back to Java.util.date Object:
    @DateTimeFormat(pattern="yyyy/MM/dd hh:mm:ss")
    private Date added; //don't use keywords conflicting with mySQL instead of WHEN use added!

    @PrePersist //calls method before object is persisted(saved to DB)
    protected void onCreate(){
        if(added == null) {
            added = new Date();
        }
    }

    public StatusUpdate() { //IDE-required no-argument constructor?
    }

    public StatusUpdate(String text) {
        this.text = text;
    }
    public StatusUpdate(String text, Date added) {
        this.text = text;
        this.added = added;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public Date getAdded() {
        return added;
    }
    public void setAdded(Date added) {
        this.added = added;
    }

    //For Debug:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusUpdate that = (StatusUpdate) o;

        if (!id.equals(that.id)) return false;
        if (!text.equals(that.text)) return false;
        return added.equals(that.added);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + added.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StatusUpdate{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", added=" + added +
                '}';
    }
}
