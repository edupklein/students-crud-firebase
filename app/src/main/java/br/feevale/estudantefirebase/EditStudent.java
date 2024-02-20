package br.feevale.estudantefirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditStudent extends AppCompatActivity {
    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference root     = database.getReference();
    DatabaseReference students = root.child(MainActivity.STUDENTS_KEY);

    EditText txtName, txtCourse, txtEmail;
    Student current;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        txtName = findViewById(R.id.txtName);
        txtCourse = findViewById(R.id.txtCourse);
        txtEmail = findViewById(R.id.txtEmail);

        key = getIntent().getStringExtra("KEY");
        current = (Student) getIntent().getSerializableExtra("STD");
        txtName.setText(current.getName());
        txtCourse.setText(current.getCourse());
        txtEmail.setText(current.getEmail());
    }

    public void updateStudent(View v){
        current.setName(txtName.getText().toString());
        current.setCourse(txtCourse.getText().toString());
        current.setEmail(txtEmail.getText().toString());
        students.child(key).setValue(current);

        finish();
    }
}
