package prettyprint.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class PrettyPrintTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/digit-tsl-pretty-print-web/api/PrettyPrint/generatePdf";
        String requestJson = "C:\\Users\\simon.ghisalberti\\git\\digit-tsl\\digit-tsl-pretty-print-web\\src\\test\\resources\\BE.xml";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        String answer = restTemplate.postForObject(url, entity, String.class);

        if (answer != null) {
            System.out.println(answer);
        } else {
            System.out.println("FAILURE");
        }

    }

}
