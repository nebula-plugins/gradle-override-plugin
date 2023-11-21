package nebula.plugin.override

import nebula.test.IntegrationTestKitSpec
import spock.lang.Issue

class NebulaOverridePluginIntegrationTest extends IntegrationTestKitSpec {
    def setup() {
        buildFile << """
        plugins {
            id 'com.netflix.nebula.override'
        }       
        """
    }

    def "Warn if property to be overriden cannot be resolved"() {
        setup:
        System.setProperty('override.example.myProp', 'replaced')

        when:
        def executionResult = runTasks('tasks')

        then:
        executionResult.output.contains("Unknown property with name 'example' on instance of type class org.gradle.api.internal.project.DefaultProject_Decorated (Full property path: 'example.myProp')")

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
        def executionResult = runTasks('checkOverridenProperties')

        then:
        executionResult.output.contains(':checkOverridenProperties')

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
        def executionResult = runTasks('checkOverridenProperties')

        then:
        executionResult.output.contains(':checkOverridenProperties')

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
        def executionResult = runTasks('checkOverridenProperties')

        then:
        executionResult.output.contains(':checkOverridenProperties')

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

        java {
            sourceCompatibility = '1.6'
            targetCompatibility = '1.6'
        }
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
        System.setProperty('ignoreDeprecations', 'true')
        when:
        def executionResult = runTasks('checkOverridenProperties')

        then:
        executionResult.output.contains(':checkOverridenProperties')

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
        def executionResult = runTasks('checkOverridenProperties')

        then:
        executionResult.output.contains(':checkOverridenProperties')

        cleanup:
        System.clearProperty('override.example.myProp')
        System.clearProperty('override.example.nested.otherProp')
        System.clearProperty('override.example.nested.deeper.deep')
    }

    @Issue("https://github.com/nebula-plugins/gradle-override-plugin/issues/1")
    def "Should allow to override not initialized Boolean property with false value"() {
        given:
        buildFile << """
        class MyExtension {
            String myProp
            Boolean myBoolean
        }

        extensions.create('example', MyExtension)

        assert example.myProp == null
        assert example.myBoolean == null

        task checkOverridenProperties {
            doLast {
                assert example.myProp == 'test'
                assert example.myBoolean == false
            }
        }
        """

        when:
        def executionResult = runTasks('checkOverridenProperties', '-Doverride.example.myProp=test',
                                                               '-Doverride.example.myBoolean=false')

        then:
        executionResult.output.contains(':checkOverridenProperties')

        cleanup: 'Left over from inprocess tests'
        System.clearProperty('override.example.myProp')
        System.clearProperty('override.example.myBoolean')
    }
}
