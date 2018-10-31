package cs300.apcs04.traveltogether;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note_Main_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;

    NotesAdapter adapter;
    List<Note> list_of_notes = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("note");

    //long initialCount;
    //int modifyPos = -1;
    Date tempDate;
    String tempID;
    final static int REQUEST_CODE_MODIFY = 1234;
    int callback = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        Log.d("Main", "onCreate");
        recyclerView = findViewById(R.id.main_list);
        fab = findViewById(R.id.fab);


        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(gridLayoutManager);

        list_of_notes = new ArrayList<>();
        adapter = new NotesAdapter(Note_Main_Activity.this, list_of_notes);
        recyclerView.setAdapter(adapter);

        // Re****************************ad data in firebase and insert to list

        //if (savedInstanceState != null)
            //modifyPos = savedInstanceState.getInt("modify");

        if (list_of_notes.isEmpty())
            Snackbar.make(recyclerView, "No list_of_notes added.", Snackbar.LENGTH_LONG).show();



        // Floating point action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(Note_Main_Activity.this, Note_AddNote_Activity.class);
                startActivity(addNoteIntent);
            }
        });

        //Handling swipe to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                //remove swiped item from list and notify the RecyclerView
                final int position = viewHolder.getAdapterPosition();
                final Note note = list_of_notes.get(viewHolder.getAdapterPosition());
                list_of_notes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(position);

                // remove on firease*********************************
                if ()

                Snackbar.make(recyclerView, "Note deleted.", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //note.save();
                                myRef.child(note.getNoteID()).setValue(note);
                                list_of_notes.add(position, note);
                                adapter.notifyItemInserted(position);
                            }
                        }).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //adapter on click listener
        adapter.SetOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.d("Main", "click");
                Note tempNote = list_of_notes.get(position);
                tempDate = tempNote.getTime();
                tempID = tempNote.getNoteID();

                Intent i = new Intent(Note_Main_Activity.this, Note_AddNote_Activity.class);
                i.putExtra("isEditing", true);
                i.putExtra("note_title", tempNote.getTitle());
                i.putExtra("note_description", tempNote.getDescription());
                i.putExtra("note_ID", tempNote.getNoteID());

                startActivityForResult(i, REQUEST_CODE_MODIFY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_MODIFY && resultCode == RESULT_OK){
            callback = data.getIntExtra("callback", 0);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //final long newCount = Note.count(Note.class);
        Log.d("SIZE", "onResume: " + list_of_notes.size());
        Intent i = new Intent();
        callback = i.getIntExtra("callback", 0);
        if (callback == 2){
            //a note is added
            Log.d("Main", "Adding new note");

            Note newNote = (Note) i.getSerializableExtra("NewNote");
            // Just load the last added note (new)
            list_of_notes.add(newNote);

           // list_of_notes.add(note);
            adapter.notifyDataSetChanged();
        }

        if (callback == 1){
            //list_of_notes.set(modifyPos, Note.listAll(Note.class).get(modifyPos));
            Note newnote = null;
            for(Note tmpNote : list_of_notes){
                if(tmpNote.getNoteID().equals(tempID) && tempDate.compareTo(tmpNote.getTime()) == 0){
                    newnote = tmpNote;
                    break;
                }
            }
            list_of_notes.remove(newnote);
            adapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long date) {
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(date));
    }
}
