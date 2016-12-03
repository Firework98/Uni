import java.io.*;
import java.nio.file.*;

/**
*Diese Klasse stellt alle noetigen Methoden um ein Spielfeld zu erstellen und darzustellen.
*Sowie die Moeglichkeit Objekte auf Kollisionen zu ueberpruefen. Des Weiteren
*ermoeglicht sie die Synchronisation der Koordinaten des Spielers mit der Position des P's im Spielfeld.
*Außerdem ist eine Methode enthalten, die die Bewegung des Spielers im Feld ermoeglicht und die Bewegung von Kisten.
*@author Diyar Polat 4763428 Gruppe 3b
*@author Thorben Sengpiel 4773337 Gruppe 3b
*/


public class Map {
	private char[][] room;
	private final int X_LENGTH, Y_LENGTH; //Laenge unter Berücksichtigung von Arraystart bei 0
 	private Player player;
 	private int playerCount = 0;
 	private int chestCount = 0;
 	private int goalCount = 0;
 	private int chestsOnGoal = 0;

	/**
	* Dieser Konstruktor, erstellt ein Map-Objekt aus einer
	* Datei. Dabei wird die Map automatisch mit Waenden auf eine
	* rechteckige Form gebracht.
	* @param file Pfad zu der Datei aus der gelesen werden soll.
	*/
	Map(String file) {
		int totalY = 0;
		int x = 0;
		try {
			Path path = Paths.get(file);
			BufferedReader reader = Files.newBufferedReader(path);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() > x) {
					x=line.length();
				}
				totalY++;
			}
			reader.close();
			
