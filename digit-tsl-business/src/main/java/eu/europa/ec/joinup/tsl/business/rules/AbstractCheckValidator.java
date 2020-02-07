package eu.europa.ec.joinup.tsl.business.rules;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;

public abstract class AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCheckValidator.class);

    public abstract boolean isSupported(CheckDTO check);

    protected void addResult(CheckDTO check, String location, boolean valid, List<CheckResultDTO> results) {
        results.add(new CheckResultDTO(location, check, valid));
    }

    protected void runAsync(CheckDTO check, String location, Future<Boolean> futureValid, List<CheckResultDTO> results) {
        boolean result = false;
        try {
            while (true) {
                if (futureValid.isDone()) {
                    result = futureValid.get();
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve future value : " + e.getMessage());
        }
        addResult(check, location, result, results);
    }

    public List<CheckResultDTO> execute(CheckDTO check, TL previous, TL current) {
        if (!CheckStatus.IGNORE.equals(check.getStatus()) && (current != null) && isSupported(check)) {
            return validate(check, current);
        }
        return Collections.emptyList();
    }

    public abstract List<CheckResultDTO> validate(CheckDTO check, TL currentVersion);

}
