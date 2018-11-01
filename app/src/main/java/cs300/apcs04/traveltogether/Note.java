package cs300.apcs04.traveltogether;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Note implements  Serializable {
    private String title, description, noteID;
    private Date time;

    public Note() {}

    public String getNoteID(){ return this.noteID; }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        this.time = new Date();
        this.noteID = UUID.randomUUID().toString();
    }

    public Note(String title, String description, String noteID) {
        this.title = title;
        this.description = description;
        this.noteID = noteID;
        this.time = new Date();
    }

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }
}
