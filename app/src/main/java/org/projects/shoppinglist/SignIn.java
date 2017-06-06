package org.projects.shoppinglist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by user on 06-06-2017.
 */

public class SignIn extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "FB_SIGNIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etPass;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        findViewById(R.id.btnCreate).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnSignOut).setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmailAddr);
        etPass = (EditText) findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "Du er logget in som: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "Du er ikke logget på");
                }
            }
        };

        updateStatus();


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnCreate:
                createUserAccount();
                break;

            case R.id.btnSignIn:
                signUserIn();
                break;

            case R.id.btnSignOut:
                signUserOut();
                break;
        }
    }


    private boolean checkFromFields(){
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();


        if(email.isEmpty()){
            etEmail.setError("Email mangler");
            return false;
        }

        if(password.isEmpty()){
            etPass.setError("Mangler kodeord");
            return false;
        }
        return true;
    }

    private boolean checkFormFields() {
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            etPass.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        TextView tvStat = (TextView)findViewById(R.id.tvSignInStatus);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvStat.setText("Signed in: " + user.getEmail());
        }
        else {
            tvStat.setText("Signed Out");
        }
    }



    private void updateStatus(String stat){
        TextView tvStat = (TextView)findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }


    private  void signUserIn(){
        if(!checkFormFields()){
            return;
        }
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignIn.this, "Logger in", Toast.LENGTH_SHORT)
                                            .show();
                                }
                                else {
                                    Toast.makeText(SignIn.this, "Logger in misløkkes", Toast.LENGTH_SHORT)
                                            .show();
                                }

                                updateStatus();
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            updateStatus("Ikke gyldigt kode.");
                        }
                        else if (e instanceof FirebaseAuthInvalidUserException) {
                            updateStatus("Der findes ikke nogen af denne slags email i systemet.");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }

    private void  signUserOut(){
        mAuth.signOut();
        updateStatus();
    }


    private  void createUserAccount(){

        if(!checkFormFields())
        {
            return;
        }

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignIn.this, "Bruger er Opratetet", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(SignIn.this, "Kunne ikke oppratte bruger", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("Emailen findes allerde i vores system");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }
}
