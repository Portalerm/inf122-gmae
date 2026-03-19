import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameFacade facade = new GameFacade();
        Scanner scanner = new Scanner(System.in);

        initializeMiniAdventureManager(facade.getMiniAdventureManager());

        System.out.println("========================================");
        System.out.println("        Welcome to GuildQuest!");
        System.out.println("========================================");

        PlayerProfile profile1 = selectOrCreateProfile(facade, scanner, 1);
        PlayerProfile profile2 = selectOrCreateProfile(facade, scanner, 2);

        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu:");

            List<MiniAdventureDefinition> adventures = facade.getMiniAdventureManager().getAllAdventures();
            for (int i = 0; i < adventures.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + adventures.get(i).getDisplayName());
            }
            int numAdventures = adventures.size();
            System.out.println("  " + (numAdventures + 1) + ") View My Profile (Player 1)");
            System.out.println("  " + (numAdventures + 2) + ") View My Profile (Player 2)");
            System.out.println("  " + (numAdventures + 3) + ") Quit");

            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            try {
                int menuChoice = Integer.parseInt(choice);

                if (menuChoice >= 1 && menuChoice <= numAdventures) {
                    launchAdventureByIndex(facade, scanner, menuChoice - 1, profile1, profile2);
                } else if (menuChoice == numAdventures + 1) {
                    profile1.displayProfile();
                } else if (menuChoice == numAdventures + 2) {
                    profile2.displayProfile();
                } else if (menuChoice == numAdventures + 3) {
                    running = false;
                    System.out.println("Goodbye!");
                } else {
                    System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
            }
        }
        scanner.close();
    }

    private static void initializeMiniAdventureManager(MiniAdventureManager manager) {
        manager.register(new MiniAdventureDefinition(
                "timed-raid",
                "Timed Raid Window",
                "A co-op turn-based raid where 2 players navigate a dungeon, fight monsters, collect objectives, and escape before time runs out.",
                new TimedRaidAdventureContext()));

        manager.register(new MiniAdventureDefinition(
                "escort",
                "Escort Across the Realm Window",
                "A co-op turn-based mission where 2 players traverse a dungeon filled with monsters and traps while one player escorts an NPC towards the exit.",
                new EscortAdventureContext()));
    }

    private static void launchAdventureByIndex(GameFacade facade, Scanner scanner,
            int adventureIndex, PlayerProfile profile1, PlayerProfile profile2) {
        Character c1 = profile1.getActiveCharacter();
        Character c2 = profile2.getActiveCharacter();

        Realm realm = buildRealm(adventureIndex);

        MiniAdventure adventure = facade.launchAdventureByIndex(adventureIndex, c1, c2, realm);
        if (adventure == null) {
            System.out.println("Error: Adventure not found.");
            return;
        }

        adventure.run(scanner);

        saveOutcome(facade, adventure.getContext(), profile1, profile2);
    }

    private static PlayerProfile selectOrCreateProfile(GameFacade facade, Scanner scanner, int playerNumber) {
        System.out.println("\n--- Player " + playerNumber + " Profile ---");

        List<String> existing = facade.getAllProfileNames();
        if (!existing.isEmpty()) {
            System.out.println("Existing profiles: " + String.join(", ", existing));
        }

        System.out.print("Enter your profile name (new or existing): ");
        String name = scanner.nextLine().trim();

        PlayerProfile profile = facade.findProfile(name);
        if (!profile.isNullProfile()) {
            System.out.println("Welcome back, " + profile.getName() + "!");
            Character active = profile.getActiveCharacter();
            if (active != null) {
                System.out.println("Playing as: " + active.getName() + " the " + active.getClassType()
                        + " (Level " + active.getLevel() + ")");
            } else {
                // Create a character if we don't have one already
                createCharacterForProfile(facade, scanner, profile);
            }
        } else {
            System.out.println("Creating new profile: " + name);
            profile = facade.createProfile(name);
            createCharacterForProfile(facade, scanner, profile);
        }

        return profile;
    }

    private static void createCharacterForProfile(GameFacade facade, Scanner scanner, PlayerProfile profile) {
        System.out.println("Classes: (w)arrior, (m)age, (a)rcher, (r)ogue, m(o)nk");
        System.out.print("Choose a class: ");

        String input = scanner.nextLine().trim().toLowerCase();
        if (input == null || input.isBlank()) {
            System.out.println("No class selected. Defaulting to warrior.");
            input = "w";
        } else if (!CharacterFactory.acceptableClasses.contains(input.trim().toLowerCase())) {
            System.out.println("Invalid class choice. Defaulting to warrior.");
            input = "w";
        }

        char classChoice = input.charAt(0);
        facade.createCharacterForProfile(profile, profile.getName(), classChoice);
        Character c = profile.getActiveCharacter();
        System.out.println("Created: " + c.getName() + " the " + c.getClassType());
    }

    private static Realm buildRealm(int adventureIndex) {
        LocalTimeRule raidRule = new LocalTimeRule() {
            @Override
            public LocalTime toLocal(Time worldTime) {
                int hours = worldTime.getHour() + 2;
                int days = worldTime.getDay();
                if (hours >= 24) {
                    days += hours / 24;
                    hours = hours % 24;
                }
                return new LocalTime(days, hours, worldTime.getMinute());
            }

            @Override
            public Time toWorld(LocalTime local) {
                int hours = local.getHours() - 2;
                int days = local.getDays();
                if (hours < 0) {
                    hours += 24;
                    days--;
                }
                return new Time(days, hours, local.getMinutes());
            }

            @Override
            public String describe() {
                return "Shadowfell time (UTC+2)";
            }
        };

        LocalTimeRule escortRule = new LocalTimeRule() {
            @Override
            public LocalTime toLocal(Time worldTime) {
                int hours = worldTime.getHour() + 4;
                int days = worldTime.getDay();
                if (hours >= 24) {
                    days += hours / 24;
                    hours = hours % 24;
                }
                return new LocalTime(days, hours, worldTime.getMinute());
            }

            @Override
            public Time toWorld(LocalTime local) {
                int hours = local.getHours() - 4;
                int days = local.getDays();
                if (hours < 0) {
                    hours += 24;
                    days--;
                }
                return new Time(days, hours, local.getMinutes());
            }

            @Override
            public String describe() {
                return "Verdant Crossroads time (UTC+4)";
            }
        };

        switch (adventureIndex) {
            case 0:
                return new Realm("Shadowfell Catacombs",
                        "Ancient underground tunnels haunted by restless spirits.", raidRule);
            case 1:
                return new Realm("Verdant Crossroads",
                        "A sprawling trade route through ancient forests.", escortRule);
            default:
                return new Realm("Unknown Realm", "A placeholder realm.", raidRule);
        }
    }

    private static void saveOutcome(GameFacade facade, MiniAdventureContext context, PlayerProfile profile1,
            PlayerProfile profile2) {
        if (context == null) {
            System.out.println("Error: No adventure context available.");
            return;
        }

        Time worldTime = WorldClock.getInstance().getTime();
        AdventureRecord record = new AdventureRecord(
                context.getName(),
                context.isVictory(),
                context.getRealm().getName(),
                worldTime);
        facade.recordAdventure(profile1, record);
        facade.recordAdventure(profile2, record);
        System.out.println("Adventure recorded to both profiles.");
    }
}