package eu.europa.ec.joinup.tsl.business.dto.data.retention;

public class RetentionAlertDTO {

    private int nbDraftstore;
    private int nbTLs;
    private int nbDraftstoreDeleted;
    private int nbTLsDeleted;

    public RetentionAlertDTO(int nbDraftstore, int nbTLs, int nbDraftstoreDeleted, int nbTLsDeleted) {
        super();
        this.nbDraftstore = nbDraftstore;
        this.nbTLs = nbTLs;
        this.nbDraftstoreDeleted = nbDraftstoreDeleted;
        this.nbTLsDeleted = nbTLsDeleted;
    }

    public int getNbDraftstore() {
        return nbDraftstore;
    }

    public void setNbDraftstore(int nbDraftstore) {
        this.nbDraftstore = nbDraftstore;
    }

    public int getNbTLs() {
        return nbTLs;
    }

    public void setNbTLs(int nbTLs) {
        this.nbTLs = nbTLs;
    }

    public int getNbDraftstoreDeleted() {
        return nbDraftstoreDeleted;
    }

    public void setNbDraftstoreDeleted(int nbDraftstoreDeleted) {
        this.nbDraftstoreDeleted = nbDraftstoreDeleted;
    }

    public int getNbTLsDeleted() {
        return nbTLsDeleted;
    }

    public void setNbTLsDeleted(int nbTLsDeleted) {
        this.nbTLsDeleted = nbTLsDeleted;
    }

}
