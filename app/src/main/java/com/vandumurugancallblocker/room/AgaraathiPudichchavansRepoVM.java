package com.vandumurugancallblocker.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class AgaraathiPudichchavansRepoVM extends AndroidViewModel {
    private AgaraathiPudichchavansDao agaraathiPudichchavansDao;
    private FlagDao flagDao;
    public AgaraathiPudichchavansRepoVM(@NonNull Application application) {
        super(application);
        AppDb appDb = AppDb.getInstance(application);
        this.agaraathiPudichchavansDao=appDb.getAgaraathiPudichchavansDao();
        this.flagDao=appDb.getFlagDao();
    }

    public LiveData<List<AgaraathiPudichchavan>> getAgaraathiPudichavansAsLiveData(){
        return agaraathiPudichchavansDao.getAgaraathiPudichavansAsLiveData();
    }

    public void insert(AgaraathiPudichchavan agaraathiPudichchavan){
        new Insert(agaraathiPudichchavan).execute();
    }

    public void delete(AgaraathiPudichchavan agaraathiPudichchavan){
        new Delete(agaraathiPudichchavan).execute();
    }

    private class Delete extends AsyncTask{
        AgaraathiPudichchavan agaraathiPudichchavan;

        public Delete(AgaraathiPudichchavan agaraathiPudichchavan) {
            this.agaraathiPudichchavan = agaraathiPudichchavan;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            agaraathiPudichchavansDao.delete(agaraathiPudichchavan);
            return null;
        }
    }
    private class Insert extends AsyncTask{
        List<AgaraathiPudichchavan> agaraathiPudichchavans;

        public Insert(List<AgaraathiPudichchavan> agaraathiPudichchavans) {
            this.agaraathiPudichchavans = agaraathiPudichchavans;
        }

        public Insert(AgaraathiPudichchavan agaraathiPudichchavan) {
            agaraathiPudichchavans=new ArrayList<>();
            agaraathiPudichchavans.add(agaraathiPudichchavan);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            agaraathiPudichchavansDao.insert(agaraathiPudichchavans);
            return null;
        }
    }

}
