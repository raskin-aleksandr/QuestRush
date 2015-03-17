package com.example.questrush;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QuestInfo extends Activity {

    final ParseQuery<ParseObject> query = ParseQuery.getQuery("quests");
    final String UserID = ParseUser.getCurrentUser().getObjectId();
    String ID;
    JSONArray participantsJSONArray = null;

    Button signUnsign;

    TextView questName;
    TextView questInfo;
    Date questDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_info);

        questName = (TextView) findViewById(R.id.questName);
        questInfo = (TextView) findViewById(R.id.questInfo);
        questInfo.setMovementMethod(new ScrollingMovementMethod());

        ID = getIntent().getStringExtra("ID");
        signUnsign = (Button) findViewById(R.id.register);
        query.getInBackground(ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    questName.setText(parseObject.getString("name"));
                    questInfo.setText(parseObject.getString("description"));

                    questDate = parseObject.getDate("startTime");

                    participantsJSONArray = parseObject.getJSONArray("participants");
                    if (participantsJSONArray.toString().contains(UserID)) {
                        signUnsign.setText("Unsign");
                        unSign();
                    } else {
                        signUnsign.setText("SIGN IN");
                        sign();
                    }
                }
            }
        });
    }

    public void startQuest(View view) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM (EEE) HH:mm");
        String formattedDate = df.format(questDate);

        Date time = new Date();
        time.getTime();

        if (time.getTime() > questDate.getTime()) {
            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
            intent.putExtra("quest_id", ID);
            startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuestInfo.this);
            alertDialogBuilder.setTitle(getString(R.string.quests_to_early));
            alertDialogBuilder.setMessage(getString(R.string.quests_starts_at) + formattedDate);
            alertDialogBuilder.setPositiveButton(getString(R.string.quests_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
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
                                        calendarIntent.putExtra("beginTime", parseObject.getDate("startTime").getTime());
                                        calendarIntent.putExtra("allDay", false);
                                        calendarIntent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                        startActivity(calendarIntent);

                                        signUnsign.setText("UNSIGN");
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