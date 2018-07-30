package com.example.zeynep.nativenotes;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener, NoteInterface {
    RecyclerView recyclerView;
    ImageView addNote;
    List<String> note_list = new ArrayList<>();
    List<String> note_list_db = new ArrayList<>();
    NotesAdapter notesAdapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
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
                //todo: dialog
                showAddNotePopup();
                break;
        }
    }

    public void saveToDB(String note) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.NOTES_COLUMN_NOTES, note);
            db.insert(DataBaseHelper.NOTES_TABLE_NAME, null, cv);
            startActivity(new Intent(NotesActivity.this, NotesActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void deleteFromDB(int id) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();


        try {

            db.execSQL("DELETE FROM "+DataBaseHelper.NOTES_TABLE_NAME+" WHERE _id="+id);
           // db.delete(DataBaseHelper.NOTES_TABLE_NAME, DataBaseHelper.NOTES_COLUMN_ID + "=?" , new String[]{String.valueOf(id)}                              );
            startActivity(new Intent(NotesActivity.this, NotesActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    public int updateNoteDB(String note, int id) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int count = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.NOTES_COLUMN_NOTES, note);
            count = db.update(DataBaseHelper.NOTES_TABLE_NAME, cv, DataBaseHelper.NOTES_COLUMN_ID+" = ?",new String[]{String.valueOf(id)});

            startActivity(new Intent(NotesActivity.this, NotesActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
        return  count;
    }


    public List<String> readFromDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(NotesActivity.this);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        try {
            String[] stunlar = {DataBaseHelper.NOTES_COLUMN_ID, DataBaseHelper.NOTES_COLUMN_NOTES};
            Cursor cursor = db.query(DataBaseHelper.NOTES_TABLE_NAME, stunlar, null, null, null, null, null);
            while (cursor.moveToNext()) {
                note_list_db.add(cursor.getString(1));
            }
            adapterPush();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return note_list_db;
    }


    public void adapterPush() {

        for (int i = 0; i < note_list_db.size(); i++) {
            note_list.add(note_list_db.get(i));
        }
        notesAdapter = new NotesAdapter(note_list, getApplicationContext(), this);
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
                note_edt.setText(note_list_db.get(position));
                //  updateNoteDB()
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
                  //  String[] stunlar = {DataBaseHelper.NOTES_COLUMN_ID, DataBaseHelper.NOTES_COLUMN_NOTES};
                    Cursor row =  db.rawQuery("select *from " + DataBaseHelper.NOTES_TABLE_NAME, new String[]{DataBaseHelper.NOTES_COLUMN_ID});
                  //  Cursor row = db.rawQuery("select *from " + DataBaseHelper.NOTES_TABLE_NAME , stunlar);
                    String _id = row.getString(row.getColumnIndexOrThrow("_id"));
                    deleteFromDB(Integer.parseInt(_id));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

        dialog.show();
    }
}
