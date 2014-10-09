package nebula.plugin.override

import nebula.plugin.override.converter.ApacheCommonsTypeConverter
import nebula.plugin.override.converter.TypeConverter
import org.codehaus.groovy.runtime.NullObject
import org.gradle.api.Project

class DotNotationWalkerOverrideStrategy implements OverrideStrategy {
    TypeConverter typeConverter = new ApacheCommonsTypeConverter()
     
    @Override
    void apply(Project project, String propertyName, String overrideValue) {
        String[] extractedOptions = propertyName.split('\\.')
        
        def node = project
        def parent = null
        def fieldName = null

        extractedOptions.eachWithIndex { String entry, int index ->
            if(index == extractedOptions.size() - 1) {
                parent = node
                fieldName = entry
            }

            node = determineProperty(node, entry, propertyName)
        }

        Class clazz = node.getClass()

        // If no value is assigned, determine the property's class via the parent's metaClass
        if(clazz == NullObject) {
            clazz = parent.class.metaClass.properties.find { it.name == fieldName }.type
        }

        parent.setProperty(fieldName, typeConverter.convert(overrideValue, clazz))
    }

    /**
     * Checks if node has a property with the provided name.
     *
     * @param node Gradle domain object
     * @param propertyName Property name
     * @return Property instance
     * @throws UnknownPropertyException
     */
    def determineProperty(Object node, String propertyName, String fullOverridePath) {
        if(node.hasProperty(propertyName)) {
            return node.getProperty(propertyName)
        }

        throw new UnknownPropertyException("Unknown property with name '$propertyName' on instance of type ${node.getClass()} (Full property path: '$fullOverridePath')")
    }
}