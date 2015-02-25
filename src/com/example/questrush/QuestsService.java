package com.example.questrush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class QuestsService extends Service implements Runnable{

    private QuestList activity;

    public QuestList getActivity() {
        return activity;
    }

    public void setActivity(QuestList activity) {
        this.activity = activity;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new QuestsBinder(this);
    }

    @Override
    public void run() {
        while (true){
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
                            activity.update();
                        }
                    }
                    else {
                        System.out.println("from Quests service: " + e.getMessage());
                    }
                }
            });

            SystemClock.sleep(500);
        }
    }

    @Override
    public void onCreate() {
        Thread t1 = new Thread(this);
        t1.start();
        super.onCreate();
    }
}