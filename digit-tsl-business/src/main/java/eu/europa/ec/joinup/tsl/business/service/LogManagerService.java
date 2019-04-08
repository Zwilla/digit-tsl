/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
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

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

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
        logFiles.sort(new Comparator<LogFileDTO>() {
            @Override
            public int compare(LogFileDTO o1, LogFileDTO o2) {
                return o2.getLastModificationDate().compareTo(o1.getLastModificationDate());
            }
        });
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
    public Boolean deleteFile(String fileName) {
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
