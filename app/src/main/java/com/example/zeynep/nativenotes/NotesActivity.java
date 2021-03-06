package com.example.zeynep.nativenotes;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener, NoteInterface , SearchView.OnQueryTextListener{
    RecyclerView recyclerView;
    ImageView addNote;
    NotesAdapter notesAdapter;
    private Dialog dialog;
    List<NotesModel> notes;
    List<NotesModel> searchList;
    DataBaseHelper dataBaseHelper;
    UserModel user;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        user = UserPreferences.getInstance(this).readUser();
        recyclerView = findViewById(R.id.notes_recycler);
        addNote = findViewById(R.id.fab);
        addNote.setOnClickListener(this);
        readFromDB();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fab :
                showAddNotePopup();
                break;

        }
    }

    public void saveToDB(String note) {

        NotesModel notesModel = new NotesModel(note,String.valueOf(user.getID()));
        dataBaseHelper.addNote(notesModel);
        adapterPush();

    }

    public void updateNoteDB(NotesModel note, String newNote) {

        note.setNote(newNote);
        dataBaseHelper.updateNote(note);
        adapterPush();
    }


    public void readFromDB() {

        user = UserPreferences.getInstance(this).readUser();
        notes = dataBaseHelper.getAllNotes(user.getID());
        adapterPush();
    }


    public void adapterPush() {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        notesAdapter = new NotesAdapter(dataBaseHelper.getAllNotes(user.getID()), getApplicationContext(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(notesAdapter);
    }

       public void showAddNotePopup() {
        dialog = new Dialog(NotesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_layout);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Creating email model to send

        final EditText note_edt = dialog.findViewById(R.id.add_dialog_note);


        dialog.findViewById(R.id.add_note_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                saveToDB(note_edt.getText().toString());
            }
        });
        dialog.findViewById(R.id.add_note_delete_btn).setClickable(false);
        dialog.show();
    }

    @Override
    public void showUpdateNotePopup(final int position) {
        dialog = new Dialog(NotesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_layout);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Creating email model to send

        final EditText note_edt = dialog.findViewById(R.id.update_dialog_note);
                note_edt.setText(notes.get(position).getNote());

        dialog.findViewById(R.id.update_note_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                  updateNoteDB(notes.get(position),note_edt.getText().toString());
            }
        });
        dialog.findViewById(R.id.update_note_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                dataBaseHelper.deleteNote(notes.get(position));
                notes.remove(position);
                adapterPush();
            }
        });

        dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);


        menu.findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(NotesActivity.this, MainActivity.class));
                finish();
                UserPreferences.getInstance(NotesActivity.this).removeUser();
                return false;
            }
        });
        menu.findItem(R.id.profile_btn).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(NotesActivity.this, ProfileActivity.class));
                return false;
            }
        });

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (notes.size() > 0) {

            searchList= new ArrayList<>();
            int i;
            for( i = 0; i < notes.size(); i++) {
                if (notes.get(i).getNote().toLowerCase().contains(s.toLowerCase())) {
                    searchList.add(notes.get(i));
                }

            }
            if (searchList.size() > 0) {
                notesAdapter = new NotesAdapter(true,i-1,searchList, getApplicationContext(), this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(notesAdapter);
            }

        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}
