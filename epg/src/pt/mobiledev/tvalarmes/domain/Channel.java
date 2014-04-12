package pt.mobiledev.tvalarmes.domain;

import pt.mobiledev.tvalarmes.util.Util;
import android.content.Context;

public class Channel {

    String name, id;

    public Channel(String sigla) {
        this.id = sigla;
    }

    public Channel(String name, String sigla) {
        this(sigla);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLogoResourceId(Context context) {
        return (Util.getResourceId(context, "drawable", getId().toLowerCase()));
    }

    @Override
    public String toString() {
        return getName();
    }
}
