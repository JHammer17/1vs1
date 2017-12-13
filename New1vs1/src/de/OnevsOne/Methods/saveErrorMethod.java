package de.OnevsOne.Methods;

import de.OnevsOne.States.AllErrors;

/**
 * Der Code ist von JHammer
 *
 * 05.05.2016 um 21:25:46 Uhr
 */
public class saveErrorMethod {

	
	public static void saveError(AllErrors error, String Class, String infos) {
		System.out.println("Ein Error ist aufgetreten: " + error.getError());
		System.out.println("Fehler in Klasse: " + Class);
		System.out.println("Weitere Infos: " + infos);
	}
	
}
