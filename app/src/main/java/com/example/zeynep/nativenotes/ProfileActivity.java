package com.example.zeynep.nativenotes;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView uname_txt;
    ImageView profile_img;
    List<String> username_list_db = new ArrayList<>();
    List<String> uname_list_db = new ArrayList<>();
    List<String> userpassword_list_db = new ArrayList<>();
    List<String> user_id_list_db = new ArrayList<>();

    List<String> userimg_list_db = new ArrayList<>();
    private String pictureFilePath;
    static final int REQUEST_PICTURE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        readFromDB();
        uname_txt = findViewById(R.id.uname_txt);
        uname_txt.setText("default");
        profile_img = findViewById(R.id.profile_img);
        profile_img.setOnClickListener(this);

    }
    public List<String> readFromDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ProfileActivity.this);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        try {
            String[] stunlar = {DataBaseHelper.USER_COLUMN_ID, DataBaseHelper.USER_NAME, DataBaseHelper.U_NAME,DataBaseHelper.USER_PASSWORD,DataBaseHelper.USER_IMG,DataBaseHelper.USER_NOTE_ID};
            Cursor cursor = db.query(DataBaseHelper.USER_TABLE_NAME, stunlar, null, null, null, null, null);
            while (cursor.moveToNext()) {
                user_id_list_db.add(cursor.getString(0));
                username_list_db.add(cursor.getString(1));
                uname_list_db.add(cursor.getString(2));
                userpassword_list_db.add(cursor.getString(3));

            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return username_list_db;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_img:
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    takePicture();
                }
                break;
                case R.id.profile_save_btn:
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    takePicture();
                }
                break;
        }
    }
    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {


            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.example.zeynep.nativenotes", pictureFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                } catch (Exception e) {
                    Toast.makeText(this,
                            e.toString(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ZOFTINO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists())            {
                profile_img.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }

    public int updateNoteDB(String userId, String noteId) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int count = 0;
        try {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.USER_NOTE_ID, noteId);
            count = db.update(DataBaseHelper.USER_TABLE_NAME, cv, DataBaseHelper.USER_COLUMN_ID+" = ?",new String[]{userId});


        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        db.close();
        readFromDB();
        //showNotes(note_list_db);
        return  count;


    }
}
