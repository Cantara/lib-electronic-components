package no.cantara.electronic.component.autonomous_submarine.subsystems;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubsystemVerificationResult {
    private final boolean componentsValid;
    private final boolean integrationValid;
    private final List<String> issues;

    public SubsystemVerificationResult(
            boolean componentsValid,
            boolean integrationValid,
            List<String> issues) {
        this.componentsValid = componentsValid;
        this.integrationValid = integrationValid;
        this.issues = new ArrayList<>(issues);
    }

    public boolean isValid() {
        return componentsValid && integrationValid && issues.isEmpty();
    }

    public boolean isComponentsValid() {
        return componentsValid;
    }

    public boolean isIntegrationValid() {
        return integrationValid;
    }

    public List<String> getIssues() {
        return Collections.unmodifiableList(issues);
    }
}