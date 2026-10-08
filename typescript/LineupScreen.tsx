// Example of the piece GameManager never had: something that actually
// renders a step and turns clicks into calls to gameFlow.ts.
//
// This replaces this chunk of lineupBuildHelper():
//
//   System.out.println("\nYour options are:");
//   for (Player p : team.getDefenders()) { System.out.println(p + " RATING: " + p.getRating()); }
//   String defNums = sc.nextLine();
//   ... parse, validate count, add to startingXI ...
//
// with a checkbox list + a button, and delegates the actual validation back
// to pickPlayers() so the rule ("pick exactly N") lives in one place, not
// copy-pasted per position like in the Java version.

import { useState } from "react";
import { GameState, Step, pickPlayers } from "../game/gameFlow";
import { Player, playersByPosition } from "../game/types";

interface Props {
  state: GameState;
  onAdvance: (next: GameState) => void;
}

export function LineupScreen({ state, onAdvance }: Props) {
  const [error, setError] = useState<string | null>(null);
  const [selected, setSelected] = useState<Player[]>([]);

  if (state.step.name !== "pickPosition") return null;
  const step: Step & { name: "pickPosition" } = state.step;
  const candidates = playersByPosition(step.team, step.position);

  function toggle(player: Player) {
    setSelected((prev) =>
      prev.includes(player) ? prev.filter((p) => p !== player) : [...prev, player]
    );
  }

  function submit() {
    try {
      const next = pickPlayers(state, selected);
      setError(null);
      setSelected([]);
      onAdvance(next);
    } catch (e) {
      // This is the direct replacement for "Invalid choice! Please try again."
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h2>Pick {step.needed} {step.position}(s)</h2>
      {candidates.map((p) => (
        <label key={p.number} style={{ display: "block" }}>
          <input type="checkbox" checked={selected.includes(p)} onChange={() => toggle(p)} />
          {p.name} (#{p.number}) - rating {p.rating}
        </label>
      ))}
      {error && <p style={{ color: "crimson" }}>{error}</p>}
      <button onClick={submit}>Confirm</button>
    </div>
  );
}
