package controller;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import service.PrettyPrintService;
import tsl.plugtest.tslhtmlprettyprint.ExportPDFA;
import tsl.plugtest.tslhtmlprettyprint.Main;

public class PrettyPrintController implements PrettyPrintService {

    @Value("${tsl.folder}")
    private String folderPath;

    @Override
    public String generatePdf(String xmlPath) {
        try {
            Resource res = new ClassPathResource("ccProperties.xml");
            File propertyFilePath = res.getFile();
            File absoluteFolderPathTmp = new File(folderPath);
            File absoluteFolderPath = new File(absoluteFolderPathTmp, xmlPath);

            String xmlPathFolder = absoluteFolderPath.getAbsolutePath();
            startMain(propertyFilePath, xmlPathFolder, xmlPathFolder + ".html", xmlPathFolder + ".pdf");

            return xmlPath + ".pdf";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param args
     *            the command line arguments
     */
    private void startMain(File ccPropertiesPath, String xmlFilePath, String htmlFilePath, String pdfFilePath) throws Exception {
        FileInputStream fis = new FileInputStream(ccPropertiesPath);

        File dir = ccPropertiesPath.getParentFile();
        String propertyAbsolutePath = dir.getAbsolutePath();

        Main m = new Main();

        // Override properties hard-coded
        Properties prop = getPrivateAttribute("prop", m, Properties.class);
        assert prop != null;
        prop.loadFromXML(fis);
        prop.setProperty("imagePath", propertyAbsolutePath + File.separator + "res");
        prop.setProperty("pdfImagePath", propertyAbsolutePath + File.separator + "res");
        prop.setProperty("fontPathAndName", propertyAbsolutePath + File.separator + "res" + File.separator + "times.ttf");
        prop.setProperty("srgbPath", propertyAbsolutePath + File.separator + "res" + File.separator + "srgb.profile");

        // Override resourceBundle not reset
        setValueToPrivateAttribute("descriptionBundleList", new ArrayList<ResourceBundle>());

        fis.close();

        genericInvokMethod(Main.class, "InitializeVectors", prop);
        genericInvokMethod(Main.class, "InitializeX509ExtensionsHashTable", (Object) (Object) null);

        setValueToPrivateAttribute("outFileNamePdf", pdfFilePath);

        File tslFile = new File((xmlFilePath));
        Object[] readTSLParams = new Object[2];
        readTSLParams[0] = tslFile;
        readTSLParams[1] = (htmlFilePath);
        genericInvokMethod(m, "readTSL", readTSLParams);

        ExportPDFA ep = getPrivateAttribute("ep", m, ExportPDFA.class);
        if (ep != null) {
            ep.ClosePDFA();
        }
    }

    private void setValueToPrivateAttribute(String attributeName, Object newValue) {
        try {
            Field privateField = Main.class.getDeclaredField(attributeName);
            privateField.setAccessible(true);
            privateField.set(Main.class, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getPrivateAttribute(String attributeName, Object target, Class<T> type) {
        try {
            Field privateField = Main.class.getDeclaredField(attributeName);
            privateField.setAccessible(true);
            Object value = privateField.get(target);
            return (T) value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void genericInvokMethod(Object m, String methodName, Object... params) throws Exception {
        if (params != null) {
            int paramCount = params.length;
            Class<?>[] classArray = new Class<?>[paramCount];
            for (int i = 0; i < paramCount; i++) {
                classArray[i] = params[i].getClass();
            }
            Method method = Main.class.getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            method.invoke(m, params);
        } else {
            Method method = Main.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(m);
        }
    }

}
