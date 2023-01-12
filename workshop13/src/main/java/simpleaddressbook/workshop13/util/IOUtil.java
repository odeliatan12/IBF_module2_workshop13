package simpleaddressbook.workshop13.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Creating a new directory for the file
public class IOUtil {
    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    // Creating a new directory for the file
    public static void createDir(String path) {
        File dir = new File(path);
        boolean isDirCreated = dir.mkdirs();
        logger.info("dir created > " + isDirCreated);
        if (isDirCreated) {
            // System is a final class in java that allows the user to get properties, set err, etc....
            // in order to add file into folder, must bypass the os secure system
            // step 1: getting the name of the os system (macos)
            String osName = System.getProperty("os.name");

            // Windows do not have the same properties as mac
            // So as long as the os is not windows, use this hack
            if (!osName.contains("Windows")) {
                try {

                    // There are three types of file permissions: read (r), write (w), and execute (x). These permissions can be set for three different groups: the owner of the file, members of the owner's group, and all other users.
                    // r = read, w = write, x = execute
                    //  in the string "rwxrwx---", the owner has read, write, and execute permissions (rwx), the group has read, write, and execute permissions (rwx), and other users have no permissions (-)
                    String perm = "rwxrwx---";

                    // since the permission are set to read(r), write(w), and execute(x) for the file for both the reader and writer, when it it is added to the permissions set, it will contain these permissions 
                    // PosixFilePermission.OWNER_READ
                    // PosixFilePermission.OWNER_WRITE
                    // PosixFilePermission.OWNER_EXECUTE
                    // PosixFilePermission.GROUP_READ
                    // PosixFilePermission.GROUP_WRITE
                    // PosixFilePermission.OTHERS_EXECUTE
                    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(perm);
                    Files.setPosixFilePermissions(dir.toPath(), permissions);
                } catch (IOException e) {
                    logger.error("Error ", e);
                }
            }
        }
    }
}
