/**
*Diese Klasse ermoeglicht das Erstellen eines Spieler-objektes, welches die Koordinaten des Spieler buendelt und
*Getter/Setter-Methoden zum Zugriff auf die Attribute bietet.
*@author Diyar Polat 4763428 Gruppe 3b
*@author Thorben Sengpiel 4773337 Gruppe 3b
*/

public class Player {
	private int xPast;
	private int yPast;

	private int xNow;
	private int yNow;
	/**
	*Mit diesem Konstruktor ist es moeglich ein Spielerobjekt zu erstellen, welches mit der Position initialisiert wird
	*@param xCoord x-Koordinate des Spielers
	*@param yCoord y-Koordinate des Spielers
	*/
	Player(int xCoord, int yCoord) {
		xPast = xCoord;
		yPast = yCoord;

		xNow = xCoord;
		yNow = yCoord;
	}
	/**
	*Diese Methode gibt die aktuelle Position des Spielers aus
	*@return gibt die Position in Form eines int[2] wieder.( [0]=x-Koordinate,[1]=y-Koordinate)
	*/
	public int[] getNowCoords() { 
		int[] buffer = new int[2];
		buffer[0] = xNow;
		buffer[1] = yNow;
		return buffer;
	}
	/**
	*Diese Methode gibt die Position des Spielers aus, an der er sich vor der letzten Bewegung befand.
	*@return gibt die Position in Form eines int[2] wieder.( [0]=x-Koordinate,[1]=y-Koordinate)
	*/
	public int[] getPastCoords() {
		
		int[] buffer = new int[2];
		buffer[0] = xPast;
		buffer[1] = yPast;
		return buffer;
	}
	/**
	*Diese Metode ermoeglicht es die Position des Spielers zu veraendern.
	*@param xCoord : x-Koordinate die der Spieler erhaelt
	*@param yCoord : y-Koordinate die der Spieler erhaelt
	*/
	public void setCoords(int xCoord, int yCoord) {
		xPast = xNow;
		yPast = yNow;

		xNow = xCoord;
		yNow = yCoord;

	}

	/**
	* Setzt die alten Koordinaten gleich den neuen Koordinaten.
	*/
	
	public void pushCoords() {
		xPast = xNow;
		yPast = yNow;
	}


}
