package pt.mobiledev.tvalarmes.domain;

public class Alarm extends Program {

    int minutesBefore;

    public int getMinutesBefore() {
        return minutesBefore;
    }

    public void setMinutesBefore(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }
}
