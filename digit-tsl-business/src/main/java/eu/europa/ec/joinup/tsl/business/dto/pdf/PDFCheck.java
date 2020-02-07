package eu.europa.ec.joinup.tsl.business.dto.pdf;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

@XStreamAlias("grouped-checks")
public class PDFCheck {

    private List<CheckDTO> checks;
    private int number;
    private String tlLocationId;

    public PDFCheck() {
        checks = new ArrayList<>();
        number = 0;
    }

    public void add(CheckResultDTO checkResult) {
        CheckDTO checkDTO = new CheckDTO(checkResult);
        checks.add(checkDTO);
        number++;
        tlLocationId = checkResult.getLocation();
    }

    public void add(CheckDTO check) {
        checks.add(check);
        number++;
        tlLocationId = check.getHrLocation();
    }

    public String getLocationId() {
        if (!checks.isEmpty()) {
            return checks.get(0).getId();
        } else {
            return null;
        }
    }

    public CheckStatus getStatus() {
        if (!checks.isEmpty()) {
            return checks.get(0).getStatus();
        } else {
            return null;
        }
    }

    public List<CheckDTO> getChecks() {
        return checks;
    }

    public void setChecks(List<CheckDTO> checks) {
        this.checks = checks;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTlLocationId() {
        return tlLocationId;
    }

    public void setTlLocationId(String tlLocationId) {
        this.tlLocationId = tlLocationId;
    }

}
