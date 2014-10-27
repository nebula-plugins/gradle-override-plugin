package nebula.plugin.override

import nebula.test.functional.ExecutionResult
import spock.lang.Ignore
import spock.lang.Issue
import spock.lang.Unroll

@Ignore
@Issue("https://github.com/nebula-plugins/gradle-override-plugin/issues/3")
class ComplexTypePropertyIntegrationTest extends BaseNebulaOverridePluginIntegrationTest {

    @Unroll("'#commandLineParameter' should be converted to '#expectedValue'")
    def "Should allow to override List property"() {
        given:
            buildFile << """
                class MyExtension {
                    List myList
                }

                extensions.create('example', MyExtension)

                assert example.myList == null

                task checkOverridenProperties {
                    doLast {
                        assert example.myList == $expectedValue
                    }
                }
                """
        when:
            ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties', "-Doverride.example.myList=$commandLineParameter")
        then:
            executionResult.standardOutput.contains(':checkOverridenProperties')
        where:
            commandLineParameter || expectedValue
            'foo,bar'            || "['foo', 'bar']"
            '[1,2]'              || "[1, 2]"
    }
}
