package com.example.questrush;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class QuestsAdapter extends BaseAdapter {

    private Context context;
    private QuestList parent;

    QuestsAdapter(Context context, QuestList parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public int getCount() {
        return Quests.getIntance().getQuestsVector().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.quest, viewGroup, false);

        TextView name = (TextView) rl.findViewById(R.id.questName);
        name.setTextColor(Color.BLACK);
        name.setText(Quests.getIntance().getQuestsVector().get(i).getQuestName());

        TextView descriptionShort = (TextView) rl.findViewById(R.id.shortDescription);
        descriptionShort.setTextColor(Color.BLACK);
        descriptionShort.setText(Quests.getIntance().getQuestsVector().get(i).getQuestDescriptionShort());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setTextColor(Color.BLACK);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM (EEE) HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        String formattedDate = df.format(Quests.getIntance().getQuestsVector().get(i).getQuestDate());
        time.setText("Starts at: " + formattedDate);

        ImageView iv = (ImageView) rl.findViewById(R.id.startImage);


        Date curTime = new Date();
        curTime.getTime();


        if (Quests.getIntance().getQuestsVector().get(i).getQuestDate().getTime() < curTime.getTime()) {
            iv.setImageResource(R.drawable.play_active);
        }
        else{
            iv.setImageResource(R.drawable.play_inactive);
        }


        final QuestList ql = new QuestList();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.start(Quests.getIntance().getQuestsVector().get(i).getQuestID(), Quests.getIntance().getQuestsVector().get(i).getQuestDate());
            }
        });

        return rl;
    }


}
