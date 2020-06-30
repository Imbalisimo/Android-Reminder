package hr.vsite.mapreminder;

import android.provider.BaseColumns;

public final class EventsContract {
    private EventsContract(){}
    public static final String DB_NAME = "Events";
    public static abstract class Event implements BaseColumns {
        public static final String TABLE_NAME = "Event";
        public static final String ROW_DESCRIPTION = "description";
        public static final String ROW_DATE = "date";
        public static final String ROW_ANNUAL = "annually";
    }
}
