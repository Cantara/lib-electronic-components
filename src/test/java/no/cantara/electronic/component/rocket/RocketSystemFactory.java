package no.cantara.electronic.component.rocket;

import no.cantara.electronic.component.advanced.PlannedProductionBatch;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.BOMEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RocketSystemFactory {

    public static RocketSystemBuilder createCompleteSystem(
            String batchId,
            String productName,
            String revision,
            int quantity) {
        return new RocketSystemBuilder(batchId, productName, revision, quantity)
                .withFlightControl()
                .withPropulsion()
                .withTelemetry()
                .withRedundancy();
    }

    public static RocketSystemBuilder createCustomSystem(
            String batchId,
            String productName,
            String revision,
            int quantity) {
        return new RocketSystemBuilder(batchId, productName, revision, quantity);
    }

    public static class RocketSystemBuilder {
        private final String batchId;
        private final String productName;
        private final String revision;
        private final int quantity;
        private LocalDate plannedDate;
        private boolean includeFlightControl;
        private boolean includePropulsion;
        private boolean includeTelemetry;
        private boolean includeRedundancy;

        private RocketSystemBuilder(String batchId, String productName, String revision, int quantity) {
            this.batchId = batchId;
            this.productName = productName;
            this.revision = revision;
            this.quantity = quantity;
        }

        public RocketSystemBuilder plannedDate(LocalDate date) {
            this.plannedDate = date;
            return this;
        }

        public RocketSystemBuilder withFlightControl() {
            this.includeFlightControl = true;
            return this;
        }

        public RocketSystemBuilder withPropulsion() {
            this.includePropulsion = true;
            return this;
        }

        public RocketSystemBuilder withTelemetry() {
            this.includeTelemetry = true;
            return this;
        }

        public RocketSystemBuilder withRedundancy() {
            this.includeRedundancy = true;
            return this;
        }

        public PlannedProductionBatch build() {
            PCBABOM pcbaBOM = new PCBABOM();
            MechanicalBOM mechanicalBOM = new MechanicalBOM();

            // Base components always included
            addBaseComponents(pcbaBOM);
            addBaseMechanicalComponents(mechanicalBOM);

            // Optional subsystems
            if (includeFlightControl) {
                addFlightControlComponents(pcbaBOM, includeRedundancy);
            }
            if (includePropulsion) {
                addPropulsionComponents(pcbaBOM, includeRedundancy);
            }
            if (includeTelemetry) {
                addTelemetryComponents(pcbaBOM, includeRedundancy);
            }

            PlannedProductionBatch batch = new PlannedProductionBatch(batchId, productName, revision, quantity);

            if (plannedDate != null) {
                batch.setPlannedDate(plannedDate);
            }

            batch.addPCBA(pcbaBOM);
            batch.addMechanical(mechanicalBOM);

            return batch;
        }

        private void addBaseComponents(PCBABOM pcbaBOM) {
            List<BOMEntry> baseEntries = new ArrayList<>();

            // Power management
            baseEntries.add(createComponent("TPS62840", "Buck Converter",
                    Map.of("efficiency", "95%", "protection", "OCP")));
            baseEntries.add(createComponent("BQ24195", "Battery Management IC",
                    Map.of("protection", "OVP")));

            // Microcontroller
            baseEntries.add(createComponent("STM32H755", "Dual-Core Flight Controller",
                    Map.of("watchdog", "enabled", "thermal_management", "active", "secure_bootloader", "enabled")));

            // Add entries to BOM
            baseEntries.forEach(entry -> pcbaBOM.getBomEntries().add(entry));
        }

        private void addFlightControlComponents(PCBABOM pcbaBOM, boolean withRedundancy) {
            List<BOMEntry> flightControlEntries = new ArrayList<>();

            // Primary sensors
            flightControlEntries.add(createComponent("BMI088", "High-G IMU",
                    Map.of("thermal_management", "active")));
            flightControlEntries.add(createComponent("MS5611", "High-Precision Barometer",
                    Map.of("thermal_management", "passive")));
            flightControlEntries.add(createComponent("MAX31856", "Thermocouple Interface",
                    Map.of("thermal_protection", "enabled")));

            if (withRedundancy) {
                // Redundant sensors
                flightControlEntries.add(createComponent("BMI088-RED", "Redundant High-G IMU",
                        Map.of("thermal_management", "active")));
                flightControlEntries.add(createComponent("MS5611-RED", "Redundant Barometer",
                        Map.of("thermal_management", "passive")));
            }

            // Safety systems
            flightControlEntries.add(createComponent("SafetyController", "Emergency Abort Controller",
                    Map.of("emergency_abort", "yes", "watchdog", "enabled")));

            flightControlEntries.forEach(entry -> pcbaBOM.getBomEntries().add(entry));
        }

        private void addPropulsionComponents(PCBABOM pcbaBOM, boolean withRedundancy) {
            List<BOMEntry> propulsionEntries = new ArrayList<>();

            // Primary components
            propulsionEntries.add(createComponent("DRV8701", "Motor Driver",
                    Map.of("thermal_protection", "enabled", "overcurrent_protection", "enabled")));
            propulsionEntries.add(createComponent("INA240", "Current Sense Amplifier",
                    Map.of("protection", "OVP")));
            propulsionEntries.add(createComponent("MAX31855", "Thermocouple Interface",
                    Map.of("thermal_protection", "enabled")));

            if (withRedundancy) {
                // Redundant components
                propulsionEntries.add(createComponent("DRV8701-RED", "Redundant Motor Driver",
                        Map.of("thermal_protection", "enabled", "overcurrent_protection", "enabled")));
                propulsionEntries.add(createComponent("INA240-RED", "Redundant Current Sensor",
                        Map.of("protection", "OVP")));
            }

            propulsionEntries.forEach(entry -> pcbaBOM.getBomEntries().add(entry));
        }

        private void addTelemetryComponents(PCBABOM pcbaBOM, boolean withRedundancy) {
            List<BOMEntry> telemetryEntries = new ArrayList<>();

            // Primary components
            telemetryEntries.add(createComponent("CC1200", "High-Power RF Transceiver",
                    Map.of("encryption", "enabled")));
            telemetryEntries.add(createComponent("NEO-M9N", "GPS Module",
                    Map.of("thermal_management", "passive")));
            telemetryEntries.add(createComponent("SE2435L", "RF Power Amplifier",
                    Map.of("thermal_protection", "enabled")));

            if (withRedundancy) {
                // Redundant components
                telemetryEntries.add(createComponent("CC1200-RED", "Redundant RF Transceiver",
                        Map.of("encryption", "enabled")));
                telemetryEntries.add(createComponent("NEO-M9N-RED", "Redundant GPS Module",
                        Map.of("thermal_management", "passive")));
            }

            telemetryEntries.forEach(entry -> pcbaBOM.getBomEntries().add(entry));
        }

        private void addBaseMechanicalComponents(MechanicalBOM mechanicalBOM) {
            List<BOMEntry> mechanicalEntries = new ArrayList<>();

            // Structure
            mechanicalEntries.add(createComponent("AL-6061-T6", "Main Body Tube",
                    Map.of("material", "aluminum")));
            mechanicalEntries.add(createComponent("CF-001", "Nose Cone",
                    Map.of("material", "carbon_fiber")));
            mechanicalEntries.add(createComponent("TI-6AL-4V", "Fin Set",
                    Map.of("material", "titanium")));

            mechanicalEntries.forEach(entry -> mechanicalBOM.getBomEntries().add(entry));
        }

        private BOMEntry createComponent(String mpn, String description, Map<String, String> specs) {
            BOMEntry entry = new BOMEntry()
                    .setMpn(mpn)
                    .setDescription(description)
                    .addSpecs(specs);
            entry.setQty("1"); // Default quantity, can be modified if needed
            return entry;
        }
    }
}