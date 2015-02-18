package com.example.questrush;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        questsAdapter = new QuestsAdapter(getApplicationContext());

        lv = (ListView) findViewById(R.id.questListView);
        lv.setAdapter(questsAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), Questions.class);
                intent.putExtra("questID", Quests.getIntance().getQuestsVector().get(position).getQuestID());
                startActivity(intent);
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
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        QuestsBinder binder = (QuestsBinder) iBinder;
        questsService = binder.getService();
        questsService.setActivity(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        unbindService(this);
    }
}
