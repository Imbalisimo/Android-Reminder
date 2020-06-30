package hr.vsite.mapreminder;

public class Notif {
    public static final String CHANNEL_ID = "Reminder" ;

    private static int notificationID = 0;

    public static int getNotificationID()
    {
        return notificationID++;
    }
}
