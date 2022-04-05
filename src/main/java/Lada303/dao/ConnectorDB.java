package Lada303.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
@Scope("singleton")
public class ConnectorDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectorDB.class);

    private final DriverManagerDataSource dataSource;
    private Connection connection;

    @Autowired
    public ConnectorDB(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
        setConnection();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection() {
        try {
            if (this.isNoConnected()) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            LOGGER.error("DB not connected : " + e.getMessage());
        }
        LOGGER.debug("DB connected");
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("ErrorLog: " + e.getMessage());
        }
    }

    public boolean isNoConnected() throws SQLException {
        return connection == null || connection.isClosed();
    }
}
