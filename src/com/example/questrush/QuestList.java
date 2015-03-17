package com.example.questrush;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.parse.*;

import java.util.List;


public class QuestList extends Activity{

    private QuestsAdapter questsAdapter;
    ListView lv;
    ImageButton menuButton;
    ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);

        questsAdapter = new QuestsAdapter(getApplicationContext(), this);

        lv = (ListView) findViewById(R.id.questList);
        lv.setAdapter(questsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                Intent questInfo = new Intent(getApplication(), QuestInfo.class);
                questInfo.putExtra("ID", Quests.getIntance().getQuestsVector().get(pos).getQuestID());
                startActivity(questInfo);
            }
        });

        menuButton = (ImageButton) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        refresh = (ImageButton) findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        refresh();
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class));
                        return true;
                    case R.id.logout:
                        ParseUser.getCurrentUser().logOut();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    public void refresh (){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("quests");
        query.whereEqualTo("active", true);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> questList, ParseException e) {
                if (e == null){
                    Quests.getIntance().getQuestsVector().clear();
                    for (ParseObject po : questList){
                        Quests quest = new Quests(po.getObjectId(), po.getString("name"), po.getString("description_short"), po.getString("description"), po.getDate("startTime"),po.getInt("state"));
                        Quests.getIntance().getQuestsVector().add(quest);
                        update();
                    }
                }
                else {
                    System.out.println("from Quests service: " + e.getMessage());
                }
            }
        });
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