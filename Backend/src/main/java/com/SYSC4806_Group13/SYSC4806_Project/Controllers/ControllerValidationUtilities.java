package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.BadAttributeException;
import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.MissingAttributeException;

import java.util.Map;

public class ControllerValidationUtilities {

    /**
     * Returns the validated string attribute after confirming it is not null and not blank
     *
     * @param attributeName The name of the attribute
     * @param value         The value of the attribute
     * @return The validated attribute
     */
    public static String getValidatedStringAttribute_NonNullNonBlank(String attributeName, String value) {
        if (value == null) {
            throw new MissingAttributeException("Request body must contain '" + attributeName + "'");
        }
        if (value.isBlank()) {
            throw new BadAttributeException("Attribute '" + attributeName + "' cannot be empty");
        }
        return value;
    }

    public static Object getValidatedAttribute_NonNull(String attributeName, Object value) {
        if (value == null) {
            throw new MissingAttributeException("Request body must contain '" + attributeName + "'");
        }
        return value;
    }

    public static Long getValidatedLongAttribute_positiveOnly(String attributeName, Long value, boolean isZeroInvalid) {
        if (value == null) {
            throw new MissingAttributeException("Request body must contain '" + attributeName + "'");
        }
        if (value < 0) {
            throw new BadAttributeException("Attribute '" + attributeName + "' cannot be negative");
        }
        if (isZeroInvalid && value == 0) {
            throw new BadAttributeException("Attribute '" + attributeName + "' cannot be zero");
        }
        return value;
    }

    public static Integer getValidatedIntegerAttribute_positiveOnly(String attributeName, Integer value, boolean isZeroInvalid) {
        if (value == null) {
            throw new MissingAttributeException("Request body must contain '" + attributeName + "'");
        }
        if (value < 0) {
            throw new BadAttributeException("Attribute '" + attributeName + "' cannot be negative");
        }
        if (isZeroInvalid && value == 0) {
            throw new BadAttributeException("Attribute '" + attributeName + "' cannot be zero");
        }
        return value;
    }

    public static boolean setValidatedStringAttributeToMap(String key, String value, Map map) {
        if (value != null) {
            if (value.isBlank()) {
                throw new BadAttributeException("Attribute '" + key + "' cannot be empty");
            }
            map.put(key, value);
            return true;
        }
        return false;
    }

    public static boolean setValidatedIntegerAttributeToMap(String key, Integer value, Map map, boolean isZeroInvalid) {
        if (value != null) {
            if (value < 0) {
                throw new BadAttributeException("Attribute '" + key + "' cannot be negative");
            }
            if (isZeroInvalid && value == 0) {
                throw new BadAttributeException("Attribute '" + key + "' cannot be zero");
            }
            map.put(key, value);
            return true;
        }
        return false;
    }


}
