package hr.vsite.mapreminder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import hr.vsite.mapreminder.DateConverter;

public class DatabaseManager {
    static Uri uri = null;
    static ContentResolver resolver;

    public static void Initialize(ContentResolver cr)
    {
        Uri.Builder ub = new Uri.Builder();
        ub
                .scheme("content")
                .encodedAuthority(EventsProvider.AUTHORITY)
                .appendPath(EventsProvider.PATH_EVENT);
        uri = ub.build();

        resolver = cr;
    }

    public static boolean Insert(EventDataModel event)
    {
        ContentValues values = new ContentValues();
        values.put(EventsContract.Event.ROW_DESCRIPTION, event.getDescription());
        values.put(EventsContract.Event.ROW_DATE, event.getDate());
        values.put(EventsContract.Event.ROW_ANNUAL, event.isAnnual() ? 1 : 0);
        return resolver.insert(uri, values) != Uri.EMPTY;
    }

    public static List<EventDataModel> SelectAll()
    {
        return Select(null, null);
    }

    public static List<EventDataModel> SelectDate(int date)
    {
        String whereClause = EventsContract.Event.ROW_DATE + " = ?";
        String[] whereArgs = new String[] {
                String.valueOf(date),
        };

        return Select(whereClause, whereArgs);
    }

    private static List<EventDataModel> Select(String selection, String[] selectionsArgs)
    {
        String[] rows = new String[]{
                EventsContract.Event.ROW_DESCRIPTION,
                EventsContract.Event.ROW_DATE,
                EventsContract.Event.ROW_ANNUAL
        };

        Cursor cursor = resolver.query(uri, rows, selection, selectionsArgs, null);

        List<EventDataModel> events = new ArrayList<>();
        if (cursor == null) return events;

        while(cursor.moveToNext()) {
            int date = cursor.getInt(cursor.getColumnIndex(EventsContract.Event.ROW_DATE));
            String desc = cursor.getString(cursor.getColumnIndex(EventsContract.Event.ROW_DESCRIPTION));
            boolean annual = cursor.getInt(cursor.getColumnIndex(EventsContract.Event.ROW_ANNUAL)) == 1;
            events.add(new EventDataModel(date, desc, annual));
        }
        return events;
    }

    public static void Delete(EventDataModel event)
    {
        String whereClause = EventsContract.Event.ROW_DESCRIPTION + " = ? AND " + EventsContract.Event.ROW_DATE + " = ? ";
        String[] whereArgs = new String[] {
                event.getDescription(),
                String.valueOf(event.getDate())
        };

        resolver.delete(uri, whereClause, whereArgs);
    }
}
