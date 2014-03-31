package pt.mobiledev.tvalarmes;

import java.util.Date;

public class Program {

    private String id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String channel;

    public Program(String id, String title, String description, Date startDate,
            Date endDate, String channel) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.channel = channel;
    }

    public Program() {

    }

    public String getId() {
        return id;
    }

    public void setId(String currentValue) {
        this.id = currentValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
