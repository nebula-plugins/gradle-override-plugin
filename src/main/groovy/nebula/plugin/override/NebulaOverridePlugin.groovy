package nebula.plugin.override

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
            overrideProperties.each { propertyName, overrideValue -> 
                project.logger.info "Overriding property '$propertyName' with '$overrideValue'."
                OverrideStrategy dotNotationWalkerOverrideStrategy = new DotNotationWalkerOverrideStrategy()
                dotNotationWalkerOverrideStrategy.apply(project, propertyName, overrideValue)
            }
        }
    }
}