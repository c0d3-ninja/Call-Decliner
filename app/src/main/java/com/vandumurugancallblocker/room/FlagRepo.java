package com.vandumurugancallblocker.room;

import android.content.Context;

public class FlagRepo {
    private FlagDao flagDao;
    public FlagRepo(Context context) {
        this.flagDao=AppDb.getInstance(context).getFlagDao();
    }
    public FlagEntity getFlagSync(String  id){
        return flagDao.getFlagSync(id);
    }
}
