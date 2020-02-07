package eu.europa.ec.joinup.tsl.business.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

/**
 * PrettyPrint service
 */
@Service
@Transactional
public class TLPrettyPrintService {

    @Autowired
    private TLService tlService;

    @Value("${pretty.print.url}")
    private String prettyPrintUrl;

    /**
     * Post prettyPrint request and return PDF result
     *
     * @param tlId
     * @return
     */
    public String triggerPrettyPrint(int tlId) {
        DBTrustedLists tl = tlService.getDbTL(tlId);
        if (tl.getXmlFile() != null) {
            DBFiles xmlFile = tl.getXmlFile();
            String xmlPath = xmlFile.getLocalPath();
            if (xmlPath != null) {
                RestTemplate restTemplate = new RestTemplate();

                String url = prettyPrintUrl;
                String requestJson = xmlPath;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
                String answer = restTemplate.postForObject(url, entity, String.class);

                if (answer != null) {
                    return answer;
                }
            }
        }
        return null;
    }

}