			reader = Files.newBufferedReader(Paths.get(file));
			line = null;
			room = new char[x][totalY];
			int y = 0;
			while (y<totalY) {
				while ((line = reader.readLine()) != null) { // Zeile nicht leer
					for (int i = 0; i <= line.length()-1; i++) { // Iteration ueber x-Koordinate
						room[i][y] = line.charAt(i);
						
						if (line.charAt(i) == '@') { //Erzeugen des Spieler-objekts
							player = new Player(i , y);
							playerCount++;
						}
						if (line.charAt(i) == '$') { //Anzahl der Kisten im Feld zaehlen
							chestCount++;
						}
						if (line.charAt(i) == '.') {
							goalCount++;
						}
						if (line.charAt(i) == '*') { //Kistenanzahl, Zielanzahl und Kistenanzahl auf Ziel um eins erhoehen
							chestCount++;
							goalCount++;
							chestsOnGoal ++;
						}
						
						if (line.length()-1 < x) { //Fuellen mit '#'
							for (int z= line.length();z <= x-1;z++) {
								room[z][y]='#';
							}
						}						
					
						
					}
					y++;
				}
			} 
		}
		catch (IOException e) {

		}
		// Erst hier Initialisierung aufgrund von try-catch. 
		Y_LENGTH = totalY-1;
		X_LENGTH = x-1;

		
		
	}

	/**
	*Gibt das Spielfeld auf der Konsole aus
	*/

	public void show() {
		update();
		for (int i = 0; i <= Y_LENGTH; i++) {
			System.out.println();
			for (int z = 0; z <= X_LENGTH; z++) {
				System.out.print(room[z][i]);

			}
		}
		System.out.println();
	}



	/**
	* Ermoeglicht das setzen eines beliebigen Symbols, an einer
	* beliebigen Stelle auf der Map.
	* @param x x-Koordinate des Punktes
	* @param y y-Koordinate des Punktes
	* @param symbol Zu Setzendes Symbol
	*/
	private void set(int x, int y, char symbol) {
		room[x][y] = symbol;
	}

	/**
	*Synchronisiert die Position des Spielers mit der Position des P auf dem Spielfeld. 
	*Beachtet dabei auch moegliche Zielpositionen an der alten bzw. neuen Position des Spielers
	*und waehlt die zu setzenden Symbole dementsprechend.
	*/
	private void update() {
		int[] past = new int[2];
		int[] present = new int[2];

		past = player.getPastCoords();
		if (room[(past[0])][(past[1])] == '@') {
			room[(past[0])][(past[1])] = ' ';
		}
		else {
			room[(past[0])][(past[1])] = '.';
		}
		present = player.getNowCoords();		
		if (room[(present[0])][(present[1])] == '.'){
			room[(present[0])][(present[1])] = '+';
		}
		else {
			room[(present[0])][(present[1])] = '@';
		}
	}

	/**
	*Ueberprueft ob eine Koordinate innerhalb des Spielfelds liegt
	*@param x x-Koordinate
	*@param y y-Koordinate
	*@return boolschen Wert(false=>liegt im Spielfeld,true=>liegt außerhalb des Feldes)
	*/
	private boolean checkBorder(int x, int y) {
		boolean outOfBorder = false;
		
	
		if ((y > Y_LENGTH) || (y < 0) || (x > X_LENGTH) || (x < 0)) {
			outOfBorder = true;
		}
		
		
		return outOfBorder;
	}

	/**
	* Ueberprueft ob sich an der Angegeben Position eine Wand befindet
	* @param x x-Koordinate
	* @param y y-Koordinate
	* @return boolscher Wert(true -> An der Position befindet sich eine Wand, false -> keine Wand)
	*/

	private boolean checkWall(int x, int y) {
		boolean collision = false;
		if (room[x][y] == '#') {
			collision = true;
		}
		return collision;
	}

	/**
	* Ueberprueft ob sich an der Angegeben Position eine Kiste befindet
	* @param x x-Koordinate
	* @param y y-Koordinate
	* @return boolscher Wert(true -> An der Position befindet sich eine Kiste, false -> keine Kiste)
	*/

	private boolean checkChest(int x, int y) { 
		boolean collision = false;
		if ((room[x][y] == '$') || (room[x][y] == '*')) {
			collision = true;
		}
		return collision;
	}
	/** 
	* Ueberprueft, ob das Raetsel nur unter Betrachtung der Anzahl der Kisten und der Anzahl der
	* Ziele, loesbar ist.
	* @return boolscher Wert(true -> loesbar, false -> nicht loesbar)
	*/

	public boolean isSolveable() {
		boolean solveable = false;
		if (chestCount == goalCount) {
			solveable = true;
		}

		return solveable;
	}

	/**
	* Ermoeglicht das Bewegen einer Kiste an gegebener Position, um eine uebergebene Distanz.
	* Ueberprueft dabei ob eine Bewegung moeglich ist und ob eine Kiste an der gegeben Position existiert.
	* Sorgt dafuer, dass die Bewegung im Feld abgespeichert wird.
	* @param x x-Koordinate der Position an der sich die zu bewegende Kiste befindet 
	* @param y y-Koordinate der Position an der sich die zu bewegende Kiste befindet
	* @param xOff Wert um den die Kiste in x-Richtung verschoben werden soll(-1->links,+1->rechts) 
	* @param yOff Wert um den die Kiste in y-Richtung verschoben werden soll(-1->oben,+1->unten)
	* @return boolscher Wert (true->wurde bewegt,false->wurde nicht bewegt) 
	*/

	private boolean moveChest(int x, int y, int xOff, int yOff) {
		boolean moved = false;
		char pastSymbol= ' ';
		char toSetSymbol= ' ';
		int goalDiff = 0;
		
		if (room[x][y]== '$') {
			pastSymbol = ' ';

		}
		if (room[x][y]=='*') {
			pastSymbol = '.';
			goalDiff -= 1;

		}
					
					
		if (checkChest(x, y)) {
			if (checkChest(x+xOff, y+yOff) || checkWall(x+xOff, y+yOff) || checkBorder(x+xOff, y+yOff)) {
				moved=false;
			}
			else {
				if (room[x+xOff][y+yOff] == ' ') {
					toSetSymbol = '$';
				} 
				if (room[x+xOff][y+yOff] == '.') {
					toSetSymbol = '*';
					goalDiff+=1;
				} 
				set(x, y, pastSymbol);
				set(x+xOff, y+yOff, toSetSymbol);
				moved = true;
			}
		}	
		chestsOnGoal+=goalDiff;

		return moved;
	}
	
	/**
	* Ueberprueft ob jeder Kiste auf einem Zielfeld steht.
	* @return boolscher Wert (true -> jede Kiste steht auf einem Zielfeld, false -> mindestens eine Kiste steht nicht auf einem Zielfeld)
	*/

	public boolean isVictory() {
		boolean victory = false;
		if (chestCount == chestsOnGoal) {
			victory =true;
		}

		return victory;
	}

	/**
	*Ermoeglicht einen Spieler im Feld zu bewegen.Ueberprueft dabei ob die Bewegung moeglich ist. 
	*Die Koordinaten des Spielers werden nur bei erfolgreich durchgefuehrter Bewegung veraendert.
	*Bei nicht durchgefuehrter Bewegung werden die alten und die neuen Koordinaten des Spielers gleichgesetzt.
	*@param direction Gibt an in welche Richtung der Spieler bewegt
	*werden soll(Erwartet "up","down","left","right"(nicht case-sensitive))
	*@return boolscher Wert, gibt zurueck ob die Bewegung moeglich war.(true=> Bewegung 
	*erfolgreich,false=> Bewegung konnte nicht durchgefuehrt werden)
	*/
	
	public boolean move(String direction) {
		int[] oldCoords = new int[2];
		oldCoords = player.getNowCoords();
		int newX = oldCoords[0];
		int newY = oldCoords[1];
		int xOff = 0;
		int yOff = 0;
		
		
		boolean moved = false;
		switch (direction.toUpperCase()) {
			case "W":
			case "UP":		yOff = -1;
							break;
			case "S":
			case "DOWN":	yOff = 1;
							break;
			case "D":
			case "RIGHT":   xOff = 1;
							break;
			case "A":
			case "LEFT": 	xOff = -1;
							break;
			default: 
		}
		newX += xOff;
		newY += yOff;
		if (checkBorder(newX, newY)) {
			moved = false;
		}
		else{
			if (checkChest(newX, newY)) {
				System.out.println("Chest");
				if (!(checkBorder(newX + xOff, newY + yOff))) {
					if (checkChest(newX + xOff, newY + yOff) || checkWall(newX+xOff, newY+yOff)) {
						player.pushCoords();

					} 
					else {
						moveChest(newX, newY, xOff, yOff);
						player.setCoords(newX, newY);
					}
				}

			}
			else {
				
					if (checkWall(newX, newY)) {
						System.out.println("Kollision");
						moved = false;
						player.pushCoords();
					} 
					else {
						System.out.println("Move");
						player.setCoords(newX, newY);
						moved = true;
					}
		}
		}

		

		
		return moved;
	}

	
	
}
