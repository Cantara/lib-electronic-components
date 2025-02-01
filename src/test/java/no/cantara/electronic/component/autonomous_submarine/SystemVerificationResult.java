package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.autonomous_submarine.subsystems.SubsystemVerificationResult;

import java.util.*;

public class SystemVerificationResult {
    private Map<String, SubsystemVerificationResult> subsystemResults = new HashMap<>();
    private List<String> crossSystemIssues = new ArrayList<>();
    private boolean valid = true;

    public void setCrossSystemIssues(List<String> issues) {
        this.crossSystemIssues = new ArrayList<>(issues);
        this.valid = this.valid && issues.isEmpty();
    }

    public void addSubsystemResult(String subsystemName, SubsystemVerificationResult result) {
        subsystemResults.put(subsystemName, result);
        this.valid = this.valid && result.isValid();
    }

    public boolean isValid() {
        return valid && crossSystemIssues.isEmpty();
    }

    public Map<String, SubsystemVerificationResult> getSubsystemResults() {
        return Collections.unmodifiableMap(subsystemResults);
    }

    public List<String> getCrossSystemIssues() {
        return Collections.unmodifiableList(crossSystemIssues);
    }

    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("System Verification Report:\n");

        subsystemResults.forEach((name, result) -> {
            report.append(String.format("%s: %s\n", name, result.isValid() ? "PASS" : "FAIL"));
            if (!result.getIssues().isEmpty()) {
                result.getIssues().forEach(issue -> report.append("  - ").append(issue).append("\n"));
            }
        });

        if (!crossSystemIssues.isEmpty()) {
            report.append("\nCross-System Issues:\n");
            crossSystemIssues.forEach(issue -> report.append("  - ").append(issue).append("\n"));
        }

        return report.toString();
    }


}

