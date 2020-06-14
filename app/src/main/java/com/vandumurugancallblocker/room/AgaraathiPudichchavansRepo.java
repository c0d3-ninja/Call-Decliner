package com.vandumurugancallblocker.room;

import android.content.Context;

import java.util.List;

public class AgaraathiPudichchavansRepo  {
    AgaraathiPudichchavansDao agaraathiPudichchavansDao;
    public AgaraathiPudichchavansRepo(Context context) {
        this.agaraathiPudichchavansDao=AppDb.getInstance(context).getAgaraathiPudichchavansDao();
    }
    public List<AgaraathiPudichchavan> getAgaraathiPudichavansAsList(){
        return agaraathiPudichchavansDao.getAgaraathiPudichavansAsList();
    }

}
