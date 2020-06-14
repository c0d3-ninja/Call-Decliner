package com.vandumurugancallblocker.room;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class AgaraathiPudichchavan {
    @Ignore
    public static final String COLUMN_ID="id";
    @Ignore
    public static final String COLUMN_NUMBER="number";
    @Ignore
    public static final String COLUMN_CONDITION="condition";
    @Ignore
    public static final String COLUMN_NOTE="note";


    @NonNull
    @PrimaryKey
    private Long id;
    private String number;
    private int condition;
    private String note;

    public AgaraathiPudichchavan(@NonNull Long id, String number, int condition, String note) {
        this.id = id;
        this.number = number;
        this.condition = condition;
        this.note = note;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
