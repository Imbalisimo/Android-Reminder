package hr.vsite.mapreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button_datePicker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), "Choose date");
            }
        });

        ((Button)findViewById(R.id.button_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day == 0 || month == 0 || year == 0)
                {
                    Toast.makeText(getBaseContext(), String.format("Invalid date: %02d.%02d.%d!", day, month, year), Toast.LENGTH_LONG).show();
                    year = 0;
                    month = 0;
                    day = 0;
                    return;
                }
                String description =((EditText)findViewById(R.id.editText_description)).getText().toString();
                boolean isAnnual = ((Switch)findViewById(R.id.switch_annual)).isChecked();

                if(!DatabaseManager.Insert(new EventDataModel(DateConverter.DateToInt(year, month, day), description, isAnnual)))
                    Toast.makeText(getBaseContext(), "Entry failed", Toast.LENGTH_LONG).show();

                year = 0;
                month = 0;
                day = 0;
                ((TextView)findViewById(R.id.lb_date)).setText("");
                ((TextView)findViewById(R.id.editText_description)).setText("");
            }
        });

        ((Button)findViewById(R.id.button_all)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(getBaseContext(), AllEventsActivity.class);
                startActivity(activity);
            }
        });

        DatabaseManager.Initialize(getContentResolver());

        Intent intent = getIntent();
        if(intent.getAction() == ReminderService.ACTION){
            String events = getIntent().getStringExtra("events");
            Toast.makeText(this, "removed events:" + events, Toast.LENGTH_LONG).show();
        }
        createNotificationChannel();
        startReminderService();
    }

    private void startReminderService(){
        Intent intent = new Intent(this, ReminderService.class);
        startService(intent);
    }

    static int year = 0;
    static int month = 0;
    static int day = 0;
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int yearPicked, int monthPicked, int dayOfMonth) {
            ((TextView)getActivity().findViewById(R.id.lb_date)).setText(String.format("%02d.%02d.%d", dayOfMonth, ++monthPicked, yearPicked));
            year = yearPicked;
            month = monthPicked;
            day = dayOfMonth;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Notif.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
