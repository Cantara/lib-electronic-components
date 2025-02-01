package no.cantara.electronic.component.advanced;

import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a planned production batch that brings together all assemblies needed for production.
 * This includes PCB assemblies, mechanical assemblies, cable assemblies, and packaging assemblies.
 */
public class PlannedProductionBatch {
    private final String batchId;
    private final String productId;
    private final String revision;
    private final int quantity;
    private LocalDate plannedDate;
    private BatchStatus status;

    // Core manufacturing structures
    private final PCBAStructure pcbaStructure = new PCBAStructure();
    private final MechanicalStructure mechanicalStructure = new MechanicalStructure();
    private final CableStructure cableStructure = new CableStructure();
    private final PackagingStructure packagingStructure = new PackagingStructure();

    public enum BatchStatus {
        DRAFT,
        SOURCING,
        BOM_REVIEW,
        PLANNING,
        READY,
        IN_PRODUCTION,
        COMPLETED,
        ON_HOLD,
        CANCELLED
    }

    /**
     * Structure for managing PCB assemblies
     */
    public static class PCBAStructure {
        private final Set<PCBABOM> assemblies = new HashSet<>();
        private final Map<String, String> assemblyInstructions = new HashMap<>();
        private final Map<String, String> testRequirements = new HashMap<>();

        public Set<PCBABOM> getAssemblies() {
            return Collections.unmodifiableSet(assemblies);
        }

        public void addAssembly(PCBABOM bom) {
            assemblies.add(bom);
        }

        public Map<String, String> getAssemblyInstructions() {
            return Collections.unmodifiableMap(assemblyInstructions);
        }

        public Map<String, String> getTestRequirements() {
            return Collections.unmodifiableMap(testRequirements);
        }
    }

    /**
     * Structure for managing mechanical assemblies
     */
    public static class MechanicalStructure {
        private final Set<MechanicalBOM> assemblies = new HashSet<>();
        private final Map<String, String> assemblyInstructions = new HashMap<>();
        private final Map<String, String> testRequirements = new HashMap<>();

        public Set<MechanicalBOM> getAssemblies() {
            return Collections.unmodifiableSet(assemblies);
        }

        public void addAssembly(MechanicalBOM bom) {
            assemblies.add(bom);
        }

        public Map<String, String> getAssemblyInstructions() {
            return Collections.unmodifiableMap(assemblyInstructions);
        }

        public Map<String, String> getTestRequirements() {
            return Collections.unmodifiableMap(testRequirements);
        }
    }

    /**
     * Structure for managing cable assemblies
     */
    public static class CableStructure {
        private final Set<CableBOM> assemblies = new HashSet<>();
        private final Map<String, String> assemblyInstructions = new HashMap<>();
        private final Map<String, String> testRequirements = new HashMap<>();

        public Set<CableBOM> getAssemblies() {
            return Collections.unmodifiableSet(assemblies);
        }

        public void addAssembly(CableBOM bom) {
            assemblies.add(bom);
        }

        public Map<String, String> getAssemblyInstructions() {
            return Collections.unmodifiableMap(assemblyInstructions);
        }

        public Map<String, String> getTestRequirements() {
            return Collections.unmodifiableMap(testRequirements);
        }
    }

    /**
     * Structure for managing packaging assemblies
     */
    public static class PackagingStructure {
        private final Set<PackagingBOM> assemblies = new HashSet<>();
        private final Map<String, String> assemblyInstructions = new HashMap<>();
        private final Map<String, String> requirements = new HashMap<>();

        public Set<PackagingBOM> getAssemblies() {
            return Collections.unmodifiableSet(assemblies);
        }

        public void addAssembly(PackagingBOM bom) {
            assemblies.add(bom);
        }

        public Map<String, String> getAssemblyInstructions() {
            return Collections.unmodifiableMap(assemblyInstructions);
        }

        public Map<String, String> getRequirements() {
            return Collections.unmodifiableMap(requirements);
        }
    }

    public PlannedProductionBatch(String batchId, String productId, String revision, int quantity) {
        if (batchId == null || batchId.trim().isEmpty()) {
            throw new IllegalArgumentException("Batch ID cannot be null or empty");
        }
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        if (revision == null || revision.trim().isEmpty()) {
            throw new IllegalArgumentException("Revision cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        this.batchId = batchId;
        this.productId = productId;
        this.revision = revision;
        this.quantity = quantity;
        this.status = BatchStatus.DRAFT;
    }

    // Assembly management
    public void addPCBA(PCBABOM bom) {
        pcbaStructure.addAssembly(bom);
    }

    public void addMechanical(MechanicalBOM bom) {
        mechanicalStructure.addAssembly(bom);
    }

    public void addCable(CableBOM bom) {
        cableStructure.addAssembly(bom);
    }

    public void addPackaging(PackagingBOM bom) {
        packagingStructure.addAssembly(bom);
    }

    // Status management
    public boolean canTransitionTo(BatchStatus newStatus) {
        return switch (status) {
            case DRAFT -> newStatus == BatchStatus.SOURCING || newStatus == BatchStatus.CANCELLED;
            case SOURCING -> newStatus == BatchStatus.BOM_REVIEW || newStatus == BatchStatus.ON_HOLD;
            case BOM_REVIEW -> newStatus == BatchStatus.PLANNING || newStatus == BatchStatus.SOURCING;
            case PLANNING -> newStatus == BatchStatus.READY || newStatus == BatchStatus.BOM_REVIEW;
            case READY -> newStatus == BatchStatus.IN_PRODUCTION || newStatus == BatchStatus.ON_HOLD;
            case IN_PRODUCTION -> newStatus == BatchStatus.COMPLETED || newStatus == BatchStatus.ON_HOLD;
            case ON_HOLD -> newStatus == BatchStatus.PLANNING || newStatus == BatchStatus.IN_PRODUCTION ||
                    newStatus == BatchStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    // Validation
    public List<String> validate() {
        List<String> issues = new ArrayList<>();

        // Check for empty structures
        if (pcbaStructure.getAssemblies().isEmpty()) {
            issues.add("No PCB assemblies defined");
        }
        if (mechanicalStructure.getAssemblies().isEmpty()) {
            issues.add("No mechanical assemblies defined");
        }
        if (cableStructure.getAssemblies().isEmpty()) {
            issues.add("No cable assemblies defined");
        }
        if (packagingStructure.getAssemblies().isEmpty()) {
            issues.add("No packaging assemblies defined");
        }

        // Check production readiness
        if (plannedDate == null) {
            issues.add("Planned production date not set");
        }

        return issues;
    }

    // Getters
    public String getBatchId() { return batchId; }
    public String getProductId() { return productId; }
    public String getRevision() { return revision; }
    public int getQuantity() { return quantity; }
    public LocalDate getPlannedDate() { return plannedDate; }
    public BatchStatus getStatus() { return status; }

    // Setters
    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public void setStatus(BatchStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", status, newStatus)
            );
        }
        this.status = newStatus;
    }

    // Structure access
    public PCBAStructure getPCBAStructure() { return pcbaStructure; }
    public MechanicalStructure getMechanicalStructure() { return mechanicalStructure; }
    public CableStructure getCableStructure() { return cableStructure; }
    public PackagingStructure getPackagingStructure() { return packagingStructure; }
}