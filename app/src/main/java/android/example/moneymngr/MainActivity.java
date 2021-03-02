package android.example.moneymngr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
* @version 1.0.0
* @author Yurii Horobets(techyyy)
* Entry activity. Shows log-in window with buttons to switch to registration or to reset activities.
* */

public class MainActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgotPass;
    private TextView mSignUpHere;

    private ProgressDialog mDialog;

    private FirebaseAuth mAuth;

    /**
     * OnCreate operations.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        loginDetails();

    }

    /**
        Collecting log-in data.
        Sends authentication request to Firebase after pressing btnLogin and having collected all the data,
        otherwise log-in error is being showed.
    */

    private void loginDetails(){
        mEmail = findViewById(R.id.login_email);
        mPass = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.signin_button);
        mForgotPass = findViewById(R.id.forgot_pass);
        mSignUpHere = findViewById(R.id.signup_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email field is required.");
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Password field is required.");
                    return;
                }

                mDialog.setMessage("Processing...");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Log-in successful!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Log-in failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        // Switch to registration

        mSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        // Switch to resetting

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
            }
        });
    }
}