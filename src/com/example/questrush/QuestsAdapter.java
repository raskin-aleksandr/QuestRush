package com.example.questrush;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestsAdapter extends BaseAdapter {

    private Context context;

    QuestsAdapter(Context context) {
        this.context = context;
    }

    public QuestsAdapter(){

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout rl;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rl = (RelativeLayout) inflater.inflate(R.layout.quest, viewGroup, false);

        TextView name = (TextView) rl.findViewById(R.id.questName);
        name.setTextColor(Color.BLACK);
        name.setText(Quests.getIntance().getQuestsVector().get(i).getQuestName());

        TextView time = (TextView) rl.findViewById(R.id.questTime);
        time.setTextColor(Color.BLACK);
        time.setText(Quests.getIntance().getQuestsVector().get(i).getQuestTime());

        return rl;
    }
}
