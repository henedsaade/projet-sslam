package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "[CONSOLE]";
    private Button loginButton;
    private Button signupButton;
    private EditText motDePasse;
    private EditText addresse_courriel;
    private TextView errors;
    private FbWrapper fb = FbWrapper.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        motDePasse = (EditText) findViewById(R.id.password);
        addresse_courriel = (EditText) findViewById(R.id.courriel);
        loginButton = (Button) findViewById(R.id.login);
        signupButton = (Button) findViewById(R.id.signup);
        errors = (TextView) findViewById(R.id.errorMessages);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check valid login
                if(!addresse_courriel.getText().toString().equals("") && !motDePasse.getText().toString().equals("")) {
                    fb.handleSignIn(addresse_courriel.getText().toString(), motDePasse.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fb.initiateUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            openWelcomePage();
                                        } else {
                                            fb.handleSignOut();
                                            loginError();
                                        }
                                    }
                                });
                            } else {
                                loginError();
                            }
                        }
                    });
                }else{
                    loginError();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });

        motDePasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errors.setText("");
            }
        });

        addresse_courriel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errors.setText("");
            }
        });
        motDePasse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //requires no code
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //requires no code
            }

            @Override
            public void afterTextChanged(Editable s) {
                errors.setText("");
            }
        });

        addresse_courriel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //requires no code
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //requires no code
            }

            @Override
            public void afterTextChanged(Editable s) {
                errors.setText("");
            }
        });

        if (fb.isUserLoggedIn()) {
            fb.initiateUser().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        openWelcomePage();
                    } else {
                        fb.handleSignOut();
                    }
                }
            });
        }
    }

    //
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
////        FirebaseUser fUser = mAuth.getCurrentUser();
////        if (fUser != null) {
////            Log.d(TAG, fUser.getDisplayName());
////        }
//
////        handleSignIn("test123@gmail.com", "abcdef12345");
////        handleSignUp("Bob Doe", "test123@gmail.com", "abcdef12345", AccountType.CLIENT);
//
//    }




    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpPage2.class);
        startActivity(intent);
    }

    public void loginError() {
        motDePasse.setText("");
        addresse_courriel.setText("");
        errors.setText("Vous avez entré la mauvaise information");
    }

//    public boolean checkValidLogin() {
////        String utilisateurS = utilisateur.getText().toString();
////        String passeS = motDePasse.getText().toString();
////        String utilisateurCorrect="admin";
////        String passeCorrect="123admin456";
////        boolean validUser=utilisateurS.equals(utilisateurCorrect);
////        boolean validPass=passeS.equals(passeCorrect);
////        return validUser && validPass;
//    }

}