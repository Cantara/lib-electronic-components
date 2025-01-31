package no.cantara.electronic.component;

import java.util.regex.Pattern;

public enum Manufacturer {
    TEXAS_INSTRUMENTS("TI", "^(LM|TL|UA|NE|CD|SN|TPS|BQ)[0-9]{2,6}[A-Z0-9-]*$"),
    ANALOG_DEVICES("ADI", "^(AD|OP|ADP|ADC|DAC)[0-9]{3,5}[A-Z0-9-]*$"),
    MICROCHIP("MCHP", "^(PIC|MCP|AT|dsPIC)[0-9A-Z]{4,8}[A-Z0-9-]*$"),
    ST_MICROELECTRONICS("ST", "^(STM|ST|L|LM|TL)[0-9A-Z]{3,8}[A-Z0-9-]*$"),
    INFINEON("INF", "^(IRC|IRF|IPA|IPB|IPD|BSZ|BSC)[0-9]{3,5}[A-Z0-9-]*$"),
    ON_SEMICONDUCTOR("ON", "^(MC|LM|TL|NCP|FQP)[0-9]{3,5}[A-Z0-9-]*$"),
    VISHAY("VSH", "^(SI|IRF|IRL|SQJ|SQM)[0-9]{3,5}[A-Z0-9-]*$"),
    GENERIC(null, "^[A-Z0-9][A-Z0-9-]{2,15}$");

    private final String code;
    private final String mpnPattern;
    private final Pattern compiledPattern;

    Manufacturer(String code, String mpnPattern) {
        this.code = code;
        this.mpnPattern = mpnPattern;
        this.compiledPattern = Pattern.compile(mpnPattern);
    }

    public String getCode() {
        return code;
    }

    public String getMpnPattern() {
        return mpnPattern;
    }

    public boolean matches(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return false;
        }
        return compiledPattern.matcher(MPNUtils.normalize(mpn)).matches();
    }
}