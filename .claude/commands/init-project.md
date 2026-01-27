Initialize a new project with LLM-assisted development structure.

Read and follow the prompt at `new-project-setup/prompts/init-new-project.md`.

The target project path is: $ARGUMENTS

If no path is provided, ask the user for the target project path.

Key steps:
1. Gather project information (language, build system, tier, architecture)
2. Load templates from `new-project-setup/templates/`
3. Create directory structure based on selected tier
4. Generate CLAUDE.md (< 300 lines) from template
5. Create skills appropriate for the tier
6. Generate HISTORY.md (for standard+ tier)
7. Verify all files are complete with no unfilled placeholders
8. Report what was created
