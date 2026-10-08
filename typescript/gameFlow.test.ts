// This is the kind of test TeamTest.java was reaching for, now actually
// possible in one line each because gameFlow.ts has zero I/O to work around.

import { describe, it, expect } from "vitest";
import { startGame, submitCoachName, chooseTeam, confirmIntro, chooseFormation, pickPlayers } from "./gameFlow";
import { Team } from "./types";

const spfc: Team = {
  name: "Sao Paulo FC",
  coach: "",
  roster: [
    { name: "Rafael", age: 30, country: "BRA", number: 1, position: "Goalkeeper", rating: 80 },
    { name: "Arboleda", age: 28, country: "ECU", number: 4, position: "Defender", rating: 78 },
    // ...rest of the roster
  ],
};

describe("gameFlow", () => {
  it("walks from coach name to formation choice", () => {
    let state = startGame();
    state = submitCoachName("Julia", [spfc.name]);
    state = chooseTeam("Julia", spfc);
    state = confirmIntro(state);
    state = chooseFormation(state, "4-3-3");

    expect(state.step.name).toBe("pickPosition");
  });

  it("rejects picking the wrong number of goalkeepers", () => {
    let state = startGame();
    state = chooseTeam("Julia", spfc);
    state = confirmIntro(state);
    state = chooseFormation(state, "4-3-3");

    expect(() => pickPlayers(state, [])).toThrow();
  });
});
