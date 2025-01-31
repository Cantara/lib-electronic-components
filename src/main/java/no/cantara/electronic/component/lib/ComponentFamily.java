package no.cantara.electronic.component.lib;

import java.util.Arrays;
import java.util.Set;

public enum ComponentFamily {
    CAPACITOR {
        @Override
        public boolean matches(String mpn) {
            return mpn.matches("^C[0-9].*")           // KEMET style
                    || mpn.matches("^GRM.*")              // Murata style
                    || mpn.matches("^CL[0-9].*");         // Samsung style
        }

        @Override
        public Set<String> getPrimaryManufacturers() {
            return Set.of("KEM", "MUR", "SAM", "TDK");
        }
    },
    RESISTOR {
        @Override
        public boolean matches(String mpn) {
            return mpn.matches("^CRCW.*")             // Vishay style
                    || mpn.matches("^RC.*")               // Yageo style
                    || mpn.matches("^ERJ.*");             // Panasonic style
        }

        @Override
        public Set<String> getPrimaryManufacturers() {
            return Set.of("VSH", "RC", "PAN");
        }
    },
    MICROCONTROLLER {
        @Override
        public boolean matches(String mpn) {
            String upperMpn = mpn.toUpperCase();
            return upperMpn.matches("^PIC[0-9]+.*")        // Microchip PIC
                    || upperMpn.matches("^DSPIC[0-9]+.*")      // Microchip dsPIC  // Changed to uppercase
                    || upperMpn.matches("^ATMEGA.*")           // Atmel ATMega
                    || upperMpn.matches("^ATTINY.*")           // Atmel ATTiny
                    || upperMpn.matches("^STM32.*")            // ST STM32
                    || upperMpn.matches("^STM8.*");            // ST STM8
        }

        @Override
        public Set<String> getPrimaryManufacturers() {
            return Set.of("MCP", "AT", "ST");  // Microchip, Atmel, ST
        }
    };

    public abstract boolean matches(String mpn);
    public abstract Set<String> getPrimaryManufacturers();

    public static ComponentFamily fromMPN(String mpn) {
        if (mpn == null || mpn.isEmpty()) return null;
        String upperMpn = mpn.toUpperCase();
        return Arrays.stream(values())
                .filter(family -> family.matches(upperMpn))
                .findFirst()
                .orElse(null);
    }
}