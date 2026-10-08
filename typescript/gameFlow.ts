// This is the TS replacement for GameManager.java's control flow.
//
// The big mental shift: GameManager.playGame() is one long method that BLOCKS
// on Scanner.nextLine() and paces itself with Thread.sleep(). A browser can't
// block waiting for input like that, and it shouldn't try to. So instead of
// "one function that runs the whole game," you get:
//
//   1. A `Step` type: a snapshot of exactly where the player is right now.
//   2. Small pure functions: (current state, this user action) -> next state.
//   3. A UI layer (React) that renders whatever the current Step is, and calls
//      one of these functions when the player clicks/submits something.
//
// Nothing in this file prints, sleeps, or reads input. That's the point: it's
// the same logic GameManager had, just with I/O surgically removed so it can
// be driven by clicks instead of a terminal. It's also now trivially testable
// with Vitest, the way TeamTest.java was reaching for.

import { Player, Team, Position } from "./types";

export interface Formation {
  def: number;
  mid: number;
  ata: number;
}

export const FORMATIONS: Record<string, Formation> = {
  "4-3-3": { def: 4, mid: 3, ata: 3 },
  "4-4-2": { def: 4, mid: 4, ata: 2 },
  "3-5-2": { def: 3, mid: 5, ata: 2 },
  "5-3-2": { def: 5, mid: 3, ata: 2 },
};

// Each variant carries exactly the data that step needs to render.
// This replaces the sequence of method calls (playGame -> lineupBuild ->
// lineupBuildHelper) with a single value the UI can switch on.
export type Step =
  | { name: "enterCoachName" }
  | { name: "chooseTeam"; teamNames: string[] }
  | { name: "intro"; team: Team; greeter: Player }
  | { name: "chooseFormation"; team: Team }
  | { name: "pickPosition"; team: Team; formation: Formation; position: Position; needed: number; startingXI: Player[] }
  | { name: "lineupComplete"; team: Team; startingXI: Player[] };

export interface GameState {
  coachName: string;
  step: Step;
}

export function startGame(): GameState {
  return { coachName: "", step: { name: "enterCoachName" } };
}

// Replaces: "Please type in your name to start" + sc.nextLine()
export function submitCoachName(name: string, teamNames: string[]): GameState {
  return { coachName: name, step: { name: "chooseTeam", teamNames } };
}

// Replaces: teamChoice = sc.nextLine() + DataManager.handleTeamChoice(...) + the retry loop.
// There's no while(true) here: if `team` is null the caller (the UI) just
// keeps showing the same chooseTeam screen with an error message, instead of
// looping inside game logic.
export function chooseTeam(coachName: string, team: Team): GameState {
  const greeter = team.roster[Math.floor(Math.random() * team.roster.length)];
  return { coachName, step: { name: "intro", team, greeter } };
}

// Replaces the "a) Nice to meet you / b) Starting XI? / c) I'm out" branch.
// The banter text itself lives in the UI/copy layer, not here — this only
// decides what screen comes next.
export function confirmIntro(state: GameState): GameState {
  if (state.step.name !== "intro") return state;
  return { ...state, step: { name: "chooseFormation", team: state.step.team } };
}

// Replaces the a/b/c/d switch in lineupBuild().
export function chooseFormation(state: GameState, formationKey: keyof typeof FORMATIONS): GameState {
  if (state.step.name !== "chooseFormation") return state;
  const formation = FORMATIONS[formationKey];
  return {
    ...state,
    step: { name: "pickPosition", team: state.step.team, formation, position: "Goalkeeper", needed: 1, startingXI: [] },
  };
}

// Replaces the four near-identical while(true) blocks in lineupBuildHelper
// (goalkeeper, defenders, midfielders, forwards). Same validation, one
// function instead of four copy-pasted loops.
const POSITION_ORDER: Position[] = ["Goalkeeper", "Defender", "Midfielder", "Forward"];

export function pickPlayers(state: GameState, chosen: Player[]): GameState {
  if (state.step.name !== "pickPosition") return state;
  const { team, formation, position, needed, startingXI } = state.step;

  if (chosen.length !== needed) {
    // Same as "Invalid choice! Please try again." — the caller re-renders
    // the same step, this time showing that error next to it.
    throw new Error(`Pick exactly ${needed} ${position.toLowerCase()}(s).`);
  }

  const updatedXI = [...startingXI, ...chosen];
  const nextIndex = POSITION_ORDER.indexOf(position) + 1;

  if (nextIndex >= POSITION_ORDER.length) {
    return { ...state, step: { name: "lineupComplete", team, startingXI: updatedXI } };
  }

  const nextPosition = POSITION_ORDER[nextIndex];
  const nextNeeded = nextPosition === "Defender" ? formation.def
    : nextPosition === "Midfielder" ? formation.mid
    : nextPosition === "Forward" ? formation.ata
    : 1;

  return {
    ...state,
    step: { name: "pickPosition", team, formation, position: nextPosition, needed: nextNeeded, startingXI: updatedXI },
  };
}
