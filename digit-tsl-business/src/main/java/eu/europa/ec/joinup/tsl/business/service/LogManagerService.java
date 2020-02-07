package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.LogFileDTO;

/**
 * Log TLM in a specific folder that can be browse/manage in the application
 */
@Service
public class LogManagerService {

    @Value("${logs.folder}")
    private String logFolderPath;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    /**
     * Look into @logFolderPath and return list of file order by last modification date;
     *
     * @return
     */
    public List<LogFileDTO> getAllLogs() {
        List<LogFileDTO> logFiles = new ArrayList<>();
        File[] files = new File(logFolderPath).listFiles();
        if ((files == null) || (files.length == 0)) {
            return logFiles;
        } else {
            for (File file : files) {
                logFiles.add(new LogFileDTO(file));
            }
        }
        logFiles.sort((o1, o2) -> o2.getLastModificationDate().compareTo(o1.getLastModificationDate()));
        return logFiles;
    }

    /**
     * Delete log file
     *
     * @param fileName
     * @exception SecurityException
     *                The file is currently writing or reading
     * @return
     */
    public boolean deleteFile(String fileName) {
        File file = new File(logFolderPath + File.separatorChar + fileName);
        if (file.exists()) {
            try {
                return file.delete();
            } catch (SecurityException e) {
                throw new SecurityException(bundle.getString("error.log.delete"), e);
            }
        } else {
            throw new IllegalStateException(bundle.getString("error.log.not.exist").replaceAll("%FILE_NAME%", fileName));
        }
    }

    /**
     * Get byte[] from log file
     *
     * @param fileName
     * @return
     */
    public byte[] downloadFile(String fileName) {
        File file = new File(logFolderPath + File.separatorChar + fileName);
        if (file.exists()) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                throw new IllegalStateException(bundle.getString("error.log.read"), e);
            }
        } else {
            throw new IllegalStateException(bundle.getString("error.log.not.exist"));
        }

    }

}
