package com.company.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_screen extends AppCompatActivity {


	ImageView image;
	TextView title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		image = findViewById(R.id.imageViewsplash);
		title = findViewById(R.id.textViewsplash);

		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
		title.startAnimation(animation);

		Handler handler = new Handler();
		new Handler().postDelayed(new Runnable()
		{
			@Override
			 public void run()
			 {

				 Intent i = new Intent(Splash_screen.this, login_page.class);
				 startActivity(i);
			  finish();

			 }
		 }, 5000);
	
	}
}