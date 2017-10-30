import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

    public static long ID_COUNTER = 0;

    private long id;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;
    private double duration;

    public Event(long id, String name, String location, Date startDate, Date endDate, double duration) {
        this.setId(id);
        this.setName(name);
        this.setLocation(location);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setDuration(duration);
    }

    public Event(String name, String location, Date startDate, Date endDate, double duration) {
        this.setId(ID_COUNTER++);
        this.setName(name);
        this.setLocation(location);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setDuration(duration);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String format() {
        StringBuilder result = new StringBuilder();
        result
                .append(String.format("Id: %s", this.getId())).append(System.lineSeparator())
                .append(String.format("Name: %s", this.getName())).append(System.lineSeparator())
                .append(String.format("Location: %s", this.getLocation())).append(System.lineSeparator())
                .append(String.format("Start Date: %s", formatDate(this.startDate))).append(System.lineSeparator())
                .append(String.format("End Date: %s", formatDate(this.endDate))).append(System.lineSeparator())
                .append(String.format("Duration: %.2f", this.getDuration()));

        return result.toString();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s",
                this.id, this.name, this.location, formatDate(this.startDate), formatDate(this.endDate), this.duration);
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = sdf.format(date);

        return formatted;
    }
}
