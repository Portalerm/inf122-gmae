import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		GameFacade facade = new GameFacade();
		Scanner scanner = new Scanner(System.in);

		System.out.println("========================================");
		System.out.println("        Welcome to GuildQuest!");
		System.out.println("========================================");

		PlayerProfile profile1 = selectOrCreateProfile(facade, scanner, 1);
		PlayerProfile profile2 = selectOrCreateProfile(facade, scanner, 2);

		boolean running = true;
		while (running) {
			System.out.println("\nMain Menu:");
			System.out.println("  1) Launch Timed Raid");
			System.out.println("  2) Launch Escort Across the Realm");
			System.out.println("  3) View My Profile (Player 1)");
			System.out.println("  4) View My Profile (Player 2)");
			System.out.println("  5) Quit");
			System.out.print("Choice: ");
			String choice = scanner.nextLine().trim();

			switch (choice) {
				case "1":
					launchTimedRaid(facade, scanner, profile1, profile2);
					break;
				case "2":
					launchEscort(facade, scanner, profile1, profile2);
					break;
				case "3":
					profile1.displayProfile();
					break;
				case "4":
					profile2.displayProfile();
					break;
				case "5":
					running = false;
					System.out.println("Goodbye!");
					break;
				default:
					System.out.println("Invalid choice.");
			}
		}
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
		if (profile != null) {
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
		char classChoice = scanner.nextLine().trim().toLowerCase().charAt(0);
		facade.createCharacterForProfile(profile, profile.getName(), classChoice);
		Character c = profile.getActiveCharacter();
		System.out.println("Created: " + c.getName() + " the " + c.getClassType());
	}

	private static Realm buildTimedRaidRealm() {
		LocalTimeRule rule = new LocalTimeRule() {
			@Override
			public LocalTime toLocal(Time worldTime) {
				int hours = worldTime.getHour() + 2;
				int days = worldTime.getDay();
				if (hours >= 24) { days += hours / 24; hours = hours % 24; }
				return new LocalTime(days, hours, worldTime.getMinute());
			}
			@Override
			public Time toWorld(LocalTime local) {
				int hours = local.getHours() - 2;
				int days = local.getDays();
				if (hours < 0) { hours += 24; days--; }
				return new Time(days, hours, local.getMinutes());
			}
			@Override
			public String describe() { return "Shadowfell time (UTC+2)"; }
		};
		return new Realm("Shadowfell Catacombs",
				"Ancient underground tunnels haunted by restless spirits.", rule);
	}

	private static Realm buildEscortRealm() {
		LocalTimeRule rule = new LocalTimeRule() {
			@Override
			public LocalTime toLocal(Time worldTime) {
				int hours = worldTime.getHour() + 4;
				int days = worldTime.getDay();
				if (hours >= 24) { days += hours / 24; hours = hours % 24; }
				return new LocalTime(days, hours, worldTime.getMinute());
			}
			@Override
			public Time toWorld(LocalTime local) {
				int hours = local.getHours() - 4;
				int days = local.getDays();
				if (hours < 0) { hours += 24; days--; }
				return new Time(days, hours, local.getMinutes());
			}
			@Override
			public String describe() { return "Verdant Crossroads time (UTC+4)"; }
		};
		return new Realm("Verdant Crossroads",
				"A sprawling trade route through ancient forests.", rule);
	}

	private static void saveOutcome(GameFacade facade, MiniAdventureContext context, PlayerProfile profile1, PlayerProfile profile2) {
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

	private static void launchTimedRaid(GameFacade facade, Scanner scanner,
			PlayerProfile profile1, PlayerProfile profile2) {
		Character p1 = profile1.getActiveCharacter();
		Character p2 = profile2.getActiveCharacter();
		Realm realm = buildTimedRaidRealm();

		MiniAdventure raid = new MiniAdventure(new TimedRaidAdventureContext());
		MiniAdventure adventure = facade.launchMiniAdventure(raid, p1, p2, realm);
		adventure.run(scanner);

		saveOutcome(facade, raid.getContext(), profile1, profile2);
	}

	private static void launchEscort(GameFacade facade, Scanner scanner,
			PlayerProfile profile1, PlayerProfile profile2) {
		Character p1 = profile1.getActiveCharacter();
		Character p2 = profile2.getActiveCharacter();
		Realm realm = buildEscortRealm();

		MiniAdventure escort = new MiniAdventure(new EscortAdventureContext());
		MiniAdventure adventure = facade.launchMiniAdventure(escort, p1, p2, realm);
		adventure.run(scanner);

		saveOutcome(facade, escort.getContext(), profile1, profile2);
	}
}