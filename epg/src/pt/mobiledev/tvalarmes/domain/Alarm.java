package pt.mobiledev.tvalarmes.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Alarm extends Program implements Serializable {
	
    int minutesBefore;
    boolean repeats;

    public boolean isRepeats() {
		return repeats;
	}

	public void setRepeats(boolean repeats) {
		this.repeats = repeats;
	}

	public int getMinutesBefore() {
        return minutesBefore;
    }

    public void setMinutesBefore(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }
}
