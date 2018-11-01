package amrudesh.com.locationtodoreminder.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Random;

import amrudesh.com.locationtodoreminder.AddReminderActivity;
import amrudesh.com.locationtodoreminder.R;
import amrudesh.com.locationtodoreminder.data.AlarmReminderContract;

/**
 * Created by delaroy on 9/22/17.
 */

public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;
    String id="amrudesh.com.locationtodoreminder.reminder";
    String CHANNEL_NAME="amrudesh";

    Cursor cursor;
    //This is a deep link intent, and needs the task stack

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public ReminderAlarmService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Api +26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

        {
            NotificationChannel mChannel = new NotificationChannel(id, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(getString(R.string.reminder_title));
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            manager.createNotificationChannel(mChannel);

        }

        //Display a notification to view the task details
        Intent action = new Intent(this, AddReminderActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification note  = new NotificationCompat.Builder(getApplicationContext(),id)
                .setContentTitle(getString(R.string.reminder_title))
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentIntent(operation)
                .setChannelId(id)
                .setAutoCancel(true)
                .build();
        Log.i("msg1",id);
        manager.notify(NOTIFICATION_ID, note);

    }


}