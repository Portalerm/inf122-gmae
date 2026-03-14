import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		GameFacade facade = new GameFacade();
		Scanner scanner = new Scanner(System.in);

		System.out.println("========================================");
		System.out.println("        Welcome to GuildQuest!");
		System.out.println("========================================");

		boolean running = true;
		while (running) {
			System.out.println("\nMain Menu:");
			System.out.println("  1) Launch Timed Raid");
            System.out.println("  2) Launch Escort Across the Realm");
			System.out.println("  3) Quit");
			System.out.print("Choice: ");
			String choice = scanner.nextLine().trim();

			switch (choice) {
				case "1":
					launchTimedRaid(facade, scanner);
					break;
                case "2":
                    launchEscort(facade, scanner);
                    break;
				case "3":
					running = false;
					System.out.println("Goodbye!");
					break;
				default:
					System.out.println("Invalid choice.");
			}
		}
	}

	private static void launchTimedRaid(GameFacade facade, Scanner scanner) {
		System.out.println("\n--- Character Creation ---");

		System.out.print("Player 1 name: ");
		String name1 = scanner.nextLine().trim();
		System.out.println("Classes: (w)arrior, (m)age, (a)rcher, (r)ogue, m(o)nk");
		System.out.print("Player 1 class: ");
		char class1 = scanner.nextLine().trim().toLowerCase().charAt(0);
		facade.createCharacter(name1, class1);
		Character p1 = facade.getCharacters().get(facade.getCharacters().size() - 1);
		System.out.println("Created: " + p1.getName() + " the " + p1.getClassType());

		System.out.print("\nPlayer 2 name: ");
		String name2 = scanner.nextLine().trim();
		System.out.println("Classes: (w)arrior, (m)age, (a)rcher, (r)ogue, m(o)nk");
		System.out.print("Player 2 class: ");
		char class2 = scanner.nextLine().trim().toLowerCase().charAt(0);
		facade.createCharacter(name2, class2);
		Character p2 = facade.getCharacters().get(facade.getCharacters().size() - 1);
		System.out.println("Created: " + p2.getName() + " the " + p2.getClassType());

		LocalTimeRule realmTimeRule = new LocalTimeRule() {
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

		Realm realm = new Realm("Shadowfell Catacombs",
			"Ancient underground tunnels haunted by restless spirits.", realmTimeRule);

		TimedRaidAdventure raid = new TimedRaidAdventure();
		MiniAdventure adventure = facade.launchMiniAdventure(raid, p1, p2, realm);
		adventure.run(scanner);
	}

    private static void launchEscort(GameFacade facade, Scanner scanner) {
        System.out.println("\n--- Character Creation ---");

        System.out.print("Player 1 name: ");
        String name1 = scanner.nextLine().trim();
        System.out.println("Classes: (w)arrior, (m)age, (a)rcher, (r)ogue, m(o)nk");
        System.out.print("Player 1 class: ");
        char class1 = scanner.nextLine().trim().toLowerCase().charAt(0);
        facade.createCharacter(name1, class1);
        Character p1 = facade.getCharacters().get(facade.getCharacters().size() - 1);
        System.out.println("Created: " + p1.getName() + " the " + p1.getClassType());

        System.out.print("\nPlayer 2 name: ");
        String name2 = scanner.nextLine().trim();
        System.out.println("Classes: (w)arrior, (m)age, (a)rcher, (r)ogue, m(o)nk");
        System.out.print("Player 2 class: ");
        char class2 = scanner.nextLine().trim().toLowerCase().charAt(0);
        facade.createCharacter(name2, class2);
        Character p2 = facade.getCharacters().get(facade.getCharacters().size() - 1);
        System.out.println("Created: " + p2.getName() + " the " + p2.getClassType());

        LocalTimeRule realmTimeRule = new LocalTimeRule() {
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
                return "Shadowfell time (UTC+2)";
            }
        };

        Realm realm = new Realm("Shadowfell Catacombs",
                "Ancient underground tunnels haunted by restless spirits.", realmTimeRule);

        EscortAdventure escort = new EscortAdventure();
        MiniAdventure adventure = facade.launchMiniAdventure(escort, p1, p2, realm);
        adventure.run(scanner);
    }
}
