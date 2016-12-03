import java.util.Scanner;
/**
*Diese Klasse regelt den Ablauf des Spiels, sowie die Eingaben des Spielers.
*@author Diyar Polat 4763428 Gruppe 3b
*@author Thorben Sengpiel 4773337 Gruppe 3b
*/

public class Sokoban {
	
	public static void main(String[] args) {
		String file;

		if (args.length == 0){ //Wurde kein Param uebergeben
			file = "Sokoban.txt";
		}
		else {
			file = args[0]; //Nutze erstes Param
		}
		Map level1 = new Map(file);

		Scanner scr = new Scanner(System.in);
		boolean exit = false;
		while (exit == false) {
			if (level1.isSolveable()) {	//Nur wenn loesbar			
				level1.show();
				if (level1.isVictory() == true) { //Ueberpruefe auf Sieg
					System.out.println(" Sie haben gewonnen !!!!");
					exit = true;
				}
				else {				
					System.out.print("Direction(Up(w), Down(s), Left(a), Right(d) or Exit): ");
					String input = scr.next();
					level1.move(input);

					if (input.toUpperCase().equals("EXIT")) {
						exit = true;
					}
				}
				
			}
			else {
				exit = true;
				System.out.println(" Das Level ist nicht loesbar !");
			}
		}
	}
}
