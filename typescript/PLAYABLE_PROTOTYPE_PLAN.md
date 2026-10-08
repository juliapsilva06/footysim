# Playable Football Prototype Plan

Build a small browser game that takes the player from choosing a club and building a valid starting XI through one simulated match. Reuse the TypeScript state-machine approach already in place, with bundled sample squads so the entire loop works without a backend.

## Steps

1. **Make the app runnable.** Add minimal Vite, React, TypeScript, and Vitest setup plus a browser entry point and app shell. Fix the lineup screen's imports, which currently point to a missing `game/` directory.
2. **Add playable data.** Bundle a few typed sample clubs with enough players at each position to support every existing formation. Use another club as the opponent and automatically pick its strongest eligible XI in the chosen formation.
3. **Complete the setup flow.** Render coach entry, club selection, intro, formation selection, position-based player picking, and lineup completion. Harden the rules: reject blank names, insufficient squads, wrong-position or foreign players, duplicates, and incorrect pick counts. Prevent selecting more than needed and show errors without losing progress.
4. **Add a simple match.** Extend the state flow with a result step. Simulate a final score from the two lineups' ratings, using simple positional weights inspired by the Java simulator. Make randomness injectable for repeatable tests. Show the score and outcome, with a restart action.
5. **Verify the full loop.** Test state progression, lineup validation, opponent lineup validity, and deterministic match results. Run the test suite and production build, then manually try the full flow at desktop and mobile widths.

## Relevant Files

- [`gameFlow.ts`](gameFlow.ts) — existing flow and formation rules; extend with validation and match state.
- [`types.ts`](types.ts) — shared player and team model.
- [`LineupScreen.tsx`](LineupScreen.tsx) — fix imports and tighten player selection behavior.
- [`gameFlow.test.ts`](gameFlow.test.ts) — expand the current tests with valid squads and full-flow coverage.
- `../java/Team.java` — reference positional rating weights only; no Java changes or full simulation port.
- Add the app shell and entry point, bundled club data, match simulation module, styles, and Vite/Vitest configuration under `typescript/`.

## Scope Decisions

- “Playable” includes one setup-to-match loop, not just a completed lineup.
- Keep the match to a final score and result; exclude the Java simulator's timed event feed, cards, and career behavior.
- Keep data bundled and the prototype self-contained. No backend, persistence, squad editor, or bench management.
- Automatically select the opponent and its XI to avoid adding another setup step.

## Verification

1. Run the Vitest suite and assert that a valid 11-player XI matching the selected formation reaches the match result step.
2. Test rejection of blank coach names, insufficient squads, wrong-position, foreign, or duplicate players, and incorrect selection counts. Verify injected randomness produces repeatable scores.
3. Run the production build and start the local dev server. Manually complete club selection, lineup building, match result, and restart at desktop and mobile widths.
