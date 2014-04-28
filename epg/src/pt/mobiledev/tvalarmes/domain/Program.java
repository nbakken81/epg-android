package pt.mobiledev.tvalarmes.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Program implements Serializable {

    int id, season, episode;
    String title, description, channelId, channelName;
    Date startDate;

    public Program() {
    }

    public Program(String title, String channelId) {
        this.title = title;
        this.channelId = channelId;
    }

    public Program(int id, String title, Date startDate, String channelId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.channelId = channelId;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return getTitle() + "(" + getChannelId() + ")";
    }

    @Override
    public boolean equals(Object o) {
        Program p = ((Program) o);
        return p.getChannelId().equals(getChannelId()) && p.getTitle().equalsIgnoreCase(getTitle());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.title != null ? this.title.toLowerCase().hashCode() : 0);
        hash = 83 * hash + (this.channelId != null ? this.channelId.hashCode() : 0);
        return hash;
    }
}
