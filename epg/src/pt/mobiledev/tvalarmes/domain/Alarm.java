package pt.mobiledev.tvalarmes.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Alarm implements Serializable {
	Program program;
    int minutesBefore;
    boolean repeats;

    public Alarm(Program program, int minutesBefore, boolean repeats) {
		this.program = program;
		this.minutesBefore = minutesBefore;
		this.repeats = repeats;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

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
