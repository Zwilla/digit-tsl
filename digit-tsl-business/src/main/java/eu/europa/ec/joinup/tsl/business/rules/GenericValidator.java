package eu.europa.ec.joinup.tsl.business.rules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GenericValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericValidator.class);

    private static final String COUNTRY_NAME_SPACE = "[name of the relevant Member State]";

    public boolean isPresent(Object object) {
        return object != null;
    }

    public boolean isPositiveNumber(Integer integer) {
        return ((integer != null) && (integer > 0));
    }

    public boolean isEquals(String expectedString, String value) {
        return StringUtils.equals(expectedString, value);
    }

    public boolean isNotEmpty(String value) {
        return StringUtils.isNotEmpty(value);
    }

    public boolean isCollectionNotEmpty(List<?> objects) {
        return CollectionUtils.isNotEmpty(objects);
    }

    public boolean isNumeric(String value) {
        return StringUtils.isNumeric(value) && isNotEmpty(value);
    }

    public boolean isLowerCase(String value) {
        return StringUtils.equals(value, StringUtils.lowerCase(value)) && isNotEmpty(value);
    }

    public boolean isIn(String value, List<String> allowedValues) {
        return (allowedValues != null) && allowedValues.contains(value);
    }

    public boolean isEquals(Integer expected, Integer value) {
        return (expected != null) && (value != null) && (expected.intValue() == value.intValue());
    }

    public boolean isEqualsIgnoreWhiteSpaces(String expectedText, String value) {
        return isEquals(clean(expectedText), clean(value));
    }

    public String clean(String original) {
        if (original == null) {
            return null;
        } else {
            return StringUtils.deleteWhitespace(original.replaceAll("'", "-").replaceAll("[^\\x00-\\x7F]", "-"));
        }
    }

    /*
     * Not Used in V5 anymore
     */
    public boolean isMatch(String expectedText, String value) {
        Pattern pattern = prepareRegex(expectedText);
        String cleaned = cleanDoubleSpacesAndCarriageReturns(value);
        Matcher matcher = pattern.matcher(cleaned);
        boolean find = matcher.find();
        if (!find) {
            LOGGER.debug("'" + cleaned + "' doesnt match '" + expectedText + "'");
        }
        return find;
    }

    private String cleanDoubleSpacesAndCarriageReturns(String value) {
        return value.replace("\r\n", " ").replace("\n", " ").replace("\t", " ").replace("  ", " ");
    }

    private Pattern prepareRegex(String expectedText) {
        String regex = expectedText.replace(COUNTRY_NAME_SPACE, ".+"); // one or more words country name
        return Pattern.compile(regex);
    }

}
