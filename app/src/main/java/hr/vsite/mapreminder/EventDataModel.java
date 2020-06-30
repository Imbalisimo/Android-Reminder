package hr.vsite.mapreminder;

public class EventDataModel {
    private int date;
    private String description;
    private boolean annual;

    public int getDate() {
        return date;
    }

    public int getYear(){
        return DateConverter.Year(date);
    }

    public int getMonth(){
        return DateConverter.Month(date);
    }

    public int getDay(){
        return DateConverter.Day(date);
    }

    public String getDescription() {
        return description;
    }

    public boolean isAnnual() {
        return annual;
    }

    public EventDataModel(int date, String description, boolean annual) {
        this.date = date;
        this.description = description;
        this.annual = annual;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(DateConverter.Day(date));
        builder.append(".");
        builder.append(DateConverter.Month(date));
        builder.append(".");
        builder.append(DateConverter.Year(date));

        builder.append(",");
        builder.append(description);

        builder.append(",");
        builder.append("(");
        builder.append(annual ? "annual" : "non-annual");
        builder.append(")");

        builder.append("\n");
        return builder.toString();
    }

}
