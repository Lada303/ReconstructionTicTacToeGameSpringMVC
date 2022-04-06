package Lada303.dao.imp;

import Lada303.dao.ConnectorDB;
import Lada303.dao.StepDAO;
import Lada303.models.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StepDAOImp implements StepDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepDAOImp.class);
    private static final String SQL_INSERT_NEW_GAMEPLAY_STEP =
            "INSERT INTO steps (id_gameplay, step_number, step_player_id, step_value) VALUES ( ?, ?, ?, ?);";
    private static final String SQL_GET_ALL_GAMEPLAY_STEP =
            "SELECT step_number, step_player_id, step_value FROM steps WHERE id_gameplay = ?;";


    private final ConnectorDB db;

    @Autowired
    public StepDAOImp(ConnectorDB db)  {
        this.db = db;
    }

    public void addNewStep(int id_gameplay, Step step) {
        db.setConnection();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_INSERT_NEW_GAMEPLAY_STEP);
            prStmt.setInt(1, id_gameplay);
            prStmt.setInt(2, step.getNumber());
            prStmt.setInt(3, step.getPlayerId());
            prStmt.setString(4, step.getCellValue());
            prStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
    }

    public List<Step> getAllStepGameplay(int id_gameplay) {
        db.setConnection();
        List<Step> listStep = new ArrayList<>();
        PreparedStatement prStmt;
        try {
            prStmt = db.getConnection().prepareStatement(SQL_GET_ALL_GAMEPLAY_STEP);
            prStmt.setInt(1, id_gameplay);
            ResultSet rs = prStmt.executeQuery();
            while(rs.next()) {
                Step step = new Step();
                step.setNumber(rs.getInt(1));
                step.setPlayerId(rs.getInt(2));
                step.setCellValue(rs.getString(3));
                listStep.add(step);
            }
        } catch (SQLException e) {
            LOGGER.error("Exc: " + e.getMessage());
        }
        return listStep;
    }

}
