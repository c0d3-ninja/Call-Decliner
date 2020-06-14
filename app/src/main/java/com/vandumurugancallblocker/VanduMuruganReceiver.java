package com.vandumurugancallblocker;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.internal.telephony.ITelephony;
import com.vandumurugancallblocker.room.AgaraathiPudichchavan;
import com.vandumurugancallblocker.room.AgaraathiPudichchavansRepo;
import com.vandumurugancallblocker.room.FlagEntity;
import com.vandumurugancallblocker.room.FlagRepo;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class VanduMuruganReceiver extends BroadcastReceiver {
    private static final String TAG = VanduMuruganReceiver.class.getName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(!VakkeelUtils.isAllCallPermissionsEnabled(context)){
            return;
        }
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getCallState() != TelephonyManager.CALL_STATE_RINGING) {
            return;
        }
        if (!intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
            return;
        }
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        number=number.replace("+","");
        breakCallAndNotify(context, number);
    }

    private void breakCallNougatAndLower(Context context) {
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private void breakCallPieAndHigher(Context context) {
        TelecomManager telecomManager = (TelecomManager)
                context.getSystemService(Context.TELECOM_SERVICE);
        try {
            telecomManager.getClass().getMethod("endCall").invoke(telecomManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void breakCall(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            breakCallPieAndHigher(context);
        } else {
            breakCallNougatAndLower(context);
        }

    }
    private void breakCallAndNotify(Context context, String number) {
        new AvoidCall(context,number).execute();
    }

    private class AvoidCall extends AsyncTask{
        private String  phoneNumber;
        private AgaraathiPudichchavan matchedAgaraathiPudichchavan;
        private AgaraathiPudichchavansRepo agaraathiPudichchavansRepo;
        private FlagRepo flagRepo;
        private Context context;
        public AvoidCall(Context context,String phoneNumber) {
            this.context=context;
            this.phoneNumber = phoneNumber;
            this.agaraathiPudichchavansRepo= new AgaraathiPudichchavansRepo(context);
            this.flagRepo=new FlagRepo(context);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            FlagEntity flagEntity = flagRepo.getFlagSync(FlagEntity.DECLINE_DISABLED);
            if(flagEntity==null || !flagEntity.isValue()){
                List<AgaraathiPudichchavan> agaraathiPudichchavans = agaraathiPudichchavansRepo.getAgaraathiPudichavansAsList();
                matchedAgaraathiPudichchavan = VakkeelUtils.getMatchedAgaraathiPudichchavan(phoneNumber,agaraathiPudichchavans);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(matchedAgaraathiPudichchavan!=null){
                breakCall(context);
                showNotification(context,phoneNumber,matchedAgaraathiPudichchavan);
            }
        }
    }

    private void showNotification(Context context,String phoneNumber,AgaraathiPudichchavan agaraathiPudichchavan){
        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(context);
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context,AssistantConstants.CALL_DECLINED_NOTIFICATION_CHANNEL);

        String title="";
        if(!"".equals(agaraathiPudichchavan.getNote().trim())){
            title+=agaraathiPudichchavan.getNote().trim()+" \u2022 ";
        }
        title+=phoneNumber;
        String content = context.getString(R.string.call_declined_because_the_number)+" "+VakkeelUtils.getConditionString(context,agaraathiPudichchavan);
        notificationBuilder.setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(R.drawable.ic_call_primary_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher))
                .setPriority(NotificationCompat.PRIORITY_LOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.declined_notification);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(AssistantConstants.CALL_DECLINED_NOTIFICATION_CHANNEL, name, importance);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify((int)new Date().getTime(),notificationBuilder.build());
        
    }
}
