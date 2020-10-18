package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
//import java.lang.Object.javax.mail.internet.InternetAddress;
//import org.apache.commons.validator.routines.EmailValidator;


public class SignUpPage2 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "[CONSOLE]";
    private Account currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Account account;
    private Button createaccountButton;
    private EditText prenom;
    private EditText nom;
    private EditText courriel;
    private EditText utilisateur;
    private EditText motDePasse;
    private EditText confirmationMotDePasse;
    private TextView errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page2);
        mAuth = FirebaseAuth.getInstance();

        createaccountButton = (Button) findViewById(R.id.createaccount);

        prenom = (EditText) findViewById(R.id.firstname);
        nom = (EditText) findViewById(R.id.lastname);
        courriel = (EditText) findViewById(R.id.email);
        utilisateur = (EditText) findViewById(R.id.username);
        motDePasse = (EditText) findViewById(R.id.password);
        confirmationMotDePasse = (EditText) findViewById(R.id.confirmpassword);
        errors = (TextView) findViewById(R.id.errorMessages);

        createaccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidSignUp()) {
                    try {
                        int var = Integer.parseInt(utilisateur.getText().toString());
                        if(var<1000000000 && var>99999999) {
                            handleSignUp(Integer.toString(var), courriel.getText().toString(), motDePasse.getText().toString(), AccountType.EMPLOYEE);
                        }else{
                            throw new NumberFormatException();
                        }
                    }
                    catch (NumberFormatException e) {
                        // it was not a number
                        //create client
                        handleSignUp(utilisateur.getText().toString(), courriel.getText().toString(), motDePasse.getText().toString(), AccountType.CLIENT);
                    }
                    openWelcomePage(/*new Account type param*/);
                }else{
                    createError();
                }
            }
        });

    }

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

                            // update ui
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
                                        // update ui

                                    } else {
                                        // async task failed
                                        Log.d(TAG, "get failed with ", task.getException());
                                        handleSignOut();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure");
                        }
                    }
                });
    }

    public void handleSignOut() {
        mAuth.signOut();
        currentUser = null;
    }

    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    public boolean checkValidSignUp(){
        boolean prenomValide = !prenom.getText().toString().isEmpty() /*&& only contains alphabet*/;
        boolean nomValide = !nom.getText().toString().isEmpty() /*&& only contains alphabet*/;
        boolean utilisateurValide = !utilisateur.getText().toString().isEmpty() /*&& not already a user*/;
        boolean courrielValide = !courriel.getText().toString().isEmpty() && isValidEmail(courriel.getText().toString());
        boolean passeValide = !motDePasse.getText().toString().isEmpty();
        boolean confirmationValide = confirmationMotDePasse.getText().toString().equals(motDePasse.getText().toString());
        return prenomValide && nomValide && utilisateurValide && courrielValide && passeValide && confirmationValide;
    }

    public void createError(){
        errors.setText("You have entered invalid credentials");
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}