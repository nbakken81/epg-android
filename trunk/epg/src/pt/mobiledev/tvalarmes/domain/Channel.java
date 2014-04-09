package pt.mobiledev.tvalarmes.domain;

public class Channel {

    String name, id, logoUrl;

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return getName();
    }
}
