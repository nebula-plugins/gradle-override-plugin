package nebula.plugin.override.reader

/**
 * Aggregate override reader with a predefined list of implementations.
 */
class AggregateOverrideReader implements OverrideReader {
    List<OverrideReader> overrideReaders = [] as List<OverrideReader>

    AggregateOverrideReader() {
        overrideReaders << new EnvironmentVariableOverrideReader()
        overrideReaders << new SystemPropertyOverrideReader()
    }

    @Override
    Map<String, String> parseProperties() {
        Map<String, String> properties = [:] as Map<String, String>

        overrideReaders.each { reader ->
            properties.putAll(reader.parseProperties())
        }

        properties
    }
}
