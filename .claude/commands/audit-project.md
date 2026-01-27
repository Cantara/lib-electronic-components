Evaluate a project's LLM-assisted development structure against the quality rubric.

Read and follow the prompt at `new-project-setup/prompts/audit-project.md`.

The target project path is: $ARGUMENTS

If no path is provided, use the current project directory.

Key steps:
1. Load rubric from `new-project-setup/evaluation/rubric.md`
2. Run pass/fail prerequisites from `new-project-setup/evaluation/checklist.md`
3. Analyze the project's meta-layer (CLAUDE.md, skills, history, docs)
4. Score each of four dimensions (25 points each, 100 total):
   - Structure: Does the meta-layer exist and follow conventions?
   - Content Quality: Is content accurate, specific, and actionable?
   - Operational Integration: Does it connect to actual workflows?
   - Evolution Readiness: Can it grow and improve over time?
5. Generate a detailed report with scores, justifications, and recommendations
6. Save report to `new-project-setup/feedback/project-reports/`
7. Present results to user
