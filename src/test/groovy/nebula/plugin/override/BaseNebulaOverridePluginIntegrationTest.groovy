package nebula.plugin.override

import nebula.test.IntegrationSpec

abstract class BaseNebulaOverridePluginIntegrationTest extends IntegrationSpec {

    def setup() {
        buildFile << """
        apply plugin: nebula.plugin.override.NebulaOverridePlugin
        """
    }
}
