package eu.europa.ec.joinup.tsl.business.dto.data.tl;

import eu.europa.ec.joinup.tsl.model.DBHistory;

public class HistoryDataDTO extends ServiceHistoryAbstractDataDTO {

    private int id;
    private String historyId;

    public HistoryDataDTO(DBHistory dbHistory) {
        super(dbHistory);
        id = dbHistory.getId();
        historyId = dbHistory.getHistoryId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((historyId == null) ? 0 : historyId.hashCode());
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
        HistoryDataDTO other = (HistoryDataDTO) obj;
        if (historyId == null) {
            if (other.historyId != null) {
                return false;
            }
        } else if (!historyId.equals(other.historyId)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
