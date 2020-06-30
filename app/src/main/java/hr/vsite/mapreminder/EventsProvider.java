package hr.vsite.mapreminder;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import hr.vsite.mapreminder.EventsContract;
import hr.vsite.mapreminder.EventsHelper;

public class EventsProvider extends ContentProvider {
    public  static final String AUTHORITY = "hr.vsite.mapreminder.provider";
    public  static final String PATH_EVENT = "event";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static final int EVENT_CODE = 13;
    static {
        uriMatcher.addURI(AUTHORITY, PATH_EVENT, EVENT_CODE);
    }

    public EventsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        EventsHelper helper = new EventsHelper(getContext());
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            return db.delete(EventsContract.Event.TABLE_NAME, selection, selectionArgs);

        } catch (android.database.SQLException ex){
            ex.printStackTrace();
        }
        return  -1;
    }

    @Override
    public String getType(Uri uri) {
        throw new IllegalArgumentException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        EventsHelper helper = new EventsHelper(getContext());
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            long id = db.insert(EventsContract.Event.TABLE_NAME, null, values);

            return ContentUris.withAppendedId(uri, id);

        } catch (android.database.SQLException ex){
            ex.printStackTrace();
        }
        return  Uri.EMPTY;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case EVENT_CODE:
                EventsHelper helper = new EventsHelper(getContext());
                try {
                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor =  db.query(EventsContract.Event.TABLE_NAME, projection, selection, selectionArgs,null,null, EventsContract.Event.ROW_DATE);
                    return cursor;
                } catch (android.database.SQLException ex){
                    ex.printStackTrace();
                }
                break;
            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("Incorrect Content provider URI");
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
