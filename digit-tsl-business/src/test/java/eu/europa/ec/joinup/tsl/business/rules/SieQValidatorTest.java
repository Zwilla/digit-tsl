package eu.europa.ec.joinup.tsl.business.rules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.CheckService;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class SieQValidatorTest extends AbstractSpringTest {

    @Autowired
    private SieQValidator sieQValidator;

    @Autowired
    private CheckService checkService;

    @Test
    public void testExtension() throws IOException {
        List<CheckResultDTO> results = new ArrayList<>();
        TL tl = fileToTL(1, "src/test/resources/SIEQ_TEST.xml");
        List<CheckDTO> checks = checkService.getTarget(Tag.SIEQ_CHECK);
        for (CheckDTO check : checks) {
            results.addAll(sieQValidator.validate(check, tl));
        }
    }
}
