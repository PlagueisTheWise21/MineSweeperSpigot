# MineSweeper

## Overview
- A simple GUI for players to use
- Easy to use
- Can give rewards
- Ability to open game from other plugins (using commands)

## Commands
- /minesweeper <mine amount>
- <mine amount> must be within 1 and 53


## Default Configuration
```
give-rewards: true

#if 'give-rewards' are true, input what commands to execute when a player win a game
#use %player% to reference the player name

commands:
  - give %player% diamond
  - give %player% emerald
```

For more information check: https://www.spigotmc.org/resources/minesweeper-1-15.80878/