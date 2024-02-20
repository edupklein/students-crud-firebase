package br.feevale.estudantefirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class AddStudent extends AppCompatActivity {
    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference root     = database.getReference();
    DatabaseReference students = root.child(MainActivity.STUDENTS_KEY);
    EditText txtName, txtCourse, txtEmail;
    public static final int CAMERA_CALL  = 1022;
    Bitmap bmp;
    ImageView image;
    Boolean hasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        txtName = findViewById(R.id.txtName);
        txtCourse = findViewById(R.id.txtCourse);
        txtEmail = findViewById(R.id.txtEmail);
        image = findViewById(R.id.image);
    }

    public void addStudent(View v){
        Student s = new Student();
        s.setName(txtName.getText().toString());
        s.setCourse(txtCourse.getText().toString());
        s.setEmail(txtEmail.getText().toString());

        if(hasImage){
            String bmpEncoded = loadImage();
            hasImage = false;
            s.setImage(bmpEncoded);
        }

        String key = students.push().getKey();
        students.child(key).setValue(s);
        finish();
    }

    public String loadImage(){
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteOut);
        return Base64.encodeToString(byteOut.toByteArray(), Base64.DEFAULT);
    }

    public void takePicture(View v){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_CALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CALL && resultCode == RESULT_OK) {
            bmp = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bmp);
            hasImage = true;
        }
    }
}
