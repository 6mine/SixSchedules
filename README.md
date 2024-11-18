![title](https://github.com/user-attachments/assets/d08dd652-4274-47dc-bb69-176f385a7fdc)
An analogue of CMI Schedules, with some improvements.

## Config
```yaml
Events: # Event name
  Enabled: true # Enabled?
  MinPlayers: 5 # Minimum number of players to run
  MaxPlayers: 0 # Maximum number of players to run
  Delay: 1800 # Delay before re-triggering the event
  Commands: # Commands, 1 is selected using the odds and 1 execution is started, the next time is another
    1:
      Chance: 60 # 
      RandomizeCommands: true # Shuffle the commands or use them one at a time
      Commands:
        - "bair start ordinary_dungeons_hiding"
        - "bair start ordinary_edge_storage"
        - "bair start ordinary_mystical_chest"
        - "bair start ordinary_nether_treasure"
        - "bair start ordinary_sea_treasure"
    2:
      Chance: 30
      RandomizeCommands: true
      Commands:
        - "bair start rare_dungeons_hiding"
        - "bair start rare_edge_storage"
        - "bair start rare_mystical_chest"
        - "bair start rare_nether_treasure"
        - "bair start rare_sea_treasure"
    3:
      Chance: 10
      RandomizeCommands: true
      Commands:
        - "bair start legendary_dungeons_hiding"
        - "bair start legendary_edge_storage"
        - "bair start legendary_mystical_chest"
        - "bair start legendary_nether_treasure"
        - "bair start legendary_sea_treasure"
```

## Placeholders
displays the time in a format that is transmitted via the placeholder (for example, d.M.y H:m:s):
```
%sixschedules_nextin_<eventName>_<format>%
```
displays the time as a string, as "X days Y hours Z minutes" (for example, "1 hour 10 minutes"):
```
%sixschedules_nextin_<eventName>_string%
```
