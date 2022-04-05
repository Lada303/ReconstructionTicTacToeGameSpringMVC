package Lada303.models;

public class Gameplay {
    private int id_gameplay;
    private String name_gameplay;
    private String map_size;

    public Gameplay() {}

    public int getId_gameplay() {
        return id_gameplay;
    }

    public void setId_gameplay(int id_gameplay) {
        this.id_gameplay = id_gameplay;
    }

    public String getName_gameplay() {
        return name_gameplay;
    }

    public void setName_gameplay(String name_gameplay) {
        this.name_gameplay = name_gameplay;
    }

    public String getMap_size() {
        return map_size;
    }

    public void setMap_size(String map_size) {
        this.map_size = map_size;
    }
}
