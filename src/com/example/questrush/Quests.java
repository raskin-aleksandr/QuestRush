package com.example.questrush;

import java.util.Date;
import java.util.Vector;

public class Quests {

    private static Quests intance = null;

    private Vector<Quests> questsVector;

    public static Quests getIntance() {

        if (intance == null) {
            intance = new Quests();
        }

        return intance;
    }

    private Quests() {
        questsVector = new Vector<Quests>();
    }

    public Vector<Quests> getQuestsVector() {
        return questsVector;
    }

    private String questID;
    private String questName;
    private Date questDate;
//    private String questTime;
    private int questState;


    public Quests(String questID, String questName, Date questDate, int questState) {
        this.questID = questID;
        this.questName = questName;
        this.questDate = questDate;
        this.questState = questState;
    }

    public String getQuestID() {
        return questID;
    }

    public String getQuestName() {
        return questName;
    }

    public Date getQuestDate() {
        return questDate;
    }

    public int getQuestState() {
        return questState;
    }
}
