public class CharacterFactory {
	public static Character createCharacter(String name, char classChoice) {
		String classType = "";
		switch (classChoice) {
		case 'w':
			classType = "Warrior";
			break;
		case 'm':
			classType = "Mage";
			break;
		case 'a':
			classType = "Archer";
			break;
		case 'r':
			classType = "Rogue";
			break;
		case 'o':
			classType = "Monk";
			break;
		}
		return new Character(name, classType);
	}
}
