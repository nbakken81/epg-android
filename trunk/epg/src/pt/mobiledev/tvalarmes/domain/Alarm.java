package pt.mobiledev.tvalarmes.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Alarm implements Serializable {

    Program program;
    boolean once;

    public Alarm(Program program, boolean once) {
        this.program = program;
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

    @Override
    public String toString() {
        return getProgram().toString();
    }
}
