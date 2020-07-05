# MineSweeper

## Overview
- A simple GUI for players to use
- Easy to use
- Can give rewards
- Ability to open game from other plugins (using commands)

## Commands
- /minesweeper "<mine amount>" 
- "<mine amount>" must be within 1 and 53
- if no mine amount specificed, default bomb value from config.yml will be used


## Default Configuration
```
# possible buy in types:
# - items
# - vault

buy-in:
  enable: true
  type: vault

# the default amount of bombs at the execution of "/minesweeper"

default-bombs: 5

#if 'give-rewards' are true, input what commands to execute when a player win a game
#use %player% to reference the player name

give-rewards: true
rewards:
  - give %player% diamond
  - give %player% emerald
  
#if 'buy-in' is true, input the item ID's of the items that the player can use to buy them in

cost: 
  items:
    - 265
  vault:
    amount: 35
    currency: $
```

For more information check: https://www.spigotmc.org/resources/minesweeper-1-15.80878/
