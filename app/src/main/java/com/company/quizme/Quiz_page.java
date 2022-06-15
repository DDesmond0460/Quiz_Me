package com.company.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class Quiz_page extends AppCompatActivity {

	TextView time, correct, wrong;
	TextView question, a, b, c, d;
	Button next, finish;

	FirebaseDatabase database = FirebaseDatabase.getInstance();
	DatabaseReference databaseReference = database.getReference().child("Cloud_computing");

	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	DatabaseReference databaseReferenceSecond = database.getReference();


	String quizQuestion;
	String quizAnswerA;
	String quizAnswerB;
	String quizAnswerC;
	String quizAnswerD;
	String quizCorrectAnswer;
	int questionCount;
	int questionNumber = 1;

	String userAnswer;

	int userCorrect = 0;
	int userWrong = 0;

	CountDownTimer countDownTimer;
	private static final long TOTAL_TIME = 61000;
	Boolean timerContinue;
	long leftTime = TOTAL_TIME;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_page);

		time = findViewById(R.id.textViewTime);
		correct = findViewById(R.id.textViewCorrect);
		wrong = findViewById(R.id.textViewWrong);

		question = findViewById(R.id.textViewQuestion);
		a = findViewById(R.id.textViewA);
		b = findViewById(R.id.textViewB);
		c = findViewById(R.id.textViewC);
		d = findViewById(R.id.textViewD);

		next = findViewById(R.id.buttonNext);
		finish = findViewById(R.id.buttonFinish);

		game();

		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				a.setClickable(true);
				b.setClickable(true);
				c.setClickable(true);
				d.setClickable(true);

				resetTimer();
				game();

			}
		});

		finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				sendScore();
				Intent i = new Intent(Quiz_page.this, Score_page.class);
				startActivity(i);

				if( questionNumber < questionCount )
				{
					Toast.makeText(Quiz_page.this, "You have not finished your assessment completely!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(Quiz_page.this, "You have successfully answered all the questions!!!", Toast.LENGTH_SHORT).show();

				}

				finish();

			}
		});

		a.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				a.setClickable(false);

				pauseTimer();

			userAnswer = "a";

			if(quizCorrectAnswer.equals(userAnswer))
				{
					a.setBackgroundColor(Color.GREEN);
					userCorrect++;
					correct.setText("" + userCorrect);
					Toast.makeText(Quiz_page.this, "Correct answer!!!", Toast.LENGTH_SHORT).show();

				}
				else
				{
					a.setBackgroundColor(Color.RED);
					userWrong++;
					wrong.setText("" + userWrong);
					Toast.makeText(Quiz_page.this, "Wrong answer!!!", Toast.LENGTH_SHORT).show();
					findAnswer();
				}

				//Toast.makeText(Quiz_page.this, "value is set as true", Toast.LENGTH_SHORT).show();

			}
		});
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				b.setClickable(false);

				pauseTimer();

				userAnswer = "b";

				if(quizCorrectAnswer.equals(userAnswer))
				{
					b.setBackgroundColor(Color.GREEN);
					userCorrect++;
					correct.setText("" + userCorrect);
					Toast.makeText(Quiz_page.this, "Correct answer!!!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					b.setBackgroundColor(Color.RED);
					userWrong++;
					wrong.setText("" + userWrong);
					Toast.makeText(Quiz_page.this, "Wrong answer!!!", Toast.LENGTH_SHORT).show();
					findAnswer();
				}
				//Toast.makeText(Quiz_page.this, "value is set as true", Toast.LENGTH_SHORT).show();

			}

		});
		c.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				c.setClickable(false);

				pauseTimer();

				userAnswer = "c";

				if(quizCorrectAnswer.equals(userAnswer))
				{
					c.setBackgroundColor(Color.GREEN);
					userCorrect++;
					correct.setText("" + userCorrect);
					Toast.makeText(Quiz_page.this, "Correct answer!!!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					c.setBackgroundColor(Color.RED);
					userWrong++;
					wrong.setText("" + userWrong);
					Toast.makeText(Quiz_page.this, "Wrong answer!!!", Toast.LENGTH_SHORT).show();
					findAnswer();
				}

			}
		});
		d.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				d.setClickable(false);

				pauseTimer();

				userAnswer = "d";

				if(quizCorrectAnswer.equals(userAnswer))
				{
					d.setBackgroundColor(Color.GREEN);
					userCorrect++;
					correct.setText("" + userCorrect);
					Toast.makeText(Quiz_page.this, "Correct answer!!!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					d.setBackgroundColor(Color.RED);
					userWrong++;
					wrong.setText("" + userWrong);
					Toast.makeText(Quiz_page.this, "Wrong answer!!!", Toast.LENGTH_SHORT).show();
					findAnswer();
				}

			}
		});

	}

	public void game()
	{

		startTimer();

		a.setBackgroundColor(Color.WHITE);
		b.setBackgroundColor(Color.WHITE);
		c.setBackgroundColor(Color.WHITE);
		d.setBackgroundColor(Color.WHITE);


		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				// This method is called once with the initial value and again
				// whenever data at this location is updated artha aytha eega!!!!!

				questionCount = (int) dataSnapshot.getChildrenCount();

				quizQuestion = dataSnapshot.child(String.valueOf(questionNumber))
						.child("q").getValue().toString();
				quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber))
						.child("a").getValue().toString();
				quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber))
						.child("b").getValue().toString();
				quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber))
						.child("c").getValue().toString();
				quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber))
						.child("d").getValue().toString();
				quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber))
						.child("answer").getValue().toString();

				question.setText(quizQuestion);
				a.setText(quizAnswerA);
				b.setText(quizAnswerB);
				c.setText(quizAnswerC);
				d.setText(quizAnswerD);

				if (questionNumber < questionCount)
				{
					questionNumber++;
				}
				else
				{
					Toast.makeText(Quiz_page.this, "This is the last questions!!!"
							, Toast.LENGTH_SHORT).show();
				}


			}

			@Override
			public void onCancelled(DatabaseError error) {
				// Failed to read value
				Toast.makeText(Quiz_page.this, "There is an error!!!"
						, Toast.LENGTH_SHORT).show();

			}
		});

	}

	public void findAnswer()
	{
		if(quizCorrectAnswer.equals("a"))
		{
			a.setBackgroundColor(Color.GREEN);
		}
		else if(quizCorrectAnswer.equals("b"))
		{
			b.setBackgroundColor(Color.GREEN);
		}
		else if(quizCorrectAnswer.equals("c"))
		{
			c.setBackgroundColor(Color.GREEN);
		}
		else if(quizCorrectAnswer.equals("d"))
		{
			d.setBackgroundColor(Color.GREEN);
		}

	}

	public void startTimer()
	{
		countDownTimer = new CountDownTimer(leftTime,1000) {
			@Override
			public void onTick(long millisUntilFinished) {

				leftTime = millisUntilFinished;
				updateCountDownText();

			}

			@Override
			public void onFinish() {

				timerContinue = false;
				pauseTimer();
				question.setText("Sorry, Time is up!!!");

			}

		}.start();

		timerContinue = true;

	}

	public void resetTimer()
	{
		leftTime = TOTAL_TIME;
		updateCountDownText();
	}

	public  void updateCountDownText()
	{
		int second = (int) ((leftTime/1000)%60);
		time.setText("" + second);
	}

	public void pauseTimer()
	{
		countDownTimer.cancel();
		timerContinue = false;
	}

	public void sendScore()
	{

		String userUID = user.getUid();
		databaseReferenceSecond.child("scores").child(userUID).child("correct").setValue(userCorrect)
		.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {

				Toast.makeText(Quiz_page.this, "Marks are stored!!!", Toast.LENGTH_SHORT).show();
				
			}
		});
		databaseReferenceSecond.child("scores").child(userUID).child("wrong").setValue(userWrong);

	}

}






