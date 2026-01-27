---
skill: task-delegation
version: 1.0.0
domain: workflow
triggers:
  - "cost optimization"
  - "delegate to haiku"
  - "task delegation"
  - "efficient model usage"
---

# Task Delegation Skill

## When to Use This Skill

Use this skill when:
- Planning how to approach a multi-step task
- Deciding which model to use for sub-tasks
- Optimizing cost for straightforward work
- Delegating pattern-following tasks

## Delegation Matrix

| Delegate to Haiku (12x cheaper) | Use Sonnet/Opus Directly |
|--------------------------------|--------------------------|
| Test fixes following known patterns | Architectural decisions |
| Simple refactoring (rename, extract) | Complex debugging |
| Documentation updates | Ambiguous requirements |
| Straightforward bug fixes | New feature design |
| Pattern application (copy-adapt-paste) | Cross-cutting concerns |
| Code formatting/linting fixes | Performance optimization |
| Boilerplate generation | Security-sensitive changes |

## How to Delegate

When using Claude Code's Task tool with `model: "haiku"`:

1. **Provide complete context**: Haiku needs explicit instructions -- don't assume it knows project conventions
2. **Include examples**: Show the exact pattern to follow with a concrete example
3. **Specify files**: List exact file paths to read and modify
4. **Define done**: State what "success" looks like explicitly
5. **Limit scope**: One focused task per delegation, not "fix all tests"

### Good Delegation Prompt
```
Read the test file at src/test/FooTest.java. The test testBar() fails because
the method signature changed from bar(String) to bar(String, int). Update the
test to pass the additional parameter with value 0. Follow the same pattern as
testBaz() on line 45 which already uses the two-parameter version.
```

### Bad Delegation Prompt
```
Fix the failing tests.
```

## Cost Impact

Assuming a typical session:
- **Without delegation**: 100% Opus/Sonnet cost
- **With delegation**: ~40% Opus/Sonnet + ~60% Haiku = ~45% total cost
- **Savings**: ~55% per session for projects with delegatable work

These numbers vary by project. Projects with heavy pattern-following work save more.

## When NOT to Delegate

Never delegate work where:
- The correct approach is unclear or debatable
- Multiple valid solutions exist and judgment is needed
- The task affects security, authentication, or authorization
- You need to understand the broader system to make the change
- The task requires reading and synthesizing large amounts of code

## Checklist

Before delegating to Haiku:
- [ ] Task has a clear, unambiguous definition of done
- [ ] Required file paths are identified
- [ ] A concrete example of the pattern exists
- [ ] The task doesn't require architectural judgment
- [ ] Failure is easy to detect (tests, type checks, lint)

## Related Skills

- `/architecture` - Architectural tasks that should NOT be delegated
- `/documentation-maintenance` - Doc tasks CAN be delegated if straightforward
