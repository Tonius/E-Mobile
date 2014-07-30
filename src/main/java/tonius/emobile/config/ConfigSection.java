package tonius.emobile.config;

public class ConfigSection {

    public String name;
    public String id;

    public ConfigSection(String name, String id) {
        this.name = name;
        this.id = id;
        EMConfig.configSections.add(this);
    }

    public String toLowerCase() {
        return this.name.toLowerCase();
    }

}
