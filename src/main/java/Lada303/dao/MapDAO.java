package Lada303.dao;

public interface MapDAO {
    void addNewMap(int id_gameplay, String map_size);
    String getMapSize(int id_gameplay);
}
