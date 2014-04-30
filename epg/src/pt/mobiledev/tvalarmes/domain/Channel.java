package pt.mobiledev.tvalarmes.domain;

import android.content.Context;
import java.io.Serializable;
import pt.mobiledev.tvalarmes.util.Util;

public class Channel implements Serializable {

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
        return getLogoResourceId(context, this);
    }

    public static int getLogoResourceId(Context context, Channel ch) {
        return (Util.getResourceId(context, "drawable",
                ch.getId().toLowerCase().replace(" ", "_").replace("!", "")));
    }

    @Override
    public String toString() {
        return getName();
    }
}
