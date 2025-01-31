package no.cantara.electronic.component.lib.connectors;

import java.util.HashMap;
import java.util.Map;

public class ConnectorHandlerRegistry {
    private static final ConnectorHandlerRegistry INSTANCE = new ConnectorHandlerRegistry();
    private final Map<String, ConnectorHandler> handlers = new HashMap<>();

    private ConnectorHandlerRegistry() {
        // Register handlers with specific patterns
        // Würth connectors (61xxxxxxxxxxxx format)
        registerHandler("^61\\d{9}", new WurthHeaderHandler());

        // TE Connectivity connectors
        // Matches both formats:
        // - XXXXXX-Y (e.g., 282836-2)
        // - X-XXXXXX-Y (e.g., 1-284392-0)
        registerHandler("^(?:\\d+-)?\\d{6}-\\d+", new TEConnectorHandler());

        // Molex connectors (43045-xxxx format)
//        registerHandler("^(?:43045|505478)-\\d{4}", new MolexHandler());

        // JST connectors
//        registerHandler("^(?:PHR?|PH[RSDL]?|XHR?|XH[RSDL]?|SHR?|SH[RSDL]?|GHR?|GH[RSDL]?)-\\d+", new JSTHandler());

        // Hirose connectors
//        registerHandler("^(?:DF13|DF14|FH12|BM)\\d*-\\d+[A-Z]?-\\d+", new HiroseHandler());

        // Amphenol connectors
//        registerHandler("^(?:10120843|10120855|504182|505478)-\\d{4}", new AmphenolHandler());
    }

    public static ConnectorHandlerRegistry getInstance() {
        return INSTANCE;
    }

    public ConnectorHandler getHandler(String mpn) {
        if (mpn == null) return null;

        return handlers.entrySet().stream()
                .filter(e -> mpn.matches(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private void registerHandler(String pattern, ConnectorHandler handler) {
        handlers.put(pattern, handler);
    }
}