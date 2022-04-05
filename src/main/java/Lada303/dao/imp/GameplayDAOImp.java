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
    private static final String SQL_INSERT_NEW_GAMEPLAY = "INSERT INTO gameplay (name_gameplay) VALUES (?);";
    private static final String SQL_GET_LAST_GAMEPLAY = "SELECT max(id_gameplay) FROM gameplay;";
    private static final String SQL_GET_ALL_GAMEPLAY =
            "SELECT gameplay.id_gameplay, name_gameplay, map_size FROM gameplay JOIN maps USING(id_gameplay)";

    private final ConnectorDB db;

    @Autowired
    public GameplayDAOImp(ConnectorDB db)  {
        this.db = db;
    }

    public void addNewGameplay(String name_gameplay) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_INSERT_NEW_GAMEPLAY);
            prStmt.setString(1, name_gameplay);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

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

    public List<Gameplay> getAllGameplay() {
        db.setConnection();
        List<Gameplay> listGameplay = new ArrayList<>();
        Statement stmt;
        try {
            stmt = db.getConnection().createStatement();
            ResultSet rs  = stmt.executeQuery(SQL_GET_ALL_GAMEPLAY);
            while(rs.next()) {
                Gameplay gameplay = new Gameplay();
                gameplay.setId_gameplay(rs.getInt(1));
                gameplay.setName_gameplay(rs.getString(2));
                gameplay.setMap_size(rs.getString(3));
                listGameplay.add(gameplay);
            }
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return listGameplay;
    }
}
