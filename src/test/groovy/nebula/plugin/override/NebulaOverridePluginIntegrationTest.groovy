package nebula.plugin.override

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import org.apache.commons.lang.exception.ExceptionUtils
import spock.lang.Ignore
import spock.lang.Issue

class NebulaOverridePluginIntegrationTest extends IntegrationSpec {
    def setup() {
        buildFile << """
        apply plugin: nebula.plugin.override.NebulaOverridePlugin
        """
    }

    def "Fails build if property to be overriden cannot be resolved"() {
        setup:
        System.setProperty('override.example.myProp', 'replaced')

        when:
        ExecutionResult executionResult = runTasksWithFailure('tasks')

        then:
        executionResult.failure
        ExceptionUtils.getRootCause(executionResult.failure).message == "Unknown property with name 'example' on instance of type class org.gradle.api.internal.project.DefaultProject_Decorated (Full property path: 'example.myProp')"

        cleanup:
        System.clearProperty('override.example.myProp')
    }

    def "Can override standard and custom project properties"() {
        setup:
        System.setProperty('override.myProp', 'replaced')
        System.setProperty('override.otherProp', '1')
        System.setProperty('override.buildDir', 'target')

        buildFile << """
        ext {
            myProp = 'test'
            otherProp = 0
        }

        assert ext.myProp == 'test'
        assert ext.otherProp == 0

        task checkOverridenProperties {
            doLast {
                assert myProp == 'replaced'
                assert otherProp == 1
                assert buildDir == project.file('target')
            }
        }
        """

        when:
        ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties')

        then:
        executionResult.standardOutput.contains(':checkOverridenProperties')

        cleanup:
        System.clearProperty('override.myProp')
        System.clearProperty('override.otherProp')
        System.clearProperty('override.buildDir')
    }

    def "Can override task extra properties"() {
        setup:
        System.setProperty('override.myTask.myProp', 'replaced')
        System.setProperty('override.myTask.otherProp', '1')

        buildFile << """
        task myTask {
            ext {
                myProp = 'test'
                otherProp = 0
            }
        }

        assert myTask.myProp == 'test'
        assert myTask.otherProp == 0

        task checkOverridenProperties {
            doLast {
                assert myTask.myProp == 'replaced'
                assert myTask.otherProp == 1
            }
        }
        """

        when:
        ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties')

        then:
        executionResult.standardOutput.contains(':checkOverridenProperties')

        cleanup:
        System.clearProperty('override.myTask.myProp')
        System.clearProperty('override.myTask.otherProp')
    }

    def "Can override custom task input and output properties"() {
        setup:
        System.setProperty('override.myTask.myProp', 'replaced')
        System.setProperty('override.myTask.otherProp', '1')
        System.setProperty('override.myTask.output', 'target')

        buildFile << """
        class MyTask extends DefaultTask {
            @Input
            String myProp

            @Input
            Integer otherProp

            @OutputFile
            File output

            @TaskAction
            void doNothing() {}
        }

        task myTask(type: MyTask) {
            myProp = 'test'
            otherProp = 0
            output = project.file('output')
        }

        task checkOverridenProperties {
            doLast {
                assert myTask.myProp == 'replaced'
                assert myTask.otherProp == 1
                assert myTask.output == new File('target')
            }
        }
        """

        when:
        ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties')

        then:
        executionResult.standardOutput.contains(':checkOverridenProperties')

        cleanup:
        System.clearProperty('override.myTask.myProp')
        System.clearProperty('override.myTask.otherProp')
        System.clearProperty('override.myTask.output')
    }

    def "Can override properties provided by java plugin"() {
        setup:
        System.setProperty('override.sourceCompatibility', '1.7')
        System.setProperty('override.targetCompatibility', '1.7')
        System.setProperty('override.testResultsDirName', 'my-test-results')
        System.setProperty('override.javadoc.maxMemory', '1024m')

        buildFile << """
        apply plugin: 'java'

        sourceCompatibility = '1.6'
        targetCompatibility = '1.6'

        assert !javadoc.maxMemory

        task checkOverridenProperties {
            doLast {
                assert sourceCompatibility == JavaVersion.VERSION_1_7
                assert targetCompatibility == JavaVersion.VERSION_1_7
                assert testResultsDirName == 'my-test-results'
                assert javadoc.maxMemory == '1024m'
            }
        }
        """

        when:
        ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties')

        then:
        executionResult.standardOutput.contains(':checkOverridenProperties')

        cleanup:
        System.clearProperty('override.sourceCompatibility')
        System.clearProperty('override.targetCompatibility')
        System.clearProperty('override.testResultsDirName')
        System.clearProperty('override.javadoc.maxMemory')
    }

    def "Can override deeply-nested extension properties"() {
        setup:
        System.setProperty('override.example.myProp', 'replaced')
        System.setProperty('override.example.nested.otherProp', '1')
        System.setProperty('override.example.nested.deeper.deep', 'true')

        buildFile << """
        class MyExtension {
            String myProp = 'test'
            Nested nested = new Nested()

            class Nested {
                Integer otherProp = 0
                Deeper deeper = new Deeper()

                class Deeper {
                    Boolean deep = Boolean.FALSE
                }
            }
        }

        extensions.create('example', MyExtension)

        assert example.myProp == 'test'
        assert example.nested.otherProp == 0
        assert !example.nested.deeper.deep

        task checkOverridenProperties {
            doLast {
                assert example.myProp == 'replaced'
                assert example.nested.otherProp == 1
                assert example.nested.deeper.deep
            }
        }
        """

        when:
        ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties')

        then:
        executionResult.standardOutput.contains(':checkOverridenProperties')

        cleanup:
        System.clearProperty('override.example.myProp')
        System.clearProperty('override.example.nested.otherProp')
        System.clearProperty('override.example.nested.deeper.deep')
    }

    @Ignore("Broken as of 2.0.0")
    @Issue("https://github.com/nebula-plugins/gradle-override-plugin/issues/1")
    def "should allow to override not initialized Boolean property with false value"() {
        given:
        buildFile << """
        class MyExtension {
            Boolean myBoolean
        }

        extensions.create('example', MyExtension)

        assert example.myBoolean == null

        task checkOverridenProperties {
            doLast {
                assert example.myBoolean == false
            }
        }
        """

        when:
        ExecutionResult executionResult = runTasksSuccessfully('checkOverridenProperties', '-Doverride.example.myBoolean=false')

        then:
        executionResult.standardOutput.contains(':checkOverridenProperties')
    }
}
