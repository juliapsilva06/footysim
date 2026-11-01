# ⚽ Julia's Football Simulator

A football (soccer) management game where you pick a club, build a starting XI, and simulate a match. It exists in two forms:

- **Java version** — a text-based terminal game (the original).
- **TypeScript version** — a browser-based port built with React, currently in progress.

Both share the same core idea: choose a coach name, pick a club from a database of squads, choose a formation, and fill each position with players from your roster.

---

## Features

### Java (terminal)

- Enter a coach name and choose a club from the team database
- Meet a random player from your squad, with a short branching intro dialogue
- Pick a formation: **4-3-3**, **4-4-2**, **3-5-2**, or **5-3-2**
- Build your starting XI position by position (goalkeeper, defenders, midfielders, forwards) by typing jersey numbers
- Match simulation (`Team.gameSim`):
  - Each lineup's strength is a rating average with positional weights (GK 0.8, DEF 1.1, MID 1.5, FWD 1.4)
  - A sigmoid on the rating difference sets each side's expected goals
  - Goals are drawn from a Poisson distribution
  - A minute-by-minute match feed shows goals, yellow cards, and red cards
- Roster relationships (e.g. teammates, family) stored as an undirected graph of players

### TypeScript (browser)

- The same game flow rebuilt as a state machine with no I/O, so it can be driven by clicks and tested directly
- React lineup screen with checkbox player selection and inline validation errors
- Vitest tests for the game flow

---

## Project structure

```
.
├── database/                  # Team data (not included in this listing)
│   ├── TEAMS.txt              # team name,filename
│   └── <team>.txt             # one file per squad
│
├── java/
│   ├── GameManager.java       # Entry point: intro, team choice, formation, lineup building
│   ├── DataManager.java       # Reads TEAMS.txt and squad files into Team/Player objects
│   ├── Team.java              # Roster, position filters, starting XI, match simulation
│   ├── Player.java            # Player model (name, country, age, position, number, rating)
│   ├── Graph.java             # Graph interface (CS 10, Dartmouth)
│   ├── AdjacencyMapGraph.java # Graph implementation used for roster relationships (CS 10)
│   └── TeamTest.java          # Manual test: loads a squad and sorts it by jersey number
│
└── typescript/
    ├── types.ts               # Player, Team, Position types
    ├── gameFlow.ts            # Game state machine (replaces GameManager's control flow)
    ├── gameFlow.test.ts       # Vitest tests
    ├── LineupScreen.tsx       # React component for picking players by position
    └── PLAYABLE_PROTOTYPE_PLAN.md
```

---

## Data format

`database/TEAMS.txt` maps each club name to its squad file (without the `.txt` extension):

```
Club Name,squadfile
```

Each squad file has one player per line:

```
name,country,age,position,number,rating
```

`position` must be one of `Goalkeeper`, `Defender`, `Midfielder`, or `Forward`. Each squad needs enough players at every position to fill whichever formation you choose.

---

## Running the Java version

Requires a JDK. From the project root (so the `database/` folder resolves correctly):

```bash
javac *.java
java GameManager
```

Follow the prompts. When picking players, type **jersey numbers exactly as shown**, and separate multiple numbers with commas and **no spaces** (e.g. `2,3,4,5`).

---

## Running the TypeScript version

The browser app is still being set up. The planned stack is Vite, React, TypeScript, and Vitest. Once the project scaffolding is in place, the workflow will be:

```bash
npm install
npm run dev     # start the dev server
npm test        # run Vitest
npm run build   # production build
```

See [`PLAYABLE_PROTOTYPE_PLAN.md`](typescript/PLAYABLE_PROTOTYPE_PLAN.md) for the full roadmap.

---

## How the TypeScript port works

`GameManager.playGame()` is one long method that blocks on `Scanner.nextLine()` and paces itself with `Thread.sleep()`. A browser can't work that way, so the port splits it into three parts:

1. **`Step`**: a type describing exactly where the player is (`enterCoachName`, `chooseTeam`, `intro`, `chooseFormation`, `pickPosition`, `lineupComplete`).
2. **Pure functions** like `chooseFormation` and `pickPlayers` that take the current state plus a user action and return the next state.
3. **React components** that render the current step and call those functions on click.

The four near-identical position loops in `lineupBuildHelper` collapse into a single `pickPlayers` function.

---

## Status and roadmap

**Java**
- [x] Coach name, team selection, intro dialogue
- [x] Formation choice and starting XI builder
- [x] Match simulation with goals and cards (in `Team.gameSim`)
- [ ] Hook the match simulation into `GameManager`
- [ ] Build a team and players from scratch
- [ ] Bench management
- [ ] 1v1 player input
- [ ] Career mode vs. arcade mode

**TypeScript prototype** (see the plan file for detail)
- [x] Game flow state machine
- [x] Lineup selection screen
- [ ] Vite/React/Vitest setup and app shell
- [ ] Bundled sample clubs and an auto-selected opponent
- [ ] Stronger validation (blank names, duplicates, wrong-position players)
- [ ] Simple match result step with injectable randomness
- [ ] Full-loop tests and responsive layout check

---

## Credits

Created by **Julia Silva**.

`Graph.java` and `AdjacencyMapGraph.java` come from Dartmouth CS 10 (Chris Bailey-Kellogg and Tim Pierson), based on Goodrich & Tamassia.