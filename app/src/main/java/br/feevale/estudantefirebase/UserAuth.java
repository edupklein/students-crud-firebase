package br.feevale.estudantefirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuth extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView txtUser, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);

        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        mAuth = FirebaseAuth.getInstance();
    }

    public void logIn(View v){
        authenticateUser(txtUser.getText().toString(), txtPass.getText().toString());
    }

    public void signIn(View v){
        createUser(txtUser.getText().toString(), txtPass.getText().toString());
    }

    public void authenticateUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            finish();
                        } else {
                            Toast.makeText(UserAuth.this, "Erro ao Logar!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            finish();
                        } else {
                            Toast.makeText(UserAuth.this, "Erro ao Criar!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
