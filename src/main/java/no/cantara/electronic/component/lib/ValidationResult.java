package no.cantara.electronic.component.lib;

import java.util.Map;

/**
 * Result of validating component specifications.
 *
 * Contains validation errors (problems that must be fixed) and warnings
 * (potential issues that should be reviewed).
 *
 * @param valid Whether the component passes validation (no errors)
 * @param errors Map of field name to error message for validation failures
 * @param warnings Map of field name to warning message for potential issues
 */
public record ValidationResult(
        boolean valid,
        Map<String, String> errors,
        Map<String, String> warnings
) {
    /**
     * Creates a successful validation result with no errors or warnings.
     */
    public static ValidationResult success() {
        return new ValidationResult(true, Map.of(), Map.of());
    }

    /**
     * Creates a validation result with errors.
     */
    public static ValidationResult withErrors(Map<String, String> errors) {
        return new ValidationResult(false, errors, Map.of());
    }

    /**
     * Creates a validation result with errors and warnings.
     */
    public static ValidationResult withErrorsAndWarnings(Map<String, String> errors,
                                                          Map<String, String> warnings) {
        return new ValidationResult(errors.isEmpty(), errors, warnings);
    }

    /**
     * Checks if there are any errors.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Checks if there are any warnings.
     */
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    /**
     * Gets the total number of issues (errors + warnings).
     */
    public int issueCount() {
        return errors.size() + warnings.size();
    }
}
