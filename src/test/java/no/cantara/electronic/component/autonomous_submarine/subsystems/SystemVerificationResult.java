package no.cantara.electronic.component.autonomous_submarine.subsystems;

import java.util.*;

public class SystemVerificationResult {
    private final Map<String, SubsystemVerificationResult> subsystemResults = new HashMap<>();
    private final List<String> crossSystemIssues = new ArrayList<>();

    public void addSubsystemResult(String subsystem, SubsystemVerificationResult result) {
        subsystemResults.put(subsystem, result);
    }

    public void addCrossSystemIssue(String issue) {
        crossSystemIssues.add(issue);
    }

    public boolean isValid() {
        return subsystemResults.values().stream().allMatch(SubsystemVerificationResult::isValid) &&
                crossSystemIssues.isEmpty();
    }

    public Map<String, SubsystemVerificationResult> getSubsystemResults() {
        return Collections.unmodifiableMap(subsystemResults);
    }

    public List<String> getCrossSystemIssues() {
        return Collections.unmodifiableList(crossSystemIssues);
    }

    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("System Verification Report\n");
        report.append("========================\n\n");

        // Add subsystem results
        subsystemResults.forEach((subsystem, result) -> {
            report.append(subsystem).append(":\n");
            report.append("  Components: ").append(result.isComponentsValid() ? "PASS" : "FAIL").append("\n");
            report.append("  Integration: ").append(result.isIntegrationValid() ? "PASS" : "FAIL").append("\n");
            if (!result.getIssues().isEmpty()) {
                report.append("  Issues:\n");
                result.getIssues().forEach(issue -> report.append("    - ").append(issue).append("\n"));
            }
            report.append("\n");
        });

        // Add cross-system issues
        if (!crossSystemIssues.isEmpty()) {
            report.append("Cross-System Issues:\n");
            crossSystemIssues.forEach(issue -> report.append("  - ").append(issue).append("\n"));
        }

        return report.toString();
    }
}

