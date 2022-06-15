package com.company.quizme;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class login_page extends AppCompatActivity {

	EditText mail;
	EditText password;
	Button signIn;
	SignInButton signInGoogle;
	TextView signUp;
	TextView forgotPassword;
	ProgressBar progressBarSignIn;

	GoogleSignInClient googleSignInClient;

	FirebaseAuth auth = FirebaseAuth.getInstance();

	ActivityResultLauncher<Intent> activityResultLauncher;

	//GoogleSignInAccount

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		//register
		registerActivityForGoogleSignIn();

		mail = findViewById(R.id.editTextLoginEmail);
		password = findViewById(R.id.editTextLoginPassword);
		signIn = findViewById(R.id.buttonloginSignIn);
		signInGoogle = findViewById(R.id.buttonLoginGoogleSignIn);
		signUp = findViewById(R.id.textViewLoginSignUp);
		forgotPassword = findViewById(R.id.textViewLoginForgotPassword);
		progressBarSignIn = findViewById(R.id.progressBarSignIn);

		auth = FirebaseAuth.getInstance();
		if (auth.getCurrentUser() != null){
			//user is logged in
		}


		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String userEmail = mail.getText().toString().trim();
				String userPassword = password.getText().toString().trim();

				if(TextUtils.isEmpty(userEmail)){
					mail.setError("Email is Required.");
					return;
				}

				if(TextUtils.isEmpty(userPassword)){
					password.setError("Password is Required.");
					return;
				}

				if(password.length() < 6){
					password.setError("Incorrect password!!!");
					return;
					//onRestart(signIn.setOnClickListener());
				}

				progressBarSignIn.setVisibility(View.VISIBLE);


				signInWithFirebase(userEmail,userPassword);

			}
		});

		signInGoogle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				signinGoogle();

			}
		});

		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String userEmail = mail.getText().toString().trim();
				String userPassword = password.getText().toString().trim();

				Intent i = new Intent ( login_page.this, Sign_Up_Page.class);
				startActivity(i);
				Toast.makeText(login_page.this, "Enter valid Email and Password!!", Toast.LENGTH_SHORT).show();

				if(TextUtils.isEmpty(userEmail)){
					mail.setError("Email is Required.");
					return;
				}

				if(TextUtils.isEmpty(userPassword)){
					password.setError("Password is Required.");
					return;
				}

				if(password.length() < 6){
					password.setError("Password should consist at least  6 Characters");
					return;
				}

			}
		});

		forgotPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(login_page.this,Forgot_Password.class);
				startActivity(i);

			}
		});
	}

	public void signinGoogle(){

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
		.requestIdToken("61239220437-flq35sgvviip6uf7ej8t56mr69nu8npu.apps.googleusercontent.com")
				.requestEmail().build();

		googleSignInClient = GoogleSignIn.getClient(this,gso);


        //googleSignInClient = g

		signIn();
	}

	public void signIn(){

		Intent signInIntent = googleSignInClient.getSignInIntent();
		activityResultLauncher.launch(signInIntent);


	}

	public void registerActivityForGoogleSignIn(){

		activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
				new ActivityResultCallback<ActivityResult>() {
					@Override
					public void onActivityResult(ActivityResult result) {

						int resultCode = result.getResultCode();
						Intent data = result.getData();

						if (resultCode==RESULT_OK && data != null)
						{
							Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
							firebaseSignInWithGoogle(task);
						}

					}
				});

	}

	private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task) {


		try {
			GoogleSignInAccount account = task.getResult(ApiException.class);
			Toast.makeText(this, "Successfully logged in!!", Toast.LENGTH_LONG).show();
			Intent i = new Intent(login_page.this, MainActivity.class);
			startActivity(i);
			finish();
			firebaseGoogleAccount(account);
		}
		catch (ApiException e)
		{
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private void firebaseGoogleAccount(GoogleSignInAccount account) {
		AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
		auth.signInWithCredential(authCredential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {

						if (task.isSuccessful()) {
							FirebaseUser user = auth.getCurrentUser();
							user.getEmail();
						}
						else
						{

						}


					}
				});

	}





	public void signInWithFirebase(String userEmail, String userPassword)
	{
		progressBarSignIn.setVisibility(View.VISIBLE);
		signIn.setClickable(false);

		auth.signInWithEmailAndPassword(userEmail, userPassword)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {

					if(task.isSuccessful())
						{
							Intent i= new Intent(login_page.this,MainActivity.class);
							startActivity(i);
							finish();
							progressBarSignIn.setVisibility(View.INVISIBLE);
							Toast.makeText(login_page.this, "Sign In is Successful"
									, Toast.LENGTH_SHORT).show();

						}
						else
						{
							progressBarSignIn.setVisibility(View.INVISIBLE);
							Toast.makeText(login_page.this, "Sign In is not successful, please try again!!"
											+ task.getException().getMessage(), Toast.LENGTH_LONG).show();

							progressBarSignIn.setVisibility(View.INVISIBLE);
							//onRestart(signIn.setOnClickListener());
						}
					}
				});

	}
}