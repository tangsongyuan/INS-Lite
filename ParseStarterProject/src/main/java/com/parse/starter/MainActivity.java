/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

  Button button;
  TextView changeTextView;

  EditText usernameEditText;
  EditText passwordEditText;

  public void hideKeyboard(View view) {

    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {
      buttonClicked(view);
    }

    return false;
  }

  public void showUserList() {

    Intent intentFromMainActivity = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intentFromMainActivity);
  }

  public void buttonClicked(View view) {

    final String username = usernameEditText.getText().toString();
    final String password = passwordEditText.getText().toString();

    if (button.getText().toString().equals("sign up")) {

      // check if both username and password are not empty
      if (username == null || username.length() == 0 || password == null || password.length() == 0) {
        Toast.makeText(this, "A username and password are required.", Toast.LENGTH_SHORT).show();
        return;
      }

      // sign up
      ParseUser user = new ParseUser();

      user.setUsername(username);
      user.setPassword(password);
      
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if (e == null) {
            Log.e("signup", "successful");
            showUserList();
          } else {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("signup", "failed " + e.getMessage());

          }
        }
      });

    } else if (button.getText().toString().equals("log in")) {

      ParseUser.logInInBackground(username, password, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if (user != null) {
            Log.e("login", "successful");
            showUserList();
          } else {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("login", "failed " + e.getMessage());
          }
        }
      });
    }
  }

  public void changeButton(View view) {
    changeTextView = (TextView) findViewById(R.id.changeTextView);

    if (button.getText().toString().equals("sign up")) {
      button.setText("log in");
      changeTextView.setText("Or Signup");
    } else if (button.getText().toString().equals("log in")) {
      button.setText("sign up");
      changeTextView.setText("Or Login");
    }
  }

  public void init() {

    button = (Button) findViewById(R.id.button);
    usernameEditText = (EditText) findViewById(R.id.username);
    passwordEditText = (EditText) findViewById(R.id.password);

    passwordEditText.setOnKeyListener(this);

    if (ParseUser.getCurrentUser() != null) {

        showUserList();
    }

    Intent intent = getIntent();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    init();

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}