package eu.europa.ec.joinup.tsl.business.dto.merge;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class TLMergeResultDTO {

    private MergeResultDTO siResult;
    private MergeResultDTO pointerResult;
    private MergeResultDTO tspResult;
    private Set<Integer> draftIds;

    public TLMergeResultDTO() {
        super();
        this.siResult = new MergeResultDTO();
        this.pointerResult = new MergeResultDTO();
        this.tspResult = new MergeResultDTO();
        this.draftIds = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public String getDraftIdsConcat() {
        StringBuilder concat = new StringBuilder();
        Iterator<Integer> iterator = draftIds.iterator();
        while (iterator.hasNext()) {
            concat.append(iterator.next());
            if (iterator.hasNext()) {
                concat.append("+");
            }
        }
        return concat.toString();
    }

    public MergeResultDTO getSiResult() {
        return siResult;
    }

    public void setSiResult(MergeResultDTO siResult) {
        this.siResult = siResult;
    }

    public MergeResultDTO getPointerResult() {
        return pointerResult;
    }

    public void setPointerResult(MergeResultDTO pointerResult) {
        this.pointerResult = pointerResult;
    }

    public MergeResultDTO getTspResult() {
        return tspResult;
    }

    public void setTspResult(MergeResultDTO tspResult) {
        this.tspResult = tspResult;
    }

    public Set<Integer> getDraftIds() {
        return draftIds;
    }

    public void setDraftIds(Set<Integer> draftIds) {
        this.draftIds = draftIds;
    }
}
