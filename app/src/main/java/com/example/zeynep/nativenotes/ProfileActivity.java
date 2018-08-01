package com.example.zeynep.nativenotes;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
    DataBaseHelper dataBaseHelper;
    EditText edtName, edtPass;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = UserPreferences.getInstance(this).readUser();
        dataBaseHelper = new DataBaseHelper(this);

        uname_txt = findViewById(R.id.uname_txt);
        uname_txt.setText("default");
        profile_img = findViewById(R.id.profile_img);
        profile_img.setOnClickListener(this);
        edtName = findViewById(R.id.profile_uname);
        edtPass = findViewById(R.id.profile_password);
        findViewById(R.id.profile_save_btn).setOnClickListener(this);

        uname_txt.setText(user.getUserName());
        if (user.getName() != null) edtName.setText(user.getName());
        if (user.getImg() != null){
            File imgFile = new  File(user.getImg());

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                profile_img.setImageBitmap(myBitmap);
            }
        }
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

                if (pictureFilePath != null)user.setImg(pictureFilePath);
                if (edtName.getText() != null)user.setName(edtName.getText().toString());
                if (edtPass.getText() != null)user.setPassword(edtPass.getText().toString());
                dataBaseHelper.updateUser(user);
                UserPreferences.getInstance(this).saveUser(user);
                startActivity(new Intent(this, NotesActivity.class));
                finish();
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
}
