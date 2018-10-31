package cs300.apcs04.traveltogether;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Note_AddNote_Activity extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;

    EditText etTitle, etDesc;

    String title, note;
    long time;

    boolean editingNote;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("note");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = (Toolbar) findViewById(R.id.addnote_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);
        getSupportActionBar().setTitle("Add new note");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etTitle = findViewById(R.id.addnote_title);
        etDesc = findViewById(R.id.addnote_desc);

        fab = findViewById(R.id.addnote_fab);

        //handle intent
        editingNote = getIntent().getBooleanExtra("isEditing", false);
        if (editingNote){
            title = getIntent().getStringExtra("note_title");
            note = getIntent().getStringExtra("note");
            time = getIntent().getLongExtra("note_time", 0);

            etTitle.setText(title);
            etDesc.setText(note);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add note to Database
                String newTitle = etTitle.getText().toString();
                String newDesc = etDesc.getText().toString();
                long newTime = System.currentTimeMillis();

                //TO DO: Check if note exists before saving
                if (!editingNote){
                    Log.d("Note", "Saving");
                    Note note = new Note(newTitle, newDesc, newTime);
                    //note.save();
                    myRef.child(note.GetAnoteID()).setValue(note);

                } else {
                    Log.d("Note", "Updating");
                    List<Note> notes = Note.find(Note.class, "title = ?", title);
                    if (notes.size() > 0) {

                        Note note = notes.get(0);
                        Log.d("got note", "note: " + note.title);
                        note.title = newTitle;
                        note.note = newDesc;
                        note.time = newTime;
                        //note.save();
                        myRef.child(note.GetAnoteID()).setValue(note);
                    }
                }
                finish();
            }
        });
    }
}
