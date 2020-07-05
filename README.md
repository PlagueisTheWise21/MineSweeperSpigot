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
# 
# possible buy in types:
# - items
# - vault

commands:
- give %player% diamond
- give %player% emerald
give-rewards: true
buy-in: true
buy-in-type: vault
default-bombs: 5
rewards:
- give %player% diamond
- give %player% emerald
cost-items:
- 1
cost-vault: 35

```

For more information check: https://www.spigotmc.org/resources/minesweeper-1-15.80878/
