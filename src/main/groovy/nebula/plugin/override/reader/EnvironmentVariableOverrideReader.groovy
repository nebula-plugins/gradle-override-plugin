package nebula.plugin.override.reader

/**
 * Parses override properties from environment variables prefixed with "OVERRIDE_".
 *
 * Example:
 *
 * <pre>
 *     export OVERRIDE_example.test=replace
 * </pre>
 */
class EnvironmentVariableOverrideReader implements OverrideReader {
    static final String OVERRIDE_PROPERTY_PREFIX = 'OVERRIDE_'

    @Override
    Map<String, String> parseProperties() {
        def overrideProperties = System.getenv().findAll { it.key.startsWith(OVERRIDE_PROPERTY_PREFIX) }
        // Remove property prefix
        overrideProperties.collectEntries { key, value -> [key - OVERRIDE_PROPERTY_PREFIX, value] }
    }
}
