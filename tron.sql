CREATE TABLE "bikeclasses"
(
    [BikeClassId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    [Name] NVARCHAR(120),
    [TotalWins] INTEGER DEFAULT 0,
    [TotalGames] INTEGER DEFAULT 0,
    [AverageTurnsSurvived] NUMERIC(6,2) DEFAULT 0.0
);

CREATE TABLE "games"
(
    [GameId] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    [GameName] NVARCHAR(120),
    [WinnerClassId] INTEGER  NOT NULL,
    [TotalTurns] INTEGER,
    [NumBikes] INTEGER
);

CREATE TABLE "gamesbikes"
(
  [GameId] INTEGER NOT NULL,
  [BikeClassId] INTEGER  NOT NULL,
  FOREIGN KEY ([BikeClassId]) REFERENCES "bikeclasses"([BikeClassId]) 
      ON DELETE NO ACTION ON UPDATE NO ACTION
); 


INSERT INTO "bikeclasses" (BikeClassId, Name) VALUES (0, "SillyBike");
INSERT INTO "games" (GameID, GameName) VALUES (0, "Gametest");



