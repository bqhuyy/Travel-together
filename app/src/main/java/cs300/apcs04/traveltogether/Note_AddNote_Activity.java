package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Note_AddNote_Activity extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;

    EditText etTitle, etDesc;

    String title, noteID, description;
    long time;

    boolean editingNote;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = findViewById(R.id.addnote_toolbar);
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
        myRef = database.getReference("note/" + getIntent().getStringExtra("plan_ID"));
        editingNote = getIntent().getBooleanExtra("isEditing", false);
        if (editingNote){
            title = getIntent().getStringExtra("note_title");
            description = getIntent().getStringExtra("note_description");
            noteID = getIntent().getStringExtra("note_ID");
            etTitle.setText(title);
            etDesc.setText(description);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add note to Database
                String newTitle = etTitle.getText().toString().trim();
                String newDesc = etDesc.getText().toString().trim();
                int callback = 0;
                Intent data = new Intent(Note_AddNote_Activity.this, Note_Main_Activity.class);
                if (!editingNote){
                    if (!(newTitle.isEmpty() || newDesc.isEmpty())){
                        Log.d("Note", "Saving new note");
                        Note note = new Note(newTitle, newDesc);
                        myRef.child(note.getNoteID()).setValue(note);
                        data.putExtra("NewNote", note);
                        callback = 2;
                    } else {
                        Toast.makeText(Note_AddNote_Activity.this, "You need to fill in both Title and Description", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Note", "Updating a note");
                    if (!(newTitle.equals(title) && newDesc.equals(description)) && !(newTitle.isEmpty() || newDesc.isEmpty())){
                        Note newNote = new Note(newTitle, newDesc, noteID);
                        myRef.child(noteID).setValue(newNote);
                        callback = 1;
                        data.putExtra("NewNote", newNote);
                    } else {
                        Toast.makeText(Note_AddNote_Activity.this, "You need to fill in both Title and Description", Toast.LENGTH_SHORT).show();
                    }
                }
                if (callback != 0) {
                    data.putExtra("callback", callback);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }
}
