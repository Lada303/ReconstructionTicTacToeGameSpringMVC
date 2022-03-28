package Lada303.services;

import java.io.File;

public class ServerPath {
    public static final String SERVER_DIR = "C:\\server\\";
    public static final String SERVER_DIR_UPLOAD = "C:\\server\\uploadFile\\";
    public static final String SERVER_DIR_WRITE = "C:\\server\\writeFile";

    {
        File dir = new File(SERVER_DIR);
        File dir_upload = new File(SERVER_DIR_UPLOAD);
        File dir_write = new File(SERVER_DIR_WRITE);
        if(!dir.exists()) {
            dir.mkdir();
        }
        if(!dir_upload.exists()) {
            dir_upload.mkdir();
        }
        if(!dir_write.exists()) {
            dir_write.mkdir();
        }
    }
}
