package eu.europa.ec.joinup.tsl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.europa.ec.joinup.tsl.model.enums.CheckImpact;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Entity
@Table(name = "TL_CHECKS")
public class DBCheck {

    @Id
    @Column(name = "CHECK_ID", nullable = false, updatable = false)
    private String id;

    @Column(name = "DESCRIPTION", length = 4096, nullable = false, updatable = false)
    private String description;

    @Column(name = "TRANSLATION", length = 2000)
    private String translation;

    @Column(name = "STANDARD_REFERENCE", length = 200)
    private String standardReference;

    @Column(name = "TARGET", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Tag target;

    @Column(name = "NAME", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private CheckName name;

    @Column(name = "PRIORITY", nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckStatus priority;

    @Column(name = "IMPACT")
    @Enumerated(EnumType.STRING)
    private CheckImpact impact;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CheckType type;

    public DBCheck() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tag getTarget() {
        return target;
    }

    public void setTarget(Tag target) {
        this.target = target;
    }

    public CheckName getName() {
        return name;
    }

    public void setName(CheckName name) {
        this.name = name;
    }

    public CheckStatus getPriority() {
        return priority;
    }

    public void setPriority(CheckStatus priority) {
        this.priority = priority;
    }

    public CheckImpact getImpact() {
        return impact;
    }

    public void setImpact(CheckImpact impact) {
        this.impact = impact;
    }

    public CheckType getType() {
        return type;
    }

    public void setType(CheckType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getStandardReference() {
        return standardReference;
    }

    public void setStandardReference(String standardReference) {
        this.standardReference = standardReference;
    }

    @Override
    public String toString() {
        return "DBCheck [id=" + id + ", target=" + target + ", name=" + name + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((impact == null) ? 0 : impact.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + ((standardReference == null) ? 0 : standardReference.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        result = prime * result + ((translation == null) ? 0 : translation.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DBCheck other = (DBCheck) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (impact != other.impact)
            return false;
        if (name != other.name)
            return false;
        if (priority != other.priority)
            return false;
        if (standardReference == null) {
            if (other.standardReference != null)
                return false;
        } else if (!standardReference.equals(other.standardReference))
            return false;
        if (target != other.target)
            return false;
        if (translation == null) {
            if (other.translation != null)
                return false;
        } else if (!translation.equals(other.translation))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
