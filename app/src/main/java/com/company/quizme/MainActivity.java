package com.company.quizme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

	//DrawerLayout drawerLayout;
	//NavigationView navigationView;
	//Toolbar toolbar;

	Button signOut;
	Button startQuiz;
	FirebaseAuth auth = FirebaseAuth.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		//hooks
		//drawerLayout = findViewById(R.id.drawer_layout);
		//drawerLayout = findViewById(R.id.nav_view);
		//drawerLayout = findViewById(R.id.toolbar);

		//navigationView.bringToFront();
		//ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		//drawerLayout.addDrawerListener(toggle);
		//toggle.syncState();

		//navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);


		signOut = findViewById(R.id.buttonSignOut);
		startQuiz = findViewById(R.id.buttonStartQuiz);


		signOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				auth.signOut();
				Intent i = new Intent(MainActivity.this, login_page.class);
				startActivity(i);
				finish();

			}
		});

		startQuiz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(MainActivity.this, Quiz_page.class);
				startActivity(i);


				//game();

			}
		});

	}
}

	/*
	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	 };
	@Override
	public Boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		return true;
	}
	*/


