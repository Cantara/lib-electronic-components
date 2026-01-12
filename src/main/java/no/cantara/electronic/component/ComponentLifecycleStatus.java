package no.cantara.electronic.component;

/**
 * Lifecycle status for electronic components.
 *
 * Tracks the manufacturing/availability status of components through their lifecycle
 * from active production through obsolescence.
 */
public enum ComponentLifecycleStatus {

    /**
     * Component is actively manufactured and readily available.
     */
    ACTIVE("Active", "Component is in active production"),

    /**
     * Not Recommended For New Designs.
     * Component is still available but manufacturer recommends using alternatives
     * for new designs due to planned phase-out.
     */
    NRFND("NRFND", "Not Recommended For New Designs"),

    /**
     * Last Time Buy period.
     * Final window to place orders before manufacturing ends.
     * Typically has a specific deadline date.
     */
    LAST_TIME_BUY("Last Time Buy", "Final order period before discontinuation"),

    /**
     * Component is obsolete/discontinued.
     * No longer manufactured; may still be available through distributors
     * with remaining stock or aftermarket sources.
     */
    OBSOLETE("Obsolete", "No longer manufactured"),

    /**
     * End of Life - complete discontinuation.
     * Component is no longer available from any official channels.
     */
    EOL("End of Life", "Complete discontinuation, no longer available"),

    /**
     * Lifecycle status is not known or not yet determined.
     */
    UNKNOWN("Unknown", "Lifecycle status not determined");

    private final String displayName;
    private final String description;

    ComponentLifecycleStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if this status indicates the component is still available for purchase.
     * @return true if component can still be ordered
     */
    public boolean isAvailable() {
        return this == ACTIVE || this == NRFND || this == LAST_TIME_BUY;
    }

    /**
     * Checks if this status indicates the component should not be used in new designs.
     * @return true if component should be avoided for new designs
     */
    public boolean shouldAvoidForNewDesigns() {
        return this != ACTIVE && this != UNKNOWN;
    }

    /**
     * Checks if this status requires urgent attention (finding alternatives).
     * @return true if lifecycle status is critical
     */
    public boolean requiresAttention() {
        return this == LAST_TIME_BUY || this == OBSOLETE || this == EOL;
    }
}
