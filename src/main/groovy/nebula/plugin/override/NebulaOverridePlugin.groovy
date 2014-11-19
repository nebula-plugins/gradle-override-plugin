package nebula.plugin.override

import com.sun.org.apache.bcel.internal.classfile.Unknown
import nebula.plugin.override.reader.AggregateOverrideReader
import nebula.plugin.override.reader.OverrideReader
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin for overriding properties in a project from the command line. The main use case for this functionality is to
 * avoid the introduction of "magic" system/project properties for providing user-driven property values as part of build
 * logic. Property names are provided as String and are looked up by dot notation. If no matching property can be resolved,
 * the build fails.
 */
class NebulaOverridePlugin implements Plugin<Project> {

    void apply(Project project) {
        OverrideReader overrideReader = new AggregateOverrideReader()
        def overrideProperties = overrideReader.parseProperties()
        
        project.afterEvaluate {
            List<UnknownPropertyException> upes = new ArrayList<UnknownPropertyException>();
            overrideProperties.each { propertyName, overrideValue -> 
                project.logger.info "Overriding property '$propertyName' with '$overrideValue'."
                OverrideStrategy dotNotationWalkerOverrideStrategy = new DotNotationWalkerOverrideStrategy()
                try {
                    dotNotationWalkerOverrideStrategy.apply(project, propertyName, overrideValue)
                } catch(UnknownPropertyException upe) {
                    upes.add(upe)
                }
            }
            // TODO Use warning plugin to report on unfound properties
            upes.each { upe ->
                project.logger.warn(upe.getMessage());
            }
        }
    }
}