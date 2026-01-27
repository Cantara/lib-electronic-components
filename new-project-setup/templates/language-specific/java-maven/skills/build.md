---
skill: build
version: 1.0.0
domain: java-maven
triggers:
  - "build"
  - "maven"
  - "compile"
  - "test"
  - "dependency"
---

# Java/Maven Build Skill

## When to Use This Skill

Use this skill when:
- Building the project or diagnosing build failures
- Adding or updating Maven dependencies
- Running tests or debugging test failures
- Configuring Maven plugins

## Common Commands

```bash
# Build
mvn clean install              # Full clean build with tests
mvn install -DskipTests        # Build without tests (faster)
mvn compile                    # Compile only (no tests, no package)

# Test
mvn test                       # Run all tests
mvn test -Dtest=ClassName      # Run single test class
mvn test -Dtest=Class#method   # Run single test method
mvn test -pl module-name       # Run tests in specific module

# Dependencies
mvn dependency:tree             # Show dependency tree
mvn dependency:analyze          # Find unused/undeclared dependencies
mvn versions:display-dependency-updates  # Check for updates

# Debug
mvn -X clean install           # Verbose output for debugging
mvn help:effective-pom         # Show resolved POM after inheritance
```

## POM Patterns

### Adding a Dependency

```xml
<dependency>
    <groupId>{{GROUP_ID}}</groupId>
    <artifactId>{{ARTIFACT_ID}}</artifactId>
    <version>${{{VERSION_PROPERTY}}}</version>
    {{SCOPE_IF_NOT_COMPILE}}
</dependency>
```

Always:
- Define version as a property in `<properties>` section
- Use `<scope>test</scope>` for test-only dependencies
- Check `mvn dependency:tree` for conflicts after adding

### Multi-Module Structure

```xml
<modules>
    <module>{{MODULE_NAME}}</module>
</modules>
```

Common issues:
- Module build order matters (parent must build before children)
- Use `-pl module-name -am` to build a module with its dependencies

## Common Build Failures

| Error | Likely Cause | Fix |
|-------|-------------|-----|
| `package does not exist` | Missing dependency or wrong version | Check `mvn dependency:tree` |
| `cannot find symbol` | Source/target mismatch or wrong Java version | Check `<maven.compiler.source>` |
| `test failures` | Code change broke tests | Run `mvn test -Dtest=FailingTest` |
| `artifact not found` | Wrong repository or unreleased version | Check `<repositories>` in POM |
| `OutOfMemoryError` | Large project, insufficient heap | Set `MAVEN_OPTS=-Xmx1024m` |

## Java Version Configuration

```xml
<properties>
    <maven.compiler.source>{{JAVA_VERSION}}</maven.compiler.source>
    <maven.compiler.target>{{JAVA_VERSION}}</maven.compiler.target>
</properties>
```

Or with the compiler plugin:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>{{JAVA_VERSION}}</release>
    </configuration>
</plugin>
```

## Checklist

Before completing build-related changes:
- [ ] `mvn clean install` passes
- [ ] No new compiler warnings introduced
- [ ] Dependencies use version properties, not hardcoded versions
- [ ] Test scope is correct for test-only dependencies
- [ ] No snapshot dependencies in release branches

## Related Skills

- `/architecture` - Module structure decisions
- `/task-delegation` - Delegate build fixes to Haiku when straightforward
