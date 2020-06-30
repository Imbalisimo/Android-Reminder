package hr.vsite.mapreminder;

public class DateConverter {
    public static int DateToInt(int year, int month, int day)
    {
        return day + (32 * month) + (32 * 13 * year);
    }

    public static int Day(int date)
    {
        return date % 32;
    }

    public static int Month(int date)
    {
        return (date / 32) % 13;
    }

    public static int Year(int date)
    {
        return (date / 32) / 13;
    }
}
