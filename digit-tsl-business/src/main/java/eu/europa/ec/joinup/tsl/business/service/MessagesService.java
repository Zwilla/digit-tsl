package eu.europa.ec.joinup.tsl.business.service;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

/**
 * Loop through messages.properties file and return file as angular $scope property in string
 */
@Service
public class MessagesService {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @SuppressWarnings("rawtypes")
    public StringBuffer getMessagesBundle() {
        StringBuffer sb = new StringBuffer();
        Enumeration bundleKeys = bundle.getKeys();
        while (bundleKeys.hasMoreElements()) {

            String key = (String) bundleKeys.nextElement();
            String value = bundle.getString(key);
            sb.append("$scope.");
            key = key.replace(".", "_");
            sb.append(key);
            sb.append(" = \"");
            sb.append(value);
            sb.append("\";\n");

        }

        return sb;

    }
}
