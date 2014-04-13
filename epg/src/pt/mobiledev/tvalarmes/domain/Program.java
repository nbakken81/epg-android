package pt.mobiledev.tvalarmes.domain;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Program implements Serializable {

    int season, episode;
    int id;
    Date startDate;
    String title, channelSigla;

    public Program() {
    }

    public Program(int id, String title, Date startDate, String channelSigla) {
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

    @Override
    public boolean equals(Object o) {
        return getClass().isInstance(o) && ((Program) o).getTitle().equals(getTitle());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.title != null ? this.title.hashCode() : 0);
        return hash;
    }
}
