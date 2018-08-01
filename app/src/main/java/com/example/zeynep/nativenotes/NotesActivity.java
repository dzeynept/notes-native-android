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
    List<String> note_list = new ArrayList<>();
    List<String> note_list_db = new ArrayList<>();
    List<String> note_id_list_db = new ArrayList<>();
    NotesAdapter notesAdapter;
    private Dialog dialog;
    Button profile_btn;
    NotesModel notes;
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
        profile_btn = findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(this);
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
            case R.id.profile_btn :
                startActivity(new Intent(NotesActivity.this, ProfileActivity.class));
                break;
        }
    }

    public void saveToDB(String note) {

        NotesModel notesModel = new NotesModel(note,String.valueOf(user.getID()));
        dataBaseHelper.addNote(notesModel);
        startActivity(getIntent());

//        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
//        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(DataBaseHelper.NOTES_COLUMN_NOTES, note);
//            cv.put(DataBaseHelper.NOTES_USER_ID_COLUMN_NOTES, "1qqq");
//            db.insert(DataBaseHelper.NOTES_TABLE_NAME, null, cv);
//
//
//          //  count = db.update(DataBaseHelper.USER_NOTE_ID, cvuser, DataBaseHelper.NOTES_COLUMN_ID+" = ?",new String[]{id});
//
//        } catch (Exception e) {
//            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        db.close();
//
    }

    public void deleteFromDB(String id) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        try {

          //  db.execSQL("DELETE FROM "+DataBaseHelper.NOTES_TABLE_NAME+" WHERE _id="+id);
            db.delete(DataBaseHelper.NOTES_TABLE_NAME, DataBaseHelper.NOTES_COLUMN_ID + "=?" , new String[]{id}                              );
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
            db.close();
        startActivity(getIntent());
    }

    public int updateNoteDB(String id, String note) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int count = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.NOTES_COLUMN_NOTES, note);
            count = db.update(DataBaseHelper.NOTES_TABLE_NAME, cv, DataBaseHelper.NOTES_COLUMN_ID+" = ?",new String[]{id});


        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
        startActivity(getIntent());
        return  count;
    }


    public List<String> readFromDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(NotesActivity.this);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        try {
            /*String[] note_cloumn = {DataBaseHelper.NOTES_COLUMN_ID, DataBaseHelper.NOTES_COLUMN_NOTES};
            Cursor cursor = db.query(DataBaseHelper.NOTES_TABLE_NAME, note_cloumn, null, null, null, null, null);
            while (cursor.moveToNext()) {
                note_id_list_db.add(cursor.getString(0));
                note_list_db.add(cursor.getString(1));
            }*/

            adapterPush();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return note_list_db;
    }


    public void adapterPush() {
      /*  for (int i = 0; i < note_list_db.size(); i++) {
            note_list.add(note_list_db.get(i));
        }*/
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        UserModel userModel = UserPreferences.getInstance(this).readUser();
        notesAdapter = new NotesAdapter(dataBaseHelper.getAllNotes(userModel.getID()), getApplicationContext(), this);
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


        dialog.findViewById(R.id.update_note_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                  updateNoteDB(note_id_list_db.get(position),note_edt.getText().toString());
            }
        });
        dialog.findViewById(R.id.update_note_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
              //  deleteFromDB();
                try {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                    SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                    deleteFromDB(note_id_list_db.get(position));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
       // searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        return true;
    }
}
