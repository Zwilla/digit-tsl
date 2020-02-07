package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.FileRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.utils.Utils;

/**
 * Trusted list file service
 */
@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CountryService countryService;

    @Value("${tsl.folder}")
    private String folderPath;

    /**
     * Write XML file on file system
     *
     * @param byteArray
     * @param countryCode
     * @param filename
     */
    private String write(byte[] byteArray, String countryCode, String filename) {
        DBCountries country = countryService.getCountryByTerritory(countryCode);
        if (country != null) {
            String countryFolder = folderPath + File.separator + countryCode + File.separator;
            ensureFolderExists(countryFolder);
            String pathname = countryFolder + filename;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(pathname));
                Utils.write(byteArray, fos);
            } catch (Exception e) {
                LOGGER.error("Unable to store file : " + e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(fos);
            }
            return countryCode + File.separator + filename;
        } else {
            LOGGER.error("Country code does not exist : " + countryCode);
            return "";
        }
    }

    private String write(DSSDocument signedDoc, String countryCode, String filename) {
        String countryFolder = folderPath + File.separator + countryCode + File.separator;
        ensureFolderExists(countryFolder);
        String pathname = countryFolder + filename;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(pathname));
            signedDoc.writeTo(fos);
        } catch (Exception e) {
            LOGGER.error("Unable to store file : " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return countryCode + File.separator + filename;
    }

    /**
     * Verify that @path folder exist
     *
     * @param path
     */
    private void ensureFolderExists(String path) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
    }

    public File getTSLFile(DBFiles filesDB) {
        String localPath = filesDB.getLocalPath();
        return new File(folderPath + File.separator + localPath);
    }

    public String storeNewTL(DBFiles file, byte[] binaries, String countryCode) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return write(binaries, countryCode, simpleDateFormat.format(new Date()) + '.' + StringUtils.lowerCase(file.getMimeTypeFile().name()));
    }

    public String storeNewDraftTL(MimeType mimeType, DSSDocument doc, String countryCode, String localFile) {
        // EACH EDITION IN FEW DRAFT FILE
        if (StringUtils.isEmpty(localFile)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            localFile = simpleDateFormat.format(new Date()) + '_' + TLStatus.DRAFT + '.' + StringUtils.lowerCase(mimeType.name());
        } else {
            localFile = localFile.substring(localFile.indexOf(File.separator));
        }
        return write(doc, countryCode, localFile);
    }

    public String storeNewDraftTL(MimeType mimeType, byte[] binaries, String countryCode, String localFile) {
        // EACH EDITION IN FEW DRAFT FILE
        if (StringUtils.isEmpty(localFile)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            localFile = simpleDateFormat.format(new Date()) + '_' + TLStatus.DRAFT + '.' + StringUtils.lowerCase(mimeType.name());
        } else {
            localFile = localFile.substring(localFile.indexOf(File.separator));
        }
        return write(binaries, countryCode, localFile);
    }

    public String storeNewDraftNotification(MimeType mimeType, byte[] binaries, String countryCode, String localFile) {
        // EACH EDITION IN FEW DRAFT FILE
        if (StringUtils.isEmpty(localFile)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            localFile = simpleDateFormat.format(new Date()) + "_NOTIFICATION." + StringUtils.lowerCase(mimeType.name());
        } else {
            localFile = localFile.substring(localFile.indexOf(File.separator));
        }
        return write(binaries, countryCode, localFile);
    }

    public List<DBFiles> getFileByDigest(String digest) {
        LOGGER.debug("Digest to find : " + digest);
        return fileRepository.findByDigest(digest);
    }

    public DBFiles getFileById(int id) {
        return fileRepository.findOne(id);
    }

    public String getFilePath(DBFiles filesDB) {
        return folderPath + File.separator + filesDB.getLocalPath();
    }

    public void delete(DBFiles notificationFile) {
        File f = new File(folderPath + File.separatorChar + notificationFile.getLocalPath());
        if (f.exists()) {
            f.delete();
        }
    }

    public List<DBFiles> getProductionTLFilesByTerritoryOrderByFirstScanDate(DBCountries country) {
        return fileRepository.findByPublishedTerritoryOrderByFirstScanDate(country);
    }

}
