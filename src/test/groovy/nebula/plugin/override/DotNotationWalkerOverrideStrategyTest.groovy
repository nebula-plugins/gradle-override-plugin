package nebula.plugin.override

import nebula.test.ProjectSpec
import org.gradle.api.tasks.bundling.Jar

class DotNotationWalkerOverrideStrategyTest extends ProjectSpec {
    OverrideStrategy genericOverrideStrategy = new DotNotationWalkerOverrideStrategy()
    
    def "Throws exception if property cannot be resolved"() {
        when:
        genericOverrideStrategy.apply(project, 'doesntExist', 'test')
        
        then:
        Throwable t = thrown(UnknownPropertyException)
        t.message == "Unknown property with name 'doesntExist' on instance of type class org.gradle.api.internal.project.DefaultProject_Decorated (Full property path: 'doesntExist')"
    }
    
    def "Can override project property"() {
        given:
        project.version = '1.0'
        
        when:
        genericOverrideStrategy.apply(project, 'version', '2.0')
        
        then:
        project.version == '2.0'
    }
    
    def "Can override project extra property"() {
        given:
        project.ext.myProp = 'test'
        
        when:
        genericOverrideStrategy.apply(project, 'myProp', 'hello')
        
        then:
        project.myProp == 'hello'
    }
    
    def "Can override task property"() {
        given:
        project.task('jar', type: Jar)
        assert project.tasks.findByName('jar')
        assert !project.jar.appendix
        
        when:
        genericOverrideStrategy.apply(project, 'jar.appendix', 'myAppendix')
        
        then:
        project.jar.appendix == 'myAppendix'
    }
    
    def "Can override task extra property"() {
        given:
        project.task('myTask') {
            ext.myProp = 'test'
        }
        assert project.tasks.findByName('myTask')
        assert project.myTask.myProp == 'test'
        
        when:
        genericOverrideStrategy.apply(project, 'myTask.myProp', 'hello')
        
        then:
        project.myTask.myProp == 'hello'
    }
}