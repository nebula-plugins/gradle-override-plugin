package nebula.plugin.override.converter

import spock.lang.Specification
import spock.lang.Unroll

class ApacheCommonsTypeConverterTest extends Specification {
    TypeConverter typeConverter = new ApacheCommonsTypeConverter()
    
    @Unroll
    def "Can convert '#valueString' to #convertedType"() {
        expect:
        def convertedValue = typeConverter.convert(valueString, convertedType)
        convertedValue.getClass() == convertedType
        convertedValue == typedValue
        
        where:
        valueString      | convertedType              | typedValue
        '100'            | java.lang.Byte.class       | 100
        '10'             | java.lang.Short.class      | 10
        'A'              | java.lang.Character.class  | 'A'
        'test'           | java.lang.String.class     | 'test'
        '1'              | java.lang.Integer.class    | 1
        '1'              | java.lang.Long.class       | 1L
        'true'           | java.lang.Boolean.class    | true
        '1.5'            | java.lang.Float.class      | 1.5f
        '1.5'            | java.lang.Double.class     | 1.5d
        '/usr/bin/test'  | java.io.File.class         | new File('/usr/bin/test')
    }
}