package nebula.plugin.override.reader

import spock.lang.Specification

class AggregateOverrideReaderTest extends Specification {
    AggregateOverrideReader aggregateOverrideReader = new AggregateOverrideReader()

    def "Has registered default readers in expected order"() {
        expect:
        aggregateOverrideReader.overrideReaders.size() == 2
        aggregateOverrideReader.overrideReaders[0] instanceof EnvironmentVariableOverrideReader
        aggregateOverrideReader.overrideReaders[1] instanceof SystemPropertyOverrideReader
    }
}
