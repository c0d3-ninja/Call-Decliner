package com.vandumurugancallblocker.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AgaraathiPudichchavan.class,FlagEntity.class},version = 1,exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    public abstract AgaraathiPudichchavansDao getAgaraathiPudichchavansDao();
    public abstract FlagDao getFlagDao();

    public static volatile AppDb INSTANCE;

    public static AppDb getInstance(final Context context){
        if(INSTANCE==null){
            synchronized (AppDb.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppDb.class,"app_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
