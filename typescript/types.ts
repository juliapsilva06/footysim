// Ports of Player.java / Team.java's shape (not behavior).


export type Position = "Goalkeeper" | "Defender" | "Midfielder" | "Forward";

export interface Player {
  name: string;
  age: number;
  country: string;
  number: number;
  position: Position;
  rating: number;
}

export interface Team {
  name: string;
  coach: string;
  roster: Player[];
}

export function playersByPosition(team: Team, position: Position): Player[] {
  return team.roster
    .filter((p) => p.position === position)
    .sort((a, b) => a.rating - b.rating);
}
