package hr.vsite.mapreminder;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

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

    public static boolean Insert(int year, int month, int day, String description)
    {
        ContentValues values = new ContentValues();
        values.put(EventsContract.Event.ROW_DESCRIPTION, description);
        values.put(EventsContract.Event.ROW_DATE, DateConverter.DateToInt(year, month, day));
        return resolver.insert(uri, values) != Uri.EMPTY;
    }

    public static String SelectAll()
    {
        return Select(null, null);
    }

    public static String SelectDate(int date)
    {
        String whereClause = EventsContract.Event.ROW_DATE + " = ?";
        String[] whereArgs = new String[] {
                String.valueOf(date),
        };

        return Select(whereClause, whereArgs);
    }

    private static String Select(String selection, String[] selectionsArgs)
    {
        String[] rows = new String[]{
                EventsContract.Event.ROW_DESCRIPTION,
                EventsContract.Event.ROW_DATE
        };

        Cursor cursor = resolver.query(uri, rows, selection, selectionsArgs, null);
        if (cursor == null) return "";

        StringBuilder builder = new StringBuilder();
        while(cursor.moveToNext()) {
            int date = cursor.getInt(cursor.getColumnIndex(EventsContract.Event.ROW_DATE));
            builder.append(DateConverter.Day(date));
            builder.append(".");
            builder.append(DateConverter.Month(date));
            builder.append(".");
            builder.append(DateConverter.Year(date));
            builder.append(",");
            builder.append(cursor.getString(cursor.getColumnIndex(EventsContract.Event.ROW_DESCRIPTION)));
            builder.append("\n");
        }
        return builder.toString();
    }

    public static void Delete(String event)
    {
        String[] tokens = event.split(",");
        String description = tokens[1];

        String[] date = tokens[0].split("\\.");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);

//        String whereClause = EventsContract.Event.ROW_DESCRIPTION + " = " + description +
//                " AND " + EventsContract.Event.ROW_DATE + " = " + String.valueOf(DateConverter.DateToInt(year, month, day));
        String whereClause = EventsContract.Event.ROW_DESCRIPTION + " = ? AND " + EventsContract.Event.ROW_DATE + " = ? ";
        String[] whereArgs = new String[] {
                description,
                String.valueOf(DateConverter.DateToInt(year, month, day))
        };

        resolver.delete(uri, whereClause, whereArgs);
    }
}
