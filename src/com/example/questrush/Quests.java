package com.example.questrush;

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
    private String questTime;
    private int questState;


    public Quests(String questID, String questName, String questTime, int questState) {
        this.questID = questID;
        this.questName = questName;
        this.questTime = questTime;
        this.questState = questState;
    }

    public String getQuestID() {
        return questID;
    }

    public String getQuestName() {
        return questName;
    }

    public String getQuestTime() {
        return questTime;
    }

    public int getQuestState() {
        return questState;
    }
}
