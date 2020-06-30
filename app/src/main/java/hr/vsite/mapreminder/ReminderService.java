package hr.vsite.mapreminder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReminderService extends IntentService {
    public static final String ACTION = "Remind";

    private static final String FILE_NAME_SAVE = "day.txt";

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            while(true)
            {
                int currentDate = getCurrentDate();
                int i = getLastDateEntry();
                if(i > 0) {
                    for (++i; currentDate >= i; ++i) {
                        List<EventDataModel> events = DatabaseManager.SelectDate(i);
                        StringBuilder sb = new StringBuilder();
                        for(EventDataModel event : events){
                            sb.append(event.toString());
                        }

                        if(sb.toString() != "")
                            riseNotification(sb.toString());

                        for(EventDataModel event : events){
                            DatabaseManager.Delete(event);
                            if(event.isAnnual())
                                DatabaseManager.Insert(
                                        new EventDataModel(
                                                DateConverter.DateToInt(event.getYear() + 1, event.getMonth(), event.getDay()),
                                                event.getDescription(),
                                                event.isAnnual()));
                        }

                        writeCurrentDate(currentDate);
                    }
                }
                android.os.SystemClock.sleep(50*1000);
            }
        }
    }

    private int getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return DateConverter.DateToInt(year, month, day);
    }

    public static final String PREFERENCE_NAME = "reminder";
    public static final String PREFERENCE_ENTRY_KEY = "lastDate";
    private int getLastDateEntry()
    {
        return this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(PREFERENCE_ENTRY_KEY, 0);
    }

    private void writeCurrentDate(int date)
    {
        SharedPreferences preferences = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREFERENCE_ENTRY_KEY, date);
        editor.commit();
    }

    private void riseNotification(String eventText)
    {
        Intent activity = new Intent(this, MainActivity.class);
        activity.putExtra("events", eventText);
        activity.setAction(ACTION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,  activity,  0);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this, Notif.CHANNEL_ID)
                .setContentTitle("Reminder")
                .setContentText(eventText)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        mNotificationManager.notify(Notif.getNotificationID(), nBuilder.build());
    }
}
