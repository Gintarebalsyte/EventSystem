package lt.vu.mif.eventsystem.bl.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {
    public void validate(boolean expectedCondition, String errorMessage) throws IllegalArgumentException {
        if (!expectedCondition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
