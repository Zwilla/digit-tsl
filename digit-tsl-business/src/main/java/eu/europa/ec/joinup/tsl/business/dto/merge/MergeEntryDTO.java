package eu.europa.ec.joinup.tsl.business.dto.merge;

import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class MergeEntryDTO {

    private MergeStatus status;
    private Tag tag;
    private Object objectChanged;
    private String draft;

    public MergeEntryDTO(MergeStatus status, Tag tag, Object objectChanged, String draft) {
        super();
        this.status = status;
        this.tag = tag;
        this.objectChanged = objectChanged;
        this.draft = draft;
    }

    public MergeStatus getStatus() {
        return status;
    }

    public void setStatus(MergeStatus status) {
        this.status = status;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Object getObjectChanged() {
        return objectChanged;
    }

    public void setObjectChanged(Object objectChanged) {
        this.objectChanged = objectChanged;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    @Override
    public String toString() {
        return "MergeEntryDTO [status=" + status + ", tag=" + tag + ", draft=" + draft + "]";
    }

}
