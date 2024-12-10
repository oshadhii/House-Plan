package com.example.houseplan;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Show a toast
        Toast.makeText(context, "Alarm ringing! Time to wake up!", Toast.LENGTH_LONG).show();

        // Log event for debugging
        Log.d("AlarmReceiver", "Alarm has been triggered!");

        // Play the default alarm ringtone
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            // If no alarm sound is found, use the notification sound
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play(); // This will play the ringtone

        // Send a notification when the alarm triggers
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALARM_CHANNEL")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Alarm")
                .setContentText("Your alarm is ringing!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(1, builder.build());
    }
}
