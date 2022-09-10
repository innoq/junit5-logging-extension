# JUnit 5 logging extension
*- Sometimes logging is important*

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.innoq/junit5-logging-extension/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.innoq/junit5-logging-extension)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Open Issues](https://img.shields.io/github/issues/innoq/junit5-logging-extension.svg)](https://github.com/innoq/junit5-logging-extension/issues)
[![Build Status](https://github.com/innoq/junit5-logging-extension/actions/workflows/main.yml/badge.svg)](https://github.com/innoq/junit5-logging-extension/actions/workflows/main.yml)

[JUnit 5](https://junit.org/junit5/) extension for testing log statements.


## Quick Start

Add junit5-logging-extension as dependency within test scope to your project.

### Apache Maven

```xml
<dependency>
  <groupId>com.innoq</groupId>
  <artifactId>junit5-logging-extension</artifactId>
  <version>0.1.0</version>
  <scope>test</scope>
</dependency>
```

### Gradle Groovy DSL

```
testImplementation 'com.innoq:junit5-logging-extension:0.1.0'
```

### Gradle Groovy DSL

```
testImplementation("com.innoq:junit5-logging-extension:0.1.0")
```

Use the extension within your JUnit 5 tests:

```java
@Logging
class SomeTest {

    Logger LOG = LoggerFactory.getLogger(SomeTest.class);

    @Test
    void someTest(LoggingEvents events) {
        LOG.info("Some log message");

        assertThat(events.isEmpty()).isFalse();
    }
}
```


## Code of Conduct

[Contributor Code of Conduct](./CODE_OF_CONDUCT.md). By participating in this
project you agree to abide by its terms.


## License

junit5-logging-extension is Open Source software released under the
[Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
