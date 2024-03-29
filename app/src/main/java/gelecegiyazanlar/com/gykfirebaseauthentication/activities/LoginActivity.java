package gelecegiyazanlar.com.gykfirebaseauthentication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import gelecegiyazanlar.com.gykfirebaseauthentication.R;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmailEt;
    private EditText userPasswordEt;
    private Button registerBtn;
    private Button loginBtn;
    FirebaseAuth mAuth;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String mail = pref.getString("email","");
        String pass = pref.getString("password","");
        String result = pref.getString("result","");
        //if(!mail.equals("") && !pass.equals("") && mail != null && pass != null){
        if(result.equals("1") && result != null && !result.equals("")){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }

        userEmailEt = (EditText) findViewById(R.id.user_email_login_et);
        userPasswordEt = (EditText) findViewById(R.id.user_password_login_et);
        userEmailEt.setText(mail);
        userPasswordEt.setText(pass);

        registerBtn = (Button) findViewById(R.id.button_go_register);
        loginBtn = (Button) findViewById(R.id.button_login);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = userEmailEt.getText().toString().trim();
                String userPassword = userPasswordEt.getText().toString().trim();
                final SharedPreferences.Editor editor = pref.edit();
                editor.putString("result","1");
                editor.putString("email",userEmail);
                editor.putString("password",userPassword);
                editor.apply();
                editor.commit();
                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    login(userEmail, userPassword);
                } else {
                    Toast.makeText(getApplicationContext(), "Email ya da parola boş bırakılamaz!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goToRegister() {
        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("EMail", "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Fail", "signInWithEmail:failure", task.getException());

                        }
                    }
                });
    }
}
