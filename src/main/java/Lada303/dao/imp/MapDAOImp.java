package Lada303.dao.imp;

import Lada303.dao.ConnectorDB;
import Lada303.dao.MapDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MapDAOImp implements MapDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapDAOImp.class);
    private static final String SQL_INSERT_NEW_GAMEPLAY_MAP =
            "INSERT INTO maps (id_gameplay, map_size) VALUES (?, ?);";
    private static final String SQL_GET_GAMEPLAY_MAP =
            "SELECT map_size FROM maps WHERE id_gameplay = ?;";

    private final ConnectorDB db;

    @Autowired
    public MapDAOImp(ConnectorDB db)  {
        this.db = db;
    }

    public void addNewMap(int id_gameplay, String map_size) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_INSERT_NEW_GAMEPLAY_MAP);
            prStmt.setInt(1, id_gameplay);
            prStmt.setString(2, map_size);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

    public String getMapSize(int id_gameplay) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_GET_GAMEPLAY_MAP);
            prStmt.setInt(1, id_gameplay);
            ResultSet rs = prStmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return "";
    }
}
