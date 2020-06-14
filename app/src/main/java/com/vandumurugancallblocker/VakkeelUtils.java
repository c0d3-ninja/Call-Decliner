package com.vandumurugancallblocker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.vandumurugancallblocker.room.AgaraathiPudichchavan;

import java.util.List;

public class VakkeelUtils {
    public static final boolean isAllCallPermissionsEnabled(Context context){
        boolean isPermissionGranted =  ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            isPermissionGranted = isPermissionGranted && ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED;
        }
        return isPermissionGranted;
    }

    public static AgaraathiPudichchavan getMatchedAgaraathiPudichchavan(String phoneNumber, List<AgaraathiPudichchavan> agaraathiPudichchavans){

        for (int i = 0; i < agaraathiPudichchavans.size(); i++) {
            AgaraathiPudichchavan agaraathiPudichchavan = agaraathiPudichchavans.get(i);
            String  tempNumber = agaraathiPudichchavan.getNumber().replace("+","");
            switch (agaraathiPudichchavan.getCondition()){
                case AssistantConstants.EQUALS:
                    if(phoneNumber.equals(tempNumber)){
                        return agaraathiPudichchavan;
                    }
                    break;
                case AssistantConstants.CONTAINS:
                    if(phoneNumber.contains(tempNumber)){
                        return agaraathiPudichchavan;
                    }
                    break;
                case AssistantConstants.STARTS_WITH:
                    if(phoneNumber.startsWith(tempNumber)){
                        return agaraathiPudichchavan;
                    }
                    break;
                case AssistantConstants.ENDS_WITH:
                    if(phoneNumber.endsWith(tempNumber)){
                        return agaraathiPudichchavan;
                    }
                    break;
            }
        }
        return null;
    }


    public static String getConditionString(Context context,AgaraathiPudichchavan agaraathiPudichchavan){
        String text="";
        switch (agaraathiPudichchavan.getCondition()){
            case AssistantConstants.EQUALS:
                text+=context.getString(R.string.equals);
                break;
            case AssistantConstants.CONTAINS:
                text+=context.getString(R.string.contains);
                break;
            case AssistantConstants.STARTS_WITH:
                text+=context.getString(R.string.starts_with);
                break;
            case AssistantConstants.ENDS_WITH:
                text+=context.getString(R.string.ends_with);
                break;
        }
        text+=" "+agaraathiPudichchavan.getNumber();
        return text;
    }



}
