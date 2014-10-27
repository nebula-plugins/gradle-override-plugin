package nebula.plugin.override.converter

import nebula.plugin.override.ComplexTypeExtension
import spock.lang.Specification
import spock.lang.Unroll

class ComplexTypeConvertionTest extends Specification {

    ComplexTypeExtension ext = new ComplexTypeExtension()

    @Unroll
    def "Can convert '#valueString' for #propertyName"() {
        expect:
            //TODO

        where:
            valueString           | propertyName                   || typedValue
            '[foo,bar]'           | 'nullUntypedStringList'        || ['foo', 'bar']
            'foo,bar'             | 'nullUntypedStringList'        || ['foo', 'bar']
            '[foo,bar]'           | 'nullStringList'               || ['foo', 'bar']
            'foo,bar'             | 'nullStringList'               || ['foo', 'bar']
            '[]'                  | 'nullStringList'               || []
            'null'                | 'nullStringList'               || ['null']
            '(null)'              | 'nullStringList'               || null
            '[1,2]'               | 'nullIntegerList'              || [1, 2]
            '1,2'                 | 'nullIntegerList'              || [1, 2]
            'null'                | 'nullIntegerList'              || null
            '(null)'              | 'nullIntegerList'              || null
            '[foo,bar]'           | 'nullUntypedStringSet'         || ['foo', 'bar'] as Set
            'foo,bar'             | 'nullUntypedStringSet'         || ['foo', 'bar'] as Set
            '[foo,bar]'           | 'nullStringSet'                || ['foo', 'bar'] as Set
            'foo,bar'             | 'nullStringSet'                || ['foo', 'bar'] as Set
            '[1,2]'               | 'nullIntegerSet'               || [1, 2] as Set
            '1,2'                 | 'nullIntegerSet'               || [1, 2] as Set
            '[foo:bar,foo2:bar2]' | 'nullUntypedStringToStringMap' || [foo: 'bar', foo2: 'bar2']
            'foo:bar,foo2:bar2'   | 'nullUntypedStringToStringMap' || [foo: 'bar', foo2: 'bar2']
            '[foo:bar,foo2:bar2]' | 'nullStringToStringMap'        || [foo: 'bar', foo2: 'bar2']
            'foo:bar,foo2:bar2'   | 'nullStringToStringMap'        || [foo: 'bar', foo2: 'bar2']
            '[foo:1,foo2:2]'      | 'nullStringToIntegerMap'       || [foo: 1, foo2: 2]
            'foo:1,foo2:2'        | 'nullStringToIntegerMap'       || [foo: 1, foo2: 2]
            '[1:10,2:20]'         | 'nullIntegerToIntegerMap'      || [(1): 10, (2): 20]
            '1:10,2:20'           | 'nullIntegerToIntegerMap'      || [(1): 10, (2): 20]
            '[:]'                 | 'nullIntegerToIntegerMap'      || [:]

            '[foo,bar]'           | 'untypedStringList'            || ['foo', 'bar']
            'foo,bar'             | 'untypedStringList'            || ['foo', 'bar']
            '[foo,bar]'           | 'stringList'                   || ['foo', 'bar']
            'foo,bar'             | 'stringList'                   || ['foo', 'bar']
            '[]'                  | 'stringList'                   || []
            'null'                | 'stringList'                   || ['null']
            '(null)'              | 'stringList'                   || null
            '[1,2]'               | 'integerList'                  || [1, 2]
            '1,2'                 | 'integerList'                  || [1, 2]
            'null'                | 'integerList'                  || null
            '(null)'              | 'integerList'                  || null
            '[foo,bar]'           | 'untypedStringSet'             || ['foo', 'bar'] as Set
            'foo,bar'             | 'untypedStringSet'             || ['foo', 'bar'] as Set
            '[foo,bar]'           | 'stringSet'                    || ['foo', 'bar'] as Set
            'foo,bar'             | 'stringSet'                    || ['foo', 'bar'] as Set
            '[1,2]'               | 'integerSet'                   || [1, 2] as Set
            '1,2'                 | 'integerSet'                   || [1, 2] as Set
            '[foo:bar,foo2:bar2]' | 'untypedStringToStringMap'     || [foo: 'bar', foo2: 'bar2']
            'foo:bar,foo2:bar2'   | 'untypedStringToStringMap'     || [foo: 'bar', foo2: 'bar2']
            'null'                | 'untypedStringToStringMap'     || null
            '[foo:bar,foo2:bar2]' | 'stringToStringMap'            || [foo: 'bar', foo2: 'bar2']
            'foo:bar,foo2:bar2'   | 'stringToStringMap'            || [foo: 'bar', foo2: 'bar2']
            '[foo:1,foo2:2]'      | 'stringToIntegerMap'           || [foo: 1, foo2: 2]
            'foo:1,foo2:2'        | 'stringToIntegerMap'           || [foo: 1, foo2: 2]
            '[1:10,2:20]'         | 'integerToIntegerMap'          || [(1): 10, (2): 20]
            '1:10,2:20'           | 'integerToIntegerMap'          || [(1): 10, (2): 20]
            '[:]'                 | 'integerToIntegerMap'          || [:]
            'null'                | 'integerToIntegerMap'          || null
    }
}
