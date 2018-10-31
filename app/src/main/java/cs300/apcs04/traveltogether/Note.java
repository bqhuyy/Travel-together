package cs300.apcs04.traveltogether;

import com.orm.SugarRecord;

import java.util.UUID;

public class Note extends SugarRecord  {
    String title, note, AnoteID;
    long time;

    public Note() {
    }

    public String GetAnoteID(){
        return this.AnoteID;
    }

    public Note(String title, String note, long time) {
        this.title = title;
        this.note = note;
        this.time = time;
        this.AnoteID = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
