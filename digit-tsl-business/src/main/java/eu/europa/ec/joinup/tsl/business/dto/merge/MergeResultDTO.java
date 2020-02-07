package eu.europa.ec.joinup.tsl.business.dto.merge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class MergeResultDTO {

    private Map<String, List<MergeEntryDTO>> mergeResult;

    public MergeResultDTO() {
        super();
        this.mergeResult = new HashMap<>();
    }

    public void addEntry(String location, Tag tag, MergeStatus status, Object obj, String draft) {
        if (!mergeResult.containsKey(location)) {
            mergeResult.put(location, new ArrayList<MergeEntryDTO>());
        }
        MergeEntryDTO mergeEntry = new MergeEntryDTO(status, tag, obj, draft);
        mergeResult.get(location).add(mergeEntry);
    }

    public Map<String, List<MergeEntryDTO>> getMergeResult() {
        return mergeResult;
    }

    public void setMergeResult(Map<String, List<MergeEntryDTO>> mergeResult) {
        this.mergeResult = mergeResult;
    }
}
