package Lada303.exсeptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadFileException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(UploadFileException.class);

    public UploadFileException(String message) {
        super(message);
        logger.error("ErrorLog: ", this);
    }
}
