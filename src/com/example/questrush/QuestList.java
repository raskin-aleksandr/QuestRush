package com.example.questrush;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.*;
import org.json.JSONArray;

import java.util.Calendar;


public class QuestList extends Activity implements ServiceConnection {

    private QuestsAdapter questsAdapter;
    private QuestsService questsService;
    ListView lv;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);

        this.context = getApplicationContext();

        final String UserID = ParseUser.getCurrentUser().getObjectId();

        Intent intent = new Intent(getApplicationContext(), QuestsService.class);
        startService(intent);

        bindService(intent, this, 0);

        questsAdapter = new QuestsAdapter(getApplicationContext(), this);

        lv = (ListView) findViewById(R.id.questListView);
        lv.setAdapter(questsAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                final String ID = Quests.getIntance().getQuestsVector().get(pos).getQuestID();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuestList.this);
                alertDialogBuilder.setTitle("Do you want to take part?");
                alertDialogBuilder.setMessage("Click yes to participate");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setIcon(R.drawable.ic_launcher);

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("quests");
                        query.getInBackground(ID, new GetCallback<ParseObject>() {

                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if (e == null) {
                                    JSONArray participantsJSONArray = parseObject.getJSONArray("participants");

                                    if (participantsJSONArray.toString().contains(UserID)) {
                                        Toast.makeText(getApplicationContext(), "You are already signed in!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        participantsJSONArray.put(UserID);
                                        parseObject.put("participants", participantsJSONArray);
                                        parseObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {

                                                    Calendar cal = Calendar.getInstance();
                                                    Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
                                                    calendarIntent.setType("vnd.android.cursor.item/event");
                                                    calendarIntent.putExtra("title", "Quest Rush: " + Quests.getIntance().getQuestsVector().get(pos).getQuestName());
                                                    calendarIntent.putExtra("description", Quests.getIntance().getQuestsVector().get(pos).getQuestDescription());
                                                    calendarIntent.putExtra("beginTime", Quests.getIntance().getQuestsVector().get(pos).getQuestDate());
                                                    calendarIntent.putExtra("allDay", false);
                                                    calendarIntent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                                                    startActivity(calendarIntent);


                                                    Toast.makeText(getApplicationContext(), "You are in!", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
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
        });
    }



    public void start(String questID) {

        Toast.makeText(getApplicationContext(), questID, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        intent.putExtra("quest_id", questID);
        startActivity(intent);
    }

    public void update() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv.invalidateViews();
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        QuestsBinder binder = (QuestsBinder) iBinder;
        questsService = binder.getService();
        questsService.setActivity(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

        unbindService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                ParseUser.getCurrentUser().logOut();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;

            case R.id.about:
                AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(QuestList.this);
                aboutDialogBuilder.setTitle("About Quest Rush");
                aboutDialogBuilder.setMessage("___");
                aboutDialogBuilder.setCancelable(true);
                aboutDialogBuilder.setIcon(R.drawable.ic_launcher);

                aboutDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog aboutDialog = aboutDialogBuilder.create();
                aboutDialog.show();


                break;
        }

        return super.onOptionsItemSelected(item);
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
                QuestList.this.finish();
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