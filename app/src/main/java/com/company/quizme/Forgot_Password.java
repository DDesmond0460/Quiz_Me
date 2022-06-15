package com.company.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

	EditText mail;
	Button button;
	ProgressBar progressBar;
	FirebaseAuth auth = FirebaseAuth.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot__password);

		mail = findViewById(R.id.editTextEmail);
		button = findViewById(R.id.buttonPasswordContinue);
		progressBar = findViewById(R.id.progressBarForgotPassword);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String userEmail = mail.getText().toString();
				resetPassword(userEmail);

			}
		});

	}

	public void resetPassword(String userEmail)
	{
		progressBar.setVisibility(View.VISIBLE);
		auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {

				if (task.isSuccessful())
				{
					Toast.makeText(Forgot_Password.this, "Hi, To reset your password, a mail has been sent to your E-mail please check!!"
							, Toast.LENGTH_LONG).show();
					button.setClickable(false);
					progressBar.setVisibility(View.VISIBLE);
					finish();
				}
				else
				{
					Toast.makeText(Forgot_Password.this, "Sorry, there is a problem, please try again!!"
							, Toast.LENGTH_LONG).show();

				}
			}
		});

	}
}