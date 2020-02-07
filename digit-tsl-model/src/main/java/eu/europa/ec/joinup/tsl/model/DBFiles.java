package eu.europa.ec.joinup.tsl.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.MimeType;

@Entity
@Table(name = "TL_FILES")
public class DBFiles {

    @Id
    @Column(name = "FILE_ID", unique = true, nullable = false)
    @GeneratedValue
    private int id;

    @Column(name = "URL")
    private String url;

    @Column(name = "LOCAL_PATH")
    private String localPath;

    @Column(name = "DIGEST")
    private String digest;

    @Column(name = "FIRST_SCAN_DATE")
    private Date firstScanDate;

    @Column(name = "LAST_SCAN_DATE")
    private Date lastScanDate;

    @Column(name = "MIME_TYPE")
    @Enumerated(EnumType.STRING)
    private MimeType mimeTypeFile;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private DBSignatureInformation signatureInformation;

    // Order by check date DESC in getter (@OrderBy not working)
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DBFilesAvailability> availabilityInfos;

    public DBFiles() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DBFiles other = (DBFiles) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Date getFirstScanDate() {
        return firstScanDate;
    }

    public void setFirstScanDate(Date firstScanDate) {
        this.firstScanDate = firstScanDate;
    }

    public Date getLastScanDate() {
        return lastScanDate;
    }

    public void setLastScanDate(Date lastScanDate) {
        this.lastScanDate = lastScanDate;
    }

    public MimeType getMimeTypeFile() {
        return mimeTypeFile;
    }

    public void setMimeTypeFile(MimeType mimeTypeFile) {
        this.mimeTypeFile = mimeTypeFile;
    }

    public DBSignatureInformation getSignatureInformation() {
        return signatureInformation;
    }

    public void setSignatureInformation(DBSignatureInformation signatureInformation) {
        this.signatureInformation = signatureInformation;
    }

    public List<DBFilesAvailability> getAvailabilityInfos() {
        if (availabilityInfos == null) {
            return Collections.emptyList();
        }
        Collections.sort(availabilityInfos, new Comparator<DBFilesAvailability>() {
            @Override
            public int compare(DBFilesAvailability o1, DBFilesAvailability o2) {
                return (o2.getCheckDate().compareTo(o1.getCheckDate()));
            }
        });
        return availabilityInfos;
    }

    public void setAvailabilityInfos(List<DBFilesAvailability> availabilityInfos) {
        this.availabilityInfos = availabilityInfos;
    }

}
