package de.OnevsOne.Methods;

import org.bukkit.entity.Player;

import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.Messenger.TitleAPI;

public class moneyTitles {

	
	public static void send(Player players, main plugin) {
		
		if(plugin.economy == null) return;
		if(plugin.useEconomy == false) return;
		
		if(players.hasPermission("1vs1.Team.*") || players.hasPermission("1vs1.Admin")) {
			if(plugin.economyNormalWinAdmin <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyNormalWinAdmin);
			if(plugin.economyTournamnetWinAdmin == 1) {																// plugin.economyNormalWinAdmin  plugin.economy.currencyNameSingular() plugin.economy.currencyNamePlural()
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinAdmin).replaceAll("%Name%", plugin.economy.currencyNameSingular()))); 
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinAdmin).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else if(players.hasPermission("1vs1.Team.Special")) {
			if(plugin.economyNormalWinSpecial <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyNormalWinSpecial);
			if(plugin.economyTournamnetWinSpecial == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinSpecial).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinSpecial).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else if(players.hasPermission("1vs1.Team.Premium")  || players.hasPermission("1vs1.Premium")) {
			if(plugin.economyNormalWinPremium <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyNormalWinPremium);
			if(plugin.economyTournamnetWinPremium == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinPremium).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinPremium).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else {
			if(plugin.economyNormalWinUser <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyNormalWinUser);
			if(plugin.economyNormalWinUser == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinUser).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyNormalWinUser).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} 
	}
	
	public static void sendTournamentKill(Player players, main plugin) {
		
		if(plugin.economy == null) return;
		if(plugin.useEconomy == false) return;
		
		if(players.hasPermission("1vs1.Team.*") || players.hasPermission("1vs1.Admin")) {
			if(plugin.economyTournamnetKillAdmin <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyTournamnetKillAdmin);
			if(plugin.economyTournamnetKillAdmin == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillAdmin).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillAdmin).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else if(players.hasPermission("1vs1.Team.Special")) {
			if(plugin.economyTournamnetKillSpecial <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyTournamnetKillSpecial);
			if(plugin.economyTournamnetKillSpecial == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillSpecial).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillSpecial).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else if(players.hasPermission("1vs1.Team.Premium")  || players.hasPermission("1vs1.Premium")) {
			if(plugin.economyTournamnetKillPremium <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyTournamnetKillPremium);
			if(plugin.economyTournamnetKillPremium == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillPremium).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillPremium).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else {
			if(plugin.economyTournamnetKillUser <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyTournamnetKillUser);
			if(plugin.economyTournamnetKillUser == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillUser).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetKillUser).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} 
	}
	
	public static void sendTournament(Player players, main plugin) {
		if(plugin.economy == null) return;
		if(plugin.useEconomy == false) return;
		
		
		if(players.hasPermission("1vs1.Team.*") || players.hasPermission("1vs1.Admin")) {
			if(plugin.economyTournamnetWinAdmin <= 0) return;
			plugin.economy.depositPlayer(players, plugin.economyTournamnetWinAdmin);
			if(plugin.economyTournamnetWinAdmin == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinAdmin).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinAdmin).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else if(players.hasPermission("1vs1.Team.Special")) {
			if(plugin.economyTournamnetWinSpecial <= 0) return;
			
			plugin.economy.depositPlayer(players, plugin.economyTournamnetWinSpecial);
			if(plugin.economyTournamnetWinSpecial == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinSpecial).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinSpecial).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else if(players.hasPermission("1vs1.Team.Premium")  || players.hasPermission("1vs1.Premium")) {
			if(plugin.economyTournamnetWinPremium <= 0) return;
			
			plugin.economy.depositPlayer(players, plugin.economyTournamnetWinPremium);
			if(plugin.economyTournamnetWinPremium == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinPremium).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinPremium).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} else {
			
			if(plugin.economyTournamnetWinUser <= 0) return;
			
			plugin.economy.depositPlayer(players, plugin.economyTournamnetWinUser);
			if(plugin.economyTournamnetWinSpecial == 1) {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinUser).replaceAll("%Name%", plugin.economy.currencyNameSingular())));
			} else {
				TitleAPI.sendTitle(players, 20, 20*2, 20, MessageReplacer.replaceStrings(plugin.msgs.getMsg("economyTitle")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("economySubTitle").replaceAll("%Amount%", "" + plugin.economyTournamnetWinUser).replaceAll("%Name%", plugin.economy.currencyNamePlural())));
			}
			
		} 
	}
}
