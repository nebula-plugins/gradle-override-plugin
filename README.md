gradle-override-plugin
==============

Override arbitrary properties in a Gradle build

## Usage

### Applying the Plugin

To include, add the following to your build.gradle

    buildscript {
      repositories { jcenter() }

      dependencies {
        classpath 'com.netflix.nebula:gradle-override-plugin:1.12.+'
      }
    }

    apply plugin: 'nebula-override'

### Tasks Provided

`<your tasks>`

### Extensions Provided

`<your extensions>`
