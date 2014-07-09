package nebula.plugin.override.reader

import spock.lang.Specification

class SystemPropertyOverrideReaderTest extends Specification {
    OverrideReader systemPropertyOverrideReader = new SystemPropertyOverrideReader()
    
    def "No override properties are provided"() {
        when:
        Map<String, String> overrideProperties = systemPropertyOverrideReader.parseProperties()
        
        then:
        overrideProperties.isEmpty()
    }
    
    def "Override properties are parsed and stripped of prefix"() {
        setup:
        System.setProperty('override.example.test', 'prop1')
        System.setProperty('override.myPlugin.name', 'prop2')
        
        when:
        Map<String, String> overrideProperties = systemPropertyOverrideReader.parseProperties()
        
        then:
        !overrideProperties.isEmpty()
        overrideProperties.containsKey('example.test')
        overrideProperties.containsKey('myPlugin.name')
        overrideProperties['example.test'] == 'prop1'
        overrideProperties['myPlugin.name'] == 'prop2'
        
        cleanup:
        System.clearProperty('override.example.test')
        System.clearProperty('override.myPlugin.name')
    }
}