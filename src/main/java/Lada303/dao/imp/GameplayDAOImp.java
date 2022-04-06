package Lada303.dao.imp;

import Lada303.dao.ConnectorDB;
import Lada303.dao.GameplayDAO;
import Lada303.models.Gameplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameplayDAOImp implements GameplayDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayDAOImp.class);
    private static final String SQL_INSERT_NEW_GAMEPLAY =
            "INSERT INTO gameplay (name_gameplay, map_size, dots_to_win) VALUES (?, ?, ?);";
    private static final String SQL_GET_LAST_GAMEPLAY = "SELECT max(id_gameplay) FROM gameplay;";
    private static final String SQL_GET_ALL_GAMEPLAY =
            "SELECT * FROM gameplay";
    private static final String SQL_GET_GAMEPLAY_MAP_SIZE =
            "SELECT map_size FROM gameplay WHERE id_gameplay = ?;";

    private final ConnectorDB db;

    @Autowired
    public GameplayDAOImp(ConnectorDB db)  {
        this.db = db;
    }

    @Override
    public void addNewGameplay(Gameplay gameplay) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_INSERT_NEW_GAMEPLAY);
            prStmt.setString(1, gameplay.getName());
            prStmt.setString(2, gameplay.getMapSize());
            prStmt.setInt(3, gameplay.getDots_to_win());
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

    @Override
    public int getLastGameplayId() {
        db.setConnection();
        int id = 0;
        Statement stmt;
        try {
            stmt = db.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(SQL_GET_LAST_GAMEPLAY);
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return id;
    }

    @Override
    public List<Gameplay> getAllGameplay() {
        db.setConnection();
        List<Gameplay> listGameplay = new ArrayList<>();
        Statement stmt;
        try {
            stmt = db.getConnection().createStatement();
            ResultSet rs  = stmt.executeQuery(SQL_GET_ALL_GAMEPLAY);
            while(rs.next()) {
                Gameplay gameplay = new Gameplay();
                gameplay.setId(rs.getInt(1));
                gameplay.setName(rs.getString(2));
                gameplay.setMapSize(rs.getString(3));
                gameplay.setDots_to_win(rs.getInt(4));
                listGameplay.add(gameplay);
            }
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return listGameplay;
    }

    @Override
    public String getGameplayMapSize(int id_gameplay) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_GET_GAMEPLAY_MAP_SIZE);
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
