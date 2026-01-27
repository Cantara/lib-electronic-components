Add LLM-assisted development structure to an existing project.

Read and follow the prompt at `new-project-setup/prompts/retrofit-existing-project.md`.

The target project path is: $ARGUMENTS

If no path is provided, ask the user for the target project path.

Key steps:
1. Analyze existing state (check for CLAUDE.md, skills, docs)
2. Analyze the codebase (language, build system, architecture)
3. Perform gap analysis against the three tiers
4. Present findings and ask user which tier to target
5. Preserve existing content (never delete without confirmation)
6. Create missing structure from templates
7. Integrate with existing workflow
8. Verify and report changes

Important: Retrofitting is conservative -- add, don't replace.
