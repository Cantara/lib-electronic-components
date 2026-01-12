package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Tracks the lifecycle information for an electronic component.
 *
 * This includes the current lifecycle status, important dates (last time buy,
 * end of life), replacement part suggestions, and a history of status changes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "statusDate",
    "lastTimeBuyDate",
    "endOfLifeDate",
    "replacementParts",
    "source",
    "notes"
})
public class ComponentLifecycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("status")
    private ComponentLifecycleStatus status = ComponentLifecycleStatus.UNKNOWN;

    /**
     * Date when the current status was set or last verified.
     */
    @JsonProperty("statusDate")
    private LocalDate statusDate;

    /**
     * Deadline for Last Time Buy orders.
     * Only applicable when status is LAST_TIME_BUY or later.
     */
    @JsonProperty("lastTimeBuyDate")
    private LocalDate lastTimeBuyDate;

    /**
     * Date when the component reaches/reached End of Life.
     */
    @JsonProperty("endOfLifeDate")
    private LocalDate endOfLifeDate;

    /**
     * Suggested replacement parts (MPNs) when component becomes obsolete.
     */
    @JsonProperty("replacementParts")
    private List<ReplacementPart> replacementParts = new ArrayList<>();

    /**
     * Source of the lifecycle information (e.g., "Manufacturer PCN", "Distributor", "Manual").
     */
    @JsonProperty("source")
    private String source;

    /**
     * Additional notes about the lifecycle status.
     */
    @JsonProperty("notes")
    private String notes;

    /**
     * History of status changes for audit trail.
     */
    @JsonProperty("statusHistory")
    private List<StatusChange> statusHistory = new ArrayList<>();

    public ComponentLifecycle() {
    }

    public ComponentLifecycle(ComponentLifecycleStatus status) {
        this.status = status;
        this.statusDate = LocalDate.now();
    }

    // Factory methods for common scenarios

    /**
     * Creates a lifecycle entry for an active component.
     */
    public static ComponentLifecycle active() {
        return new ComponentLifecycle(ComponentLifecycleStatus.ACTIVE);
    }

    /**
     * Creates a lifecycle entry for an obsolete component with replacement.
     */
    public static ComponentLifecycle obsoleteWithReplacement(String replacementMpn, String replacementManufacturer) {
        ComponentLifecycle lifecycle = new ComponentLifecycle(ComponentLifecycleStatus.OBSOLETE);
        lifecycle.addReplacementPart(replacementMpn, replacementManufacturer, ReplacementPart.CompatibilityLevel.FORM_FIT_FUNCTION);
        return lifecycle;
    }

    /**
     * Creates a lifecycle entry for Last Time Buy status.
     */
    public static ComponentLifecycle lastTimeBuy(LocalDate ltbDeadline) {
        ComponentLifecycle lifecycle = new ComponentLifecycle(ComponentLifecycleStatus.LAST_TIME_BUY);
        lifecycle.setLastTimeBuyDate(ltbDeadline);
        return lifecycle;
    }

    // Getters and setters

    public ComponentLifecycleStatus getStatus() {
        return status;
    }

    public ComponentLifecycle setStatus(ComponentLifecycleStatus status) {
        // Record status change in history
        if (this.status != status && this.status != null) {
            statusHistory.add(new StatusChange(this.status, status, LocalDate.now()));
        }
        this.status = status;
        this.statusDate = LocalDate.now();
        return this;
    }

    public LocalDate getStatusDate() {
        return statusDate;
    }

    public ComponentLifecycle setStatusDate(LocalDate statusDate) {
        this.statusDate = statusDate;
        return this;
    }

    public LocalDate getLastTimeBuyDate() {
        return lastTimeBuyDate;
    }

    public ComponentLifecycle setLastTimeBuyDate(LocalDate lastTimeBuyDate) {
        this.lastTimeBuyDate = lastTimeBuyDate;
        return this;
    }

    public LocalDate getEndOfLifeDate() {
        return endOfLifeDate;
    }

    public ComponentLifecycle setEndOfLifeDate(LocalDate endOfLifeDate) {
        this.endOfLifeDate = endOfLifeDate;
        return this;
    }

    public List<ReplacementPart> getReplacementParts() {
        return replacementParts;
    }

    public ComponentLifecycle setReplacementParts(List<ReplacementPart> replacementParts) {
        this.replacementParts = replacementParts;
        return this;
    }

    public ComponentLifecycle addReplacementPart(String mpn, String manufacturer, ReplacementPart.CompatibilityLevel compatibility) {
        this.replacementParts.add(new ReplacementPart(mpn, manufacturer, compatibility));
        return this;
    }

    public ComponentLifecycle addReplacementPart(ReplacementPart replacement) {
        this.replacementParts.add(replacement);
        return this;
    }

    public String getSource() {
        return source;
    }

    public ComponentLifecycle setSource(String source) {
        this.source = source;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public ComponentLifecycle setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public List<StatusChange> getStatusHistory() {
        return statusHistory;
    }

    // Utility methods

    /**
     * Checks if the component is still available for purchase.
     */
    public boolean isAvailable() {
        return status != null && status.isAvailable();
    }

    /**
     * Checks if the component should be avoided in new designs.
     */
    public boolean shouldAvoidForNewDesigns() {
        return status != null && status.shouldAvoidForNewDesigns();
    }

    /**
     * Checks if the lifecycle status requires attention (finding alternatives).
     */
    public boolean requiresAttention() {
        return status != null && status.requiresAttention();
    }

    /**
     * Checks if Last Time Buy deadline is approaching (within specified days).
     */
    public boolean isLtbApproaching(int daysThreshold) {
        if (status != ComponentLifecycleStatus.LAST_TIME_BUY || lastTimeBuyDate == null) {
            return false;
        }
        LocalDate threshold = LocalDate.now().plusDays(daysThreshold);
        return lastTimeBuyDate.isBefore(threshold) || lastTimeBuyDate.isEqual(threshold);
    }

    /**
     * Checks if Last Time Buy deadline has passed.
     */
    public boolean isLtbExpired() {
        if (lastTimeBuyDate == null) {
            return false;
        }
        return lastTimeBuyDate.isBefore(LocalDate.now());
    }

    /**
     * Gets the primary replacement part (first in the list).
     */
    public ReplacementPart getPrimaryReplacement() {
        return replacementParts.isEmpty() ? null : replacementParts.get(0);
    }

    /**
     * Gets replacements filtered by compatibility level.
     */
    public List<ReplacementPart> getReplacementsByCompatibility(ReplacementPart.CompatibilityLevel level) {
        return replacementParts.stream()
                .filter(r -> r.getCompatibility() == level)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentLifecycle that = (ComponentLifecycle) o;
        return status == that.status &&
               Objects.equals(statusDate, that.statusDate) &&
               Objects.equals(lastTimeBuyDate, that.lastTimeBuyDate) &&
               Objects.equals(endOfLifeDate, that.endOfLifeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, statusDate, lastTimeBuyDate, endOfLifeDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ComponentLifecycle{status=").append(status);
        if (statusDate != null) {
            sb.append(", statusDate=").append(statusDate);
        }
        if (lastTimeBuyDate != null) {
            sb.append(", ltbDate=").append(lastTimeBuyDate);
        }
        if (endOfLifeDate != null) {
            sb.append(", eolDate=").append(endOfLifeDate);
        }
        if (!replacementParts.isEmpty()) {
            sb.append(", replacements=").append(replacementParts.size());
        }
        sb.append('}');
        return sb.toString();
    }

    // Inner classes

    /**
     * Represents a suggested replacement part for an obsolete component.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReplacementPart implements Serializable {
        private static final long serialVersionUID = 1L;

        public enum CompatibilityLevel {
            /**
             * Drop-in replacement: same form, fit, and function.
             */
            FORM_FIT_FUNCTION("Form/Fit/Function", "Drop-in replacement"),

            /**
             * Functional equivalent with same electrical characteristics
             * but may have different footprint or pinout.
             */
            FUNCTIONAL_EQUIVALENT("Functional Equivalent", "Same function, different form"),

            /**
             * Similar performance but may require design changes.
             */
            SIMILAR("Similar", "Similar specs, may need design changes"),

            /**
             * Alternative from a different manufacturer with equivalent specs.
             */
            CROSS_REFERENCE("Cross Reference", "Equivalent from different manufacturer"),

            /**
             * Upgraded part with better specifications.
             */
            UPGRADE("Upgrade", "Better specifications than original");

            private final String displayName;
            private final String description;

            CompatibilityLevel(String displayName, String description) {
                this.displayName = displayName;
                this.description = description;
            }

            public String getDisplayName() {
                return displayName;
            }

            public String getDescription() {
                return description;
            }
        }

        @JsonProperty("mpn")
        private String mpn;

        @JsonProperty("manufacturer")
        private String manufacturer;

        @JsonProperty("compatibility")
        private CompatibilityLevel compatibility;

        @JsonProperty("notes")
        private String notes;

        public ReplacementPart() {
        }

        public ReplacementPart(String mpn, String manufacturer, CompatibilityLevel compatibility) {
            this.mpn = mpn;
            this.manufacturer = manufacturer;
            this.compatibility = compatibility;
        }

        public String getMpn() {
            return mpn;
        }

        public ReplacementPart setMpn(String mpn) {
            this.mpn = mpn;
            return this;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public ReplacementPart setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public CompatibilityLevel getCompatibility() {
            return compatibility;
        }

        public ReplacementPart setCompatibility(CompatibilityLevel compatibility) {
            this.compatibility = compatibility;
            return this;
        }

        public String getNotes() {
            return notes;
        }

        public ReplacementPart setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        @Override
        public String toString() {
            return mpn + " (" + manufacturer + ", " + compatibility + ")";
        }
    }

    /**
     * Records a change in lifecycle status for audit trail.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatusChange implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("fromStatus")
        private ComponentLifecycleStatus fromStatus;

        @JsonProperty("toStatus")
        private ComponentLifecycleStatus toStatus;

        @JsonProperty("changeDate")
        private LocalDate changeDate;

        @JsonProperty("reason")
        private String reason;

        public StatusChange() {
        }

        public StatusChange(ComponentLifecycleStatus fromStatus, ComponentLifecycleStatus toStatus, LocalDate changeDate) {
            this.fromStatus = fromStatus;
            this.toStatus = toStatus;
            this.changeDate = changeDate;
        }

        public ComponentLifecycleStatus getFromStatus() {
            return fromStatus;
        }

        public ComponentLifecycleStatus getToStatus() {
            return toStatus;
        }

        public LocalDate getChangeDate() {
            return changeDate;
        }

        public String getReason() {
            return reason;
        }

        public StatusChange setReason(String reason) {
            this.reason = reason;
            return this;
        }

        @Override
        public String toString() {
            return fromStatus + " -> " + toStatus + " on " + changeDate;
        }
    }
}
