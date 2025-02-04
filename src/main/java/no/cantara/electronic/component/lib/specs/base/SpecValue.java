package no.cantara.electronic.component.lib.specs.base;

import java.util.Objects;
import java.util.Optional;
import no.cantara.electronic.component.lib.specs.base.SpecUnit;
/**
 * Represents a specification value with optional minimum and maximum bounds.
 *
 * @param <T> The type of the value (typically Number or String)
 */
public class SpecValue<T> {
    private final T value;
    private final SpecUnit unit;
    private final T minValue;
    private final T maxValue;
    private final String description;

    /**
     * Creates a new specification value.
     *
     * @param value The primary value
     * @param unit The unit of measurement
     */
    public SpecValue(T value, SpecUnit unit) {
        this(value, unit, null, null, null);
    }

    /**
     * Creates a new specification value with a range.
     *
     * @param value The primary value
     * @param unit The unit of measurement
     * @param minValue The minimum allowed value
     * @param maxValue The maximum allowed value
     * @param description Optional description
     */
    public SpecValue(T value, SpecUnit unit, T minValue, T maxValue, String description) {
        this.value = value;
        this.unit = unit != null ? unit : SpecUnit.NONE;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.description = description;
    }

    public T getValue() {
        return value;
    }

    public SpecUnit getUnit() {
        return unit;
    }

    public Optional<T> getMinValue() {
        return Optional.ofNullable(minValue);
    }

    public Optional<T> getMaxValue() {
        return Optional.ofNullable(maxValue);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Checks if a value falls within this specification's range.
     *
     * @param testValue The value to test
     * @return true if the value is within range
     */
    @SuppressWarnings("unchecked")
    public boolean isInRange(T testValue) {
        if (testValue == null || value == null) {
            return false;
        }

        if (!(value instanceof Comparable)) {
            return value.equals(testValue);
        }

        Comparable<T> comparableValue = (Comparable<T>) testValue;

        if (minValue != null && comparableValue.compareTo(minValue) < 0) {
            return false;
        }

        if (maxValue != null && comparableValue.compareTo(maxValue) > 0) {
            return false;
        }

        return true;
    }

    /**
     * Formats the value with its unit.
     *
     * @return The formatted string representation
     */
    public String getFormattedValue() {
        if (value == null) {
            return "N/A";
        }

        if (value instanceof Number) {
            return unit.format((Number) value);
        }

        return value + (unit == SpecUnit.NONE ? "" : " " + unit.getSymbol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecValue)) return false;
        SpecValue<?> specValue = (SpecValue<?>) o;
        return Objects.equals(value, specValue.value) &&
                unit == specValue.unit &&
                Objects.equals(minValue, specValue.minValue) &&
                Objects.equals(maxValue, specValue.maxValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit, minValue, maxValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getFormattedValue());

        if (minValue != null || maxValue != null) {
            sb.append(" (");
            if (minValue != null) {
                sb.append("min: ").append(unit.format((Number) minValue));
            }
            if (minValue != null && maxValue != null) {
                sb.append(", ");
            }
            if (maxValue != null) {
                sb.append("max: ").append(unit.format((Number) maxValue));
            }
            sb.append(")");
        }

        if (description != null) {
            sb.append(" - ").append(description);
        }

        return sb.toString();
    }
}