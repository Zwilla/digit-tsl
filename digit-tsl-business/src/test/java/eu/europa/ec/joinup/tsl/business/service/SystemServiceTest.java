package eu.europa.ec.joinup.tsl.business.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Column;
import eu.europa.ec.joinup.tsl.business.repository.ColumnsRepository;
import eu.europa.ec.joinup.tsl.model.DBColumnAvailable;
import eu.europa.ec.joinup.tsl.model.enums.ColumnName;

public class SystemServiceTest extends AbstractSpringTest {

    @Autowired
    private BackboneColumnsService systemService;

    @Autowired
    private ColumnsRepository columnsRepository;

    @Before
    public void init() {
        DBColumnAvailable cContact = new DBColumnAvailable();
        cContact.setCode(ColumnName.CONTACTS.toString());
        cContact.setVisible(true);

        DBColumnAvailable cAvailability = new DBColumnAvailable();
        cAvailability.setCode(ColumnName.AVAILABILITY.toString());
        cAvailability.setVisible(false);

        columnsRepository.save(cContact);
        columnsRepository.save(cAvailability);
    }

    @Test
    public void getColumns() {
        List<Column> columns = systemService.getColumns();
        Assert.assertNotNull(columns);
        Assert.assertEquals(3, columns.size());
    }

    @Test
    public void getColumnVisible() {
        Assert.assertTrue(systemService.getColumnVisible(ColumnName.CONTACTS.toString()));
        Assert.assertFalse(systemService.getColumnVisible(ColumnName.AVAILABILITY.toString()));
    }

}
