package com.vandumurugancallblocker.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class FlagEntity {

    @Ignore
    public static final String DECLINE_DISABLED="DECLINE_DISABLED";

    @NonNull
    @PrimaryKey
    private String id;
    private boolean value;

    public FlagEntity(@NonNull String id, boolean value) {
        this.id = id;
        this.value = value;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
