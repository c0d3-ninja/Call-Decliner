package com.vandumurugancallblocker.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FlagRepoVM extends AndroidViewModel {
    private FlagDao flagDao;
    public FlagRepoVM(@NonNull Application application) {
        super(application);
        this.flagDao=AppDb.getInstance(application).getFlagDao();
    }
    public LiveData<FlagEntity> getFlagAsLiveData(String id){
        return flagDao.getFlagAsLiveData(id);
    }

    public void insert(String id,boolean isEnabled){
        new Insert(new FlagEntity(id,isEnabled)).execute();
    }

    private class Insert extends AsyncTask{
        FlagEntity flagEntity;

        public Insert(FlagEntity flagEntity) {
            this.flagEntity = flagEntity;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            flagDao.insert(flagEntity);
            return null;
        }
    }

}
