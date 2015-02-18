package com.example.questrush;

import android.os.Binder;

public class QuestsBinder extends Binder{
    private QuestsService service;

    public QuestsBinder (QuestsService service){
        super();
        this.service = service;
    }


    public QuestsService getService() {
        return service;
    }

    public void setService(QuestsService service) {
        this.service = service;
    }
}
