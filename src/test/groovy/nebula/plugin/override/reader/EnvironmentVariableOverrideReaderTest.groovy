package nebula.plugin.override.reader

import spock.lang.Specification

class EnvironmentVariableOverrideReaderTest extends Specification {
    OverrideReader environmentVariableOverrideReader = new EnvironmentVariableOverrideReader()

    def "No override properties are provided"() {
        when:
        Map<String, String> overrideProperties = environmentVariableOverrideReader.parseProperties()

        then:
        overrideProperties.isEmpty()
    }
}
