package eu.europa.ec.joinup.tsl.business.dto;

public class TLDifference {

    private String id;
    private String publishedValue;
    private String currentValue;
    private String hrLocation;

    public TLDifference() {
    }

    public TLDifference(String parent, String publishedValue, String currentValue) {
        this.setId(parent);
        this.setPublishedValue(publishedValue);
        this.setCurrentValue(currentValue);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishedValue() {
        return publishedValue;
    }

    public void setPublishedValue(String oldValue) {
        this.publishedValue = oldValue;
    }

    public String getHrLocation() {
        return hrLocation;
    }

    public void setHrLocation(String hrLocation) {
        this.hrLocation = hrLocation;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentValue == null) ? 0 : currentValue.hashCode());
        result = prime * result + ((hrLocation == null) ? 0 : hrLocation.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((publishedValue == null) ? 0 : publishedValue.hashCode());
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
        TLDifference other = (TLDifference) obj;
        if (currentValue == null) {
            if (other.currentValue != null)
                return false;
        } else if (!currentValue.equals(other.currentValue))
            return false;
        if (hrLocation == null) {
            if (other.hrLocation != null)
                return false;
        } else if (!hrLocation.equals(other.hrLocation))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (publishedValue == null) {
            if (other.publishedValue != null)
                return false;
        } else if (!publishedValue.equals(other.publishedValue))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TLDifference [id=" + id + ", publishedValue=" + publishedValue + ", currentValue=" + currentValue + ", hrLocation=" + hrLocation + "]";
    }

}
