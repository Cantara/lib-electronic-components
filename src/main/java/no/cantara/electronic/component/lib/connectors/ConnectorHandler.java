package no.cantara.electronic.component.lib.connectors;

public interface ConnectorHandler {
    String getFamily();            // e.g., "Header", "Terminal Block", "IDC"
    int getPinCount(String mpn);   // Extract pin count from MPN
    String getPitch(String mpn);   // Extract pitch (e.g., "2.54mm")
    String getMountingType(String mpn);  // e.g., "THT", "SMD"
    String getVariant(String mpn); // Extract variant/packaging code
    boolean areCompatible(String mpn1, String mpn2);  // Check if two connectors are compatible
    double calculateSimilarity(String mpn1, String mpn2);  // Calculate similarity between two connectors
    String getSeriesName(String mpn);
}