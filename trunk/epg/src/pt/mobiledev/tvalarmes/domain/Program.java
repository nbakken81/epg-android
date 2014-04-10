package pt.mobiledev.tvalarmes.domain;

import java.util.Date;

public class Program {

    int season, episode;
    String id, title;
    Date startDate;
    String channelSigla;

    public Program() {
    }

    public Program(String id, String title, Date startDate, String channelSigla) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.channelSigla = channelSigla;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getChannelSigla() {
        return channelSigla;
    }

    public void setChannelSigla(String channelSigla) {
        this.channelSigla = channelSigla;
    }

    @Override
    public String toString() {
        return getTitle() + "(" + getChannelSigla() + ")";
    }

    public int compareTo(Program other) {
        return title.compareTo(other.getTitle());
    }

    public boolean equals(Program other) {
        return title.equals(other.getTitle());
    }
}
