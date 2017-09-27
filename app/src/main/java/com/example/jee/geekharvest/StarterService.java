package com.example.jee.geekharvest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by TanGX on 9/9/2560.
 */

public class StarterService extends Service {
    private static final String TAG = "StarterService";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent arg1, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent intent1 = new Intent(getApplicationContext(), NotiMoisture.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar1 = Calendar.getInstance();
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(),1000 * 10,pendingIntent1);
        sendBroadcast(new Intent("YouWillNeverKillMe"));

        Intent intent2 = new Intent(getApplicationContext(), NotiPhase.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(),101,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar2 = Calendar.getInstance();
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),1000 * 10,pendingIntent2);
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }
}
