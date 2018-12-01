package cs300.apcs04.traveltogether;

import java.io.Serializable;
import java.util.UUID;

public class Note implements  Serializable {
    private String title, description, noteID;
    private long time;

    public Note() {}

    public String getNoteID(){ return this.noteID; }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        this.time = System.currentTimeMillis();
        this.noteID = UUID.randomUUID().toString();
    }

    public Note(String title, String description, String noteID) {
        this.title = title;
        this.description = description;
        this.noteID = noteID;
        this.time = System.currentTimeMillis();
    }

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public long getTime() { return time; }

    public void setTime(long time) { this.time = time; }
}
