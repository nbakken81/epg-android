package pt.mobiledev.tvalarmes.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Alarm implements Serializable {

    Program program;
    int minutesBefore;
    boolean once;

    public Alarm(Program program, int minutesBefore, boolean once) {
        this.program = program;
        this.minutesBefore = minutesBefore;
        this.once = once;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public boolean isOnce() {
        return once;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public int getMinutesBefore() {
        return minutesBefore;
    }

    public void setMinutesBefore(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }
}
