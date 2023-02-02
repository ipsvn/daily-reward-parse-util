package lol.svn.reward;

import java.util.Arrays;

public enum GameType {

    WALLS3("Mega Walls"),
    QUAKECRAFT("Quakecraft"),
    WALLS("Walls"),
    PAINTBALL("Paintball"),
    SURVIVAL_GAMES("Blitz SG"),
    TNTGAMES("TNT Games"),
    VAMPIREZ("VampireZ"),
    ARCADE("Arcade"),
    ARENA("Arena"),
    UHC("UHC"),
    MCGO("Cops and Crims"),
    BATTLEGROUND("Warlords"),
    SUPER_SMASH("Smash Heroes"),
    GINGERBREAD("Turbo Kart Racers"),
    SKYWARS("SkyWars"),
    TRUE_COMBAT("CrazyWalls"),
    SPEEDUHC("Speed UHC"),
    BEDWARS("Bed Wars"),
    BUILD_BATTLE("Build Battle"),
    MURDER_MYSTERY("Murder Mystery"),
    DUELS("Duels"),
    LEGACY("Classic"),
    UNKNOWN("Unknown");

    private final String name;

    GameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static GameType from(String input) {
        if (input == null) return null;
        return Arrays.stream(values())
                .filter(gameType -> gameType.name().equals(input))
                .findFirst()
                .orElse(GameType.UNKNOWN);
    }

}
