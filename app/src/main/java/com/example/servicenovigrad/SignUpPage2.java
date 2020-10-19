package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Parcelable;
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
    private static final String TAG = "[CONSOLE]";
    private Button createaccountButton;
    private EditText prenom;
    private EditText nom;
    private EditText courriel;
    private EditText utilisateur;
    private EditText motDePasse;
    private EditText confirmationMotDePasse;
    private TextView errors;
    private FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page2);

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
                            fb.handleSignUp(Integer.toString(var), courriel.getText().toString(), motDePasse.getText().toString(), AccountType.EMPLOYEE).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    openWelcomePage();
                                }
                            });
                        }else{
                            throw new NumberFormatException();
                        }
                    }
                    catch (NumberFormatException e) {
                        // it was not a number
                        //create client
                        fb.handleSignUp(utilisateur.getText().toString(), courriel.getText().toString(), motDePasse.getText().toString(), AccountType.CLIENT).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                openWelcomePage();
                            }
                        });
                    }
                }else{
                    createError();
                }
            }
        });

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