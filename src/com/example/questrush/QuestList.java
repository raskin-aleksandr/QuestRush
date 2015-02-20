package com.example.questrush;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.*;
import org.json.JSONArray;


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

        Intent intent = new Intent(getApplicationContext(), QuestsService.class);
        startService(intent);

        bindService(intent, this, 0);

        questsAdapter = new QuestsAdapter(getApplicationContext());

        lv = (ListView) findViewById(R.id.questListView);
        lv.setAdapter(questsAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                final String ID = Quests.getIntance().getQuestsVector().get(pos).getQuestID();
                final String UserID = User.getInstance().getmUser().getObjectId();


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



    public static void start(String questID) {
        Toast.makeText(context, questID, Toast.LENGTH_SHORT).show();
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
