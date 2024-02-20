package br.feevale.estudantefirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.content.Intent;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENTS_KEY = "students";
    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference root     = database.getReference();
    DatabaseReference students = root.child(STUDENTS_KEY);
    FirebaseListAdapter<Student> listAdapter;
    ListView listStudent;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        listStudent = findViewById(R.id.listStudent);

        FirebaseListOptions<Student> options = new FirebaseListOptions.Builder<Student>()
                .setQuery(students, Student.class)
                .setLayout(R.layout.student_list_item)
                .build();

        listAdapter = new FirebaseListAdapter<Student>(options) {
            @Override
            protected void populateView(View v, Student model, int position) {
                TextView txtStudentName = v.findViewById(R.id.txtStudentName);
                ImageView imageItem = v.findViewById(R.id.imageItem);
                txtStudentName.setText(model.getName());
                if(model.getImage() != null){
                    byte imageData[] = Base64.decode(model.getImage(), Base64.DEFAULT);
                    Bitmap img = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    imageItem.setImageBitmap(img);
                }
            }
        };
        listStudent.setAdapter(listAdapter);
        listStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if(mAuth.getCurrentUser() == null){
                    Intent it = new Intent(getBaseContext(), UserAuth.class);
                    startActivity(it);
                }else{
                    DatabaseReference item = listAdapter.getRef(position);
                    item.removeValue();
                }

                return false;
            }
        });

        listStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                DatabaseReference item = listAdapter.getRef(position);
                changeToUpdate(item.getKey(), listAdapter.getItem(position));
            }
        });
        listAdapter.startListening();
    }

    public void changeToAdd(View v){
        Intent it = new Intent(getBaseContext(), AddStudent.class);
        startActivity(it);
    }

    public void changeToUpdate(String key, Student s){
        Intent it = new Intent(getBaseContext(), EditStudent.class);
        it.putExtra("KEY", key);
        it.putExtra("STD", s);
        startActivity(it);
    }
}
