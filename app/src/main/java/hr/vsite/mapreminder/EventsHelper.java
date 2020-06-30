package hr.vsite.mapreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hr.vsite.mapreminder.EventsContract;

public class EventsHelper extends SQLiteOpenHelper {
    static final int VERSION = 2;
    static final String SCRIPT_CREATE_EVENTS_TABLE =
            "CREATE TABLE " + EventsContract.Event.TABLE_NAME + " ( "  +
                    EventsContract.Event._ID + " INTEGER PRIMARY KEY, " +
                    EventsContract.Event.ROW_DESCRIPTION + " TEXT, " +
                    EventsContract.Event.ROW_DATE + " INTEGER, " +
                    EventsContract.Event.ROW_ANNUAL + " INTEGER " +
                    ");";
    static final String SCRIPT_DELETE_EVENTS_TABLE =
            "DROP TABLE " + EventsContract.Event.TABLE_NAME + ";";


    public EventsHelper(Context context) {
        super(context, EventsContract.DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SCRIPT_DELETE_EVENTS_TABLE);
        db.execSQL(SCRIPT_CREATE_EVENTS_TABLE);
    }
}
