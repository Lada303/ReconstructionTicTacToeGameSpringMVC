package Lada303.dao.imp;

import Lada303.dao.ConnectorDB;
import Lada303.dao.WinnerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WinnerDAOImp implements WinnerDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WinnerDAOImp.class);
    private static final String SQL_INSERT_NEW_GAMEPLAY_WINNER =
            "INSERT INTO winners (id_gameplay, winner_player_id) VALUES (?, ?);";
    private static final String SQL_GET_GAMEPLAY_WINNER =
            "SELECT winner_player_id FROM winners WHERE id_gameplay = ?;";

    private final ConnectorDB db;

    @Autowired
    public WinnerDAOImp(ConnectorDB db)  {
        this.db = db;
    }

    @Override
    public void addNewWinner(int id_gameplay, int id_winner) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_INSERT_NEW_GAMEPLAY_WINNER);
            prStmt.setInt(1, id_gameplay);
            prStmt.setInt(2, id_winner);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

    @Override
    public int getWinner(int id_gameplay) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_GET_GAMEPLAY_WINNER);
            prStmt.setInt(1, id_gameplay);
            ResultSet rs = prStmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return 0;
    }
}
