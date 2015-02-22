package com.example.questrush;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

@SuppressLint("DefaultLocale")
public class SignUp extends Activity {

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	public void create(View v) {
		EditText name = (EditText) findViewById(R.id.newName);
		EditText email = (EditText) findViewById(R.id.newEmail);
		EditText pass1 = (EditText) findViewById(R.id.newPassword1);
		EditText pass2 = (EditText) findViewById(R.id.newPassword2);

		if (pass1.getText().toString().equals(pass2.getText().toString())) {

			final ParseUser user = new ParseUser();

			user.setUsername(name.getText().toString().toLowerCase());
			user.setEmail(email.getText().toString());
			user.setPassword(pass1.getText().toString());
			
			pd = new ProgressDialog(this);
			pd.setTitle(getString(R.string.sign_up_new_acc));
			pd.setMessage(getString(R.string.sugn_up_saving));
			pd.show();
			
			user.signUpInBackground(new SignUpCallback() {

				@Override
				public void done(ParseException e) {
					pd.cancel();
					if (e == null) {
						User.getInstance().setmUser(ParseUser.getCurrentUser());
						startActivity(new Intent(getApplicationContext(), QuestList.class));
						Toast.makeText(getApplicationContext(), getString(R.string.sign_up_created), Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
				
			});
		}
		else {
			Toast.makeText(getApplicationContext(), getString(R.string.sign_up_password_do_not_match), Toast.LENGTH_SHORT).show();
		}
	}

    public void cancel (View v){
        finish();
    }
}
