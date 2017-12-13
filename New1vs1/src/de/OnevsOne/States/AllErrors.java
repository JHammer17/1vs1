package de.OnevsOne.States;


/**
 * Der Code ist von JHammer
 *
 * 05.05.2016 um 21:29:53 Uhr
 */
public enum AllErrors {

	
	
	MainFileFail("Standard-File konnte nicht erstellt werden!"),
	FileFail("Die File konnte auf diesen Pfad nicht erstellt werden!"),
	World("Die angegebene Welt konnte nicht gefunden werden!");
	
	
	String Errormsg = "";
	
	AllErrors(String Error) {
		this.Errormsg = Error;
	}
	
	public String getError() {
		return this.Errormsg;
	}
}
