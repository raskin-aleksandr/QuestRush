package com.example.questrush;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		iv.setImageResource(R.drawable.index);

		ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {

			User.getInstance().setmUser(currentUser);

			Intent intent = new Intent(getApplicationContext(), QuestList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);

		}
	}

	public void login(View v) {

		final Dialog loginDialog = new Dialog(this);
		loginDialog.setContentView(R.layout.login);
		loginDialog.setTitle("Login");

		Button loginButton = (Button) loginDialog.findViewById(R.id.login);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EditText name = (EditText) loginDialog.findViewById(R.id.loginName);
				EditText password = (EditText) loginDialog.findViewById(R.id.loginPassword);

				pd = new ProgressDialog(MainActivity.this);
				pd.setTitle("Login");
				pd.setMessage("Loggin in..");
				pd.show();

				ParseUser.logInInBackground(name.getText().toString(), password.getText().toString(), new LogInCallback() {

					@Override
					public void done(ParseUser arg0, ParseException e) {
						if (e == null) {
							pd.cancel();

							Intent intent = new Intent(getApplicationContext(), QuestList.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);

						} else {
							pd.cancel();
							Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		});
		loginDialog.show();
	}
	
	public void createNew(View v) {
		startActivity(new Intent(getApplicationContext(), SignUp.class));
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Exit Application?");

		alertDialogBuilder.setMessage("Click yes to exit!");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				MainActivity.this.finish();
			}
		});
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}
	
	
	
	
	
	
}
