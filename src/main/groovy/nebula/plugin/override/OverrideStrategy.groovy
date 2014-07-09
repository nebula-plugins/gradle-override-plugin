package nebula.plugin.override

import org.gradle.api.Project

interface OverrideStrategy {
    void apply(Project project, String propertyName, String overrideValue)
}