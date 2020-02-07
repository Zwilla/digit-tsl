package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Column;
import eu.europa.ec.joinup.tsl.business.repository.ColumnsRepository;
import eu.europa.ec.joinup.tsl.model.DBColumnAvailable;

/**
 * Get attribute to show/hide on trust backbone. When hide DTO attribute(s) are not filled and column(s) are not displayed
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class BackboneColumnsService {

    @Autowired
    private ColumnsRepository columnsRepository;

    /**
     * Get column informations
     */
    public List<Column> getColumns() {
        List<Column> list = new ArrayList<>();
        List<DBColumnAvailable> colList = (List<DBColumnAvailable>) columnsRepository.findAll();
        for (DBColumnAvailable dbCol : colList) {
            Column usr = new Column(dbCol);
            list.add(usr);
        }
        return list;
    }

    /**
     * Check if column @name is set to visible in database
     *
     * @param name
     */
    public boolean getColumnVisible(String name) {
        DBColumnAvailable col = columnsRepository.findByCode(name);
        if (col != null) {
            return col.getVisible();
        }
        return false;
    }
}
