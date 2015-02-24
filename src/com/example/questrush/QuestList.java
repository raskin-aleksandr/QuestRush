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
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;


public class QuestList extends Activity implements ServiceConnection {

    private QuestsAdapter questsAdapter;
    private QuestsService questsService;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);


        Intent intent = new Intent(getApplicationContext(), QuestsService.class);
        startService(intent);

        bindService(intent, this, 0);

        questsAdapter = new QuestsAdapter(getApplicationContext(), this);

        lv = (ListView) findViewById(R.id.questListView);
        lv.setAdapter(questsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                Intent questInfo = new Intent(getApplication(), QuestInfo.class);
                questInfo.putExtra("ID", Quests.getIntance().getQuestsVector().get(pos).getQuestID());

                startActivity(questInfo);
            }
        });
    }


    public void start(String questID, Date questDate) {

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM (EEE) HH:mm");

        String formattedDate = df.format(questDate);

        Date time = new Date();
        time.getTime();

        if (time.getTime() > questDate.getTime()) {
            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
            intent.putExtra("quest_id", questID);
            startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuestList.this);
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

        switch (item.getItemId()) {
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

                aboutDialogBuilder.setNegativeButton(getString(R.string.quests_ok), new DialogInterface.OnClickListener() {
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
        alertDialogBuilder.setTitle(getString(R.string.exit_title));
        alertDialogBuilder.setMessage(getString(R.string.exit_message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                QuestList.this.finish();
            }
        });

        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}