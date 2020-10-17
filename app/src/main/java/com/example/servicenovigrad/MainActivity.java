package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Account currentUser;
    private Button loginButton;
    private Button signupButton;
    private EditText motDePasse;
    private EditText utilisateur;
    private TextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        
        motDePasse = (EditText) findViewById(R.id.password);
        utilisateur = (EditText) findViewById(R.id.username);
        loginButton = (Button) findViewById(R.id.login);
        signupButton = (Button) findViewById(R.id.signup);
        errors = (TextView) findViewById(R.id.errorMessages);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check valid login
                if(checkValidLogin()) {
                    openWelcomePage();
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

        utilisateur.setOnClickListener(new View.OnClickListener() {
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

        utilisateur.addTextChangedListener(new TextWatcher() {
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
    }

  @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser fUser = mAuth.getCurrentUser();
//        if (fUser != null) {
//            Log.d(TAG, fUser.getDisplayName());
//        }

        handleSignIn("test@gmail.com", "abcdef12345");
//        handleSignUp("John Doe", "test@gmail.com", "abcdef12345", AccountType.EMPLOYEE);

    }

    /* Use this method ONLY for sign up!
    * */
    public Account accountFromType(String userName, String email, String uid, AccountType accountType) {
        switch (accountType.ordinal()) {
            case 0:
                return new AdminAccount(userName, email, uid);
            case 1:
                return new EmployeeAccount(userName, email, uid);
            case 2:
                return new ClientAccount(userName, email, uid);
            default:
                return null;
        }
    }

    public void handleSignUp(final String userName, final String email, String password, final AccountType accountType) {
        // check if email and password are valid

        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser fUser = mAuth.getCurrentUser();

                    // Change display name of firebase user
                    UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                    request = request.setDisplayName(userName);
                    UserProfileChangeRequest displayNameRequest = request.build();
                    fUser.updateProfile(displayNameRequest);

                    // Get instance
                    currentUser = accountFromType(userName, email, fUser.getUid(), accountType);
                    Log.d(TAG, currentUser.toString());

                    // Save user data to Firestore
                    currentUser.saveAccountToFirestore(FieldValue.serverTimestamp());

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "createUserWithEmail:failure");
                }
            }
        });
    }

    public void handleSignIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    final FirebaseUser fUser = mAuth.getCurrentUser();

                    // Get instance
                    db.document(Account.firestoreUsersRoute + fUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                if (document.exists()) {
                                    String role = (String) document.get("role");

                                    switch (role) {
                                        case "admin":
                                            currentUser = new AdminAccount(fUser.getDisplayName(), fUser.getEmail(), fUser.getUid());
                                            break;
                                        case "employee":
                                            currentUser = new EmployeeAccount(fUser.getDisplayName(), fUser.getEmail(), fUser.getUid());
                                            break;
                                        case "client":
                                            currentUser = new ClientAccount(fUser.getDisplayName(), fUser.getEmail(), fUser.getUid());
                                            break;
                                        default:
                                            handleSignOut();
                                            break;
                                    }

                                // failed to find user in firestore
                                } else {
                                    Log.d(TAG, "No such document");
                                    handleSignOut();
                                }

                                Log.d(TAG, currentUser.toString());

                            // async task failed
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                                handleSignOut();
                            }
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "signInWithEmail:failure");
                }

    public void handleSignOut() {
        mAuth.signOut();
        currentUser = null;
    }
    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpPage2.class);
        startActivity(intent);
    }

    public void loginError(){
        motDePasse.setText("");
        utilisateur.setText("");
        errors.setText("you have entered invalid credentials");
    }

    public boolean checkValidLogin(){
        String utilisateurS = utilisateur.getText().toString();
        String passeS = motDePasse.getText().toString();
        String utilisateurCorrect="admin";
        String passeCorrect="123admin456";
        boolean validUser=utilisateurS.equals(utilisateurCorrect);
        boolean validPass=passeS.equals(passeCorrect);
        return validUser && validPass;
    }

}