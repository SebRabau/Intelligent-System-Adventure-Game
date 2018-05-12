import java.util.concurrent.ThreadLocalRandom;

public class main {
	static String Starter;
	static boolean player;

	public static void main(String Args[]) {	

		System.out.println("Thanks for joining us adventurer! Type START to begin!");
		ready();		
	}

	private static void ready() {
		Starter = BIO.getString();
		check();
	}

	private static void check() {
		if(Starter.toLowerCase().equals("start")) {
			start();
		} else {
			System.out.println("Invalid Selection!");
			ready();
		}
	}

	private static void start() {
		player = true;
		int round = 1;	
		int choice = 0;
		int stats[] = {4, 3, 2}; //Intelligence - Speed - Weapon
		String inv[] = {"Textbook", "Jetpack", "Flashgun"};		
		int monster[]; //Intelligence - Speed - Weapon - Behaviour(1: Scared, 2:Curious, 3:Aggressive)
		int nkilled = 0;
		int nhypnotised = 0;
		int evaded = 0;

		System.out.println("Your current stats: Intelligence "+stats[0]+", Speed "+stats[1]+", Weapon "+stats[2]);
		System.out.println("Your current inventory: "+inv[0]+", "+inv[1]+", "+inv[2]);

		while(player) {
			int statsP[] = {4, 3, 2}; //stat reset
			boolean I = false;
			boolean S = false;
			boolean W = false;

			System.out.println("------------------------------------------------------------------");
			System.out.println("Round: "+round);
			System.out.println("------------------------------------------------------------------");

			monster = spawn();
			System.out.println("A new monster has appeared!");
			System.out.println("It has "+checkWeapon(monster[2])+". It looks "+checkSpeed(monster[1])+" and "+checkIntelligence(monster[0])+". It acts in a"+checkBehaviour(monster[3])+" manner.");

			System.out.println("Stat differences: Intelligence "+compare(statsP[0], monster[0])+", Speed "+compare(statsP[1], monster[1])+", Weapon "+compare(statsP[2], monster[2]));

			if(monster[3] == 1) { //If monster scared, ignore choice selection
				choice = 4;
				System.out.println("The monster looks scared! You do nothing this turn to see if it runs away");
			} else {
				while(player) {	
					int diffI = compare(statsP[0], monster[0]);
					int diffS = compare(statsP[1], monster[1]);
					int diffW = compare(statsP[2], monster[2]);
					
					if(diffI >= 2) { //Can you hypnotise?
						choice = 1;
						break;
					}

					if(diffS >= 0) { //if faster
						choice = 2;
						break;
					}

					if(diffI == 0) { //Find tie-breakers
						choice = 1;
						break;
					}
					if(diffW == 0) {
						choice = 3;
						break;
					}

					if(diffI == 1) { //Find 50% chance (diff 1)
						choice = 1;
						break;
					} 				
					if(diffW == 1) {
						choice = 3;
						break;
					}

					if(diffW + 2 >= 0 && inv[2] != null) { //Use items
						statsP[2] += 2;
						choice = 3;
						inv[2] = (String) null;
						W = true;
						break;
					}
					if(diffS + 2 >= 0 && inv[1] != null) {
						statsP[1] += 2;
						choice = 2;
						inv[1] = (String) null;
						S = true;
						break;
					}
					if(diffI + 2 >= 0 && inv[0] != null) {
						statsP[0] += 2;
						choice = 1;
						inv[0] = (String) null;
						I = true;
						break;
					}
					choice = 4;
					break;
				}				
			}
			
			if(choice == 1) {
				System.out.println("You have chosen to Hypnotise the enemy!");
				if(I) {
					System.out.println("You use your Textbook to increase your Intelligence temporarily by 2");
				}
				int killed = compare(statsP[0], monster[0]);
				if(killed < 0) {
					System.out.println("Oh no! The monster was smarter than you! You have failed this time adventurer...");
					player = false;
				} else if(killed == 0) {
					System.out.println("It's a stalemate! The monster runs away in fear");
					evaded++;
				} else if(killed == 1) {
					int rand = ThreadLocalRandom.current().nextInt(0, 100 + 1); //generate number between 0-100. +50% = success
					if(rand >= 50) {
						System.out.println("It was a tough battle but you managed to hypnotise the enemy!");
						nhypnotised++;
					} else {
						System.out.println("You tried your best, but luck wasn't on your side. You were hypnotised by the enemy and eaten...");
						player = false;
					}
				} else {
					System.out.println("You successfully hypnotised the enemy! You progress through the dungeon");
					nhypnotised++;
				}
			} else if(choice == 2) {
				System.out.println("You have chosen to Flee from the enemy!");
				if(S) {
					System.out.println("You use your Jetpack to increase your Speed temporarily by 2");
				}
				int killed = compare(statsP[1], monster[1]);
				if(killed < 0) {
					System.out.println("Oh no! The monster was quicker than you! You have failed this time adventurer...");
					player = false;
				} else if(killed == 0) {
					System.out.println("It's close, but you manage to escape the enemy just in time!");
					evaded++;
				} else {
					System.out.println("You successfully outrun the enemy! You progress through the dungeon");
					evaded++;
				}
			} else if(choice == 3) {
				System.out.println("You have chosen to Fight the enemy!");
				if(W) {
					System.out.println("You use your Flashgun to increase your Weapon temporarily by 2");
				}
				int killed = compare(statsP[2], monster[2]);
				if(killed < 0) {
					System.out.println("Oh no! The monster was stronger than you! You have failed this time adventurer...");
					player = false;
				} else if(killed == 0) {
					System.out.println("It's a stalemate! The monster runs away in fear");
					evaded++;
				} else if(killed == 1) {
					int rand = ThreadLocalRandom.current().nextInt(0, 100 + 1); //generate number between 0-100. +50% = success
					if(rand >= 50) {
						System.out.println("It was a tough battle but you managed to kill the enemy!");
						nkilled++;
					} else {
						System.out.println("You tried your best, but luck wasn't on your side. You were killed by the enemy...");
						player = false;
					}
				} else {
					System.out.println("You successfully kill the enemy! You progress through the dungeon");
					nkilled++;
				}
			} else {
				System.out.println("You have chosen to do nothing");
				if(monster[3] == 1) { //Monster was scared
					System.out.println("You were right, the monster ran away!");
					evaded++;
				} else if(monster[3] == 2) { //Monster was curious
					System.out.println("You engage in an intense staring contest with the monster. It lasts so long that you feint and the monster eats you...");
					player = false;
				} else {
					System.out.println("The monster was aggressive and challenges you to combat!");
					int killed = compare(statsP[2], monster[2]);
					if(killed < 0) {
						System.out.println("Oh no! The monster was stronger than you! You have failed this time adventurer...");
						player = false;
					} else {
						System.out.println("Somehow you survived? How about the next one!");
					}
				}
			}
			round++;
		}
		System.out.println("------------------------------------------------------------------");
		System.out.println("Valiant effort young adventurer! You survived "+(round-1)+" rounds! Here are your stats from this dungeon:");
		System.out.println("Monsters Killed: "+nkilled+", Monsters Hypnotised: "+nhypnotised+", Monsters Evaded: "+evaded);
		System.out.println("------------------------------------------------------------------");
		
		System.out.println("If you'd like to run another dungeon, type START");
		ready();
	}

	private static int[] spawn() {
		int monster[] = {ThreadLocalRandom.current().nextInt(1, 5 + 1), ThreadLocalRandom.current().nextInt(1, 5 + 1), ThreadLocalRandom.current().nextInt(1, 5 + 1), ThreadLocalRandom.current().nextInt(1, 3 + 1)};
		return monster;
	}

	private static String checkIntelligence(int a) {
		if(a == 1) {
			return "very dumb";
		} else if(a == 2) {
			return "dumb";
		} else if(a == 3) {
			return "fairly smart";
		} else if(a == 4) {
			return "smart";
		} else {
			return "really smart";
		}
	}
	
	private static String checkSpeed(int a) {
		if(a == 1) {
			return "very slow";
		} else if(a == 2) {
			return "slow";
		} else if(a == 3) {
			return "fairly fast";
		} else if(a == 4) {
			return "fast";
		} else {
			return "lightning quick";
		}
	}
	
	private static String checkWeapon(int a) {
		String tweapon;
		int weapon = ThreadLocalRandom.current().nextInt(1, 3 + 1);
		if(weapon == 1) {
			tweapon = " teeth";
		} else if(weapon == 2) {
			tweapon = " claws";
		} else {
			tweapon = " rocks";
		}
		
		if(a == 1) {
			return "very blunt" + tweapon;
		} else if(a == 2) {
			return "blunt" + tweapon;
		} else if(a == 3) {
			return "fairly sharp" + tweapon;
		} else if(a == 4) {
			return "sharp" + tweapon;
		} else {
			return "razor sharp" + tweapon;
		}
	}
	
	private static String checkBehaviour(int a) {
		if(a == 1) {
			return " Scared";
		} else if(a == 2) {
			return " Curious";
		} else {
			return "n Aggressive";
		}
	}

	private static int compare(int a, int b) {
		return a - b;
	}
}
