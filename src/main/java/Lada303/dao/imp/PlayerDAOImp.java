package Lada303.dao.imp;

import Lada303.dao.ConnectorDB;
import Lada303.dao.PlayerDAO;
import Lada303.models.players.Gamer;
import Lada303.models.players.HumanGamer;
import Lada303.utils.enums.Dots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerDAOImp implements PlayerDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDAOImp.class);
    private static final String SQL_INSERT_NEW_GAMEPLAY_PLAYER =
            "INSERT INTO players (id_gameplay, player_id, player_name, player_symbol, player_score) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_PLAYER_SCORE =
            "UPDATE players SET player_score = player_score + 1 WHERE id_gameplay = ? and player_id = ?;";
    private static final String SQL_GET_GAMEPLAY_PLAYER =
            "SELECT player_id, player_name, player_symbol FROM players WHERE id_gameplay = ? and player_id = ? ";


    private final ConnectorDB db;

    @Autowired
    public PlayerDAOImp(ConnectorDB db)  {
        this.db = db;
    }

    public void addNewGameplay(int id_gameplay, Gamer player) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_INSERT_NEW_GAMEPLAY_PLAYER);
            prStmt.setInt(1, id_gameplay);
            prStmt.setInt(2, player.getId());
            prStmt.setString(3, player.getName());
            prStmt.setString(4, player.getDots().name());
            prStmt.setInt(5, player.getScore());
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

    public void incrementScore(int id_gameplay, int player_id) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_UPDATE_PLAYER_SCORE);
            prStmt.setInt(1, id_gameplay);
            prStmt.setInt(2, player_id);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

    public Gamer getPlayerByItId(int id_gameplay, int player_id) {
        db.setConnection();
        Gamer player = null;
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_GET_GAMEPLAY_PLAYER);
            prStmt.setInt(1, id_gameplay);
            prStmt.setInt(2, player_id);
            ResultSet rs = prStmt.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String symbol = rs.getString(3);
            player = new HumanGamer(id, name, (symbol.equalsIgnoreCase("x") ? Dots.X : Dots.O));
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return player;
    }

}

