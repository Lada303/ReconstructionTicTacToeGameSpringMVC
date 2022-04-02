package Lada303.exeptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameDataException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(GameDataException.class);

    public GameDataException(String message) {
        super(message);
        logger.error("ErrorLog: ", this);
    }
}
