package com.example.questrush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.parse.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

public class QuestInfo extends Activity {

    final ParseQuery<ParseObject> query = ParseQuery.getQuery("quests");
    final String UserID = ParseUser.getCurrentUser().getObjectId();
    String ID;
    JSONArray participantsJSONArray = null;

    Button signUnsign;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_info);

        ID = getIntent().getStringExtra("ID");
        signUnsign = (Button) findViewById(R.id.sign_unsign_button);
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    participantsJSONArray = parseObject.getJSONArray("participants");
                    if (participantsJSONArray.toString().contains(UserID)) {
                        signUnsign.setText("Unsign");
                        unSign();
                    } else {
                        signUnsign.setText("Sign");
                        sign();
                    }
                }
            }
        });
    }

    public void sign() {
        signUnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.getInBackground(ID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject parseObject, ParseException e) {
                        if (e == null) {

                            participantsJSONArray = parseObject.getJSONArray("participants");
                            participantsJSONArray.put(UserID);
                            parseObject.put("participants", participantsJSONArray);
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {

                                        Calendar cal = Calendar.getInstance();
                                        Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
                                        calendarIntent.setType("vnd.android.cursor.item/event");
                                        calendarIntent.putExtra("title", "Quest Rush: " + parseObject.getString("name"));
                                        calendarIntent.putExtra("description", parseObject.getString("description"));
//                                        calendarIntent.putExtra("beginTime", Quests.getIntance().getQuestsVector().get(pos).getQuestDate());
                                        calendarIntent.putExtra("allDay", false);
                                        calendarIntent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                        startActivity(calendarIntent);

                                        Toast.makeText(getApplicationContext(), "Signed", Toast.LENGTH_SHORT).show();
                                        signUnsign.setText("Unsign");
                                        unSign();

                                    } else {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void unSign() {
        signUnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                query.getInBackground(ID, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            participantsJSONArray = parseObject.getJSONArray("participants");

                            JSONArray temp = new JSONArray();
                            for (int i = 0; i < participantsJSONArray.length(); i++) {
                                try {
                                    if (!participantsJSONArray.get(i).equals(UserID)) {
                                        temp.put(participantsJSONArray.get(i));
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            parseObject.put("participants", temp);
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Unsigned", Toast.LENGTH_SHORT).show();
                                        signUnsign.setText("Sign");
                                        sign();
                                    } else {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}