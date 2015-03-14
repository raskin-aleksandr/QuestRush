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
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.quest, viewGroup, false);

        switch (i % 6) {
            case 0:
                rl.setBackgroundColor(0x6676bebf);
                break;
            case 1:
                rl.setBackgroundColor(0x66db1675);
                break;
            case 2:
                rl.setBackgroundColor(0x66e8a820);
                break;
            case 3:
                rl.setBackgroundColor(0x66fb0740);
                break;
            case 4:
                rl.setBackgroundColor(0x66034561);
                break;
            case 5:
                rl.setBackgroundColor(0x66dda037);
                break;
        }

        TextView name = (TextView) rl.findViewById(R.id.questName);
        name.setTextColor(Color.WHITE);
        name.setText(Quests.getIntance().getQuestsVector().get(i).getQuestName().toUpperCase());

        TextView descriptionShort = (TextView) rl.findViewById(R.id.shortDescription);
        descriptionShort.setTextColor(Color.WHITE);
        descriptionShort.setText(Quests.getIntance().getQuestsVector().get(i).getQuestDescriptionShort());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setTextColor(Color.WHITE);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM (EEE) HH:mm");

        String formattedDate = df.format(Quests.getIntance().getQuestsVector().get(i).getQuestDate());
        time.setText(context.getString(R.string.quests_starts_at) + formattedDate);

        ImageView iv = (ImageView) rl.findViewById(R.id.startImage);

        Date curTime = new Date();
        curTime.getTime();


        if (Quests.getIntance().getQuestsVector().get(i).getQuestDate().getTime() < curTime.getTime()) {
            iv.setImageResource(R.drawable.unlock_icon);
        } else {
            iv.setImageResource(R.drawable.lock_icon);
        }

//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                parent.start(i);
//                parent.start(Quests.getIntance().getQuestsVector().get(i).getQuestID(), Quests.getIntance().getQuestsVector().get(i).getQuestDate());
//            }
//        });
        return rl;
    }
}