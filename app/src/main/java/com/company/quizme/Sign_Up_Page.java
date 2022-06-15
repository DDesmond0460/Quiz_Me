package com.company.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_Up_Page extends AppCompatActivity {


	EditText mail;
	EditText password;
	Button signUp;
	ProgressBar progressBar;

	FirebaseAuth auth  = FirebaseAuth.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign__up__page);


		mail = findViewById(R.id.editTextSignUpMailAddress);
		password = findViewById(R.id.editTextSignUpPassword);
		signUp = findViewById(R.id.buttonSignUpSign);
		progressBar = findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);

		mail = findViewById(R.id.editTextEmail);
		signUp = findViewById(R.id.buttonSignUpSign);


		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				signUp.setClickable(false);

				String userEmail = mail.getText().toString();
				String userPassword = password.getText().toString();
				signUpFirebase(userEmail, userPassword);

				validateEmailAddress(mail);

			}

		});

	}


		private boolean validateEmailAddress(EditText mail){
			String emailInput = mail.getText().toString();

			if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
				Toast.makeText(this,"Email is valid!!", Toast.LENGTH_SHORT).show();
				return true;
			}
			else
			{
				Toast.makeText(this,"Invalid Email address!!", Toast.LENGTH_SHORT).show();
				return false;
			}
		}


	public void signUpFirebase(String userEmail, String userPassword)
	{
		progressBar.setVisibility(View.VISIBLE);

		auth.createUserWithEmailAndPassword(userEmail, userPassword)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {

						if(TextUtils.isEmpty(userEmail)){
							mail.setError("Email is Required.");
							return;
						}

						if(TextUtils.isEmpty(userPassword)){
							password.setError("Password is Required.");
							return;
						}

						if(password.length() < 6){
							password.setError("Password should at least consist 6 Characters");
							return;
						}

						if(task.isSuccessful())
						{
							Toast.makeText(Sign_Up_Page.this, "Your account is created successfully!!"
								, Toast.LENGTH_LONG).show();
							finish();
						}
						else
						{
							Toast.makeText(Sign_Up_Page.this, "Please try again!!", Toast.LENGTH_LONG).show();

						}
						progressBar.setVisibility(View.INVISIBLE);

					}
				});


	}

}