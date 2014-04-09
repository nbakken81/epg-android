package pt.mobiledev.tvalarmes.domain;

public class Channel {

    String name, id, logoUrl;

    public Channel(String id) {
        this.id = id;
    }

    public Channel(String name, String sigla) {
        this.name = name;
        this.id = sigla;
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
}
