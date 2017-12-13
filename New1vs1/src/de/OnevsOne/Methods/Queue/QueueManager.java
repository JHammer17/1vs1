package de.OnevsOne.Methods.Queue;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.ArenaJoin;
import de.OnevsOne.Listener.Manager.Preferences_Manager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.ArenaTeamPlayer;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

public class QueueManager implements Listener {

	private static main plugin;

	@SuppressWarnings("static-access")
	public QueueManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	
	public static void checkQueue(final boolean msg) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				try {
					ArrayList<Player> queue = new ArrayList<>();
					
					for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) 
						if(players.isInQueue()) queue.add(players.getPlayer());
					
					
					int size = queue.size();
					for(int i = 0; i < size; i++) {
					 for(int i2 = 0; i2 < size; i2++) {
					  if(queue.size() > i && queue.size() > i2) {
						  if(!queue.get(i).getUniqueId().toString().equalsIgnoreCase(queue.get(i2).getUniqueId().toString())) {
							   if(comparePlayers(queue.get(i), queue.get(i2))) {
								String map = getRandomMap(queue.get(i), queue.get(i2));
								if(map == null) {
									if(msg) {
									 queue.get(i).sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noFreeArenas")));
									 queue.get(i2).sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noFreeArenas")));
									}
									
									return;
								}
								if(queue.size() > i && queue.size() > i2) {
									String kit = getKit(queue.get(i), queue.get(i2));
									if(kit == null) {
										queue.get(i).sendMessage("§cEin Fehler beim suchen des Kits ist aufgetreten!");
										queue.get(i2).sendMessage("§cEin Fehler beim suchen des Kits ist aufgetreten!");
										//TODO Msgs
										return;
									}
									if(Preferences_Manager.getPref(queue.get(i).getUniqueId(), PlayerPrefs.QUEUE,"")) {				 
									 plugin.getOneVsOnePlayer(queue.get(i)).setWasInQueue(true);
									}
									if(Preferences_Manager.getPref(queue.get(i2).getUniqueId(), PlayerPrefs.QUEUE,"")) {
									 plugin.getOneVsOnePlayer(queue.get(i2)).setWasInQueue(true);
									}
									 
									final Player Fp1 = queue.get(i);
									final Player Fp2 = queue.get(i2);
									final String Fmap = map;
									final String Fkit = kit;
									
									Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
										
										@Override
										public void run() {
											if(plugin.getOneVsOnePlayer(Fp1).getArena() == null && plugin.getOneVsOnePlayer(Fp2).getArena() == null) {
												if(plugin.getOneVsOnePlayer(Fp1).getPlayerTeam() != null) {
													
													ArenaTeamPlayer playerP1 = new ArenaTeamPlayer(plugin.getOneVsOnePlayer(Fp1).getPlayerTeam().getPlayer(), plugin.getOneVsOnePlayer(Fp1).getPlayerTeam().getTeamMates());
											   		ArenaTeamPlayer playerP2 = new ArenaTeamPlayer(plugin.getOneVsOnePlayer(Fp2).getPlayerTeam().getPlayer(), plugin.getOneVsOnePlayer(Fp2).getPlayerTeam().getTeamMates());
													
											   		for(Player mates : playerP1.getTeamMates()) 
											   		 if(plugin.getOneVsOnePlayer(mates).getArena() != null ) return;
											   		
											   		for(Player mates : playerP2.getTeamMates()) 
											   		 if(plugin.getOneVsOnePlayer(mates).getArena() != null ) return;
												   	
												  
											   		for(Player all : playerP1.getAll()) 
											   			plugin.getOneVsOnePlayer(all).setInQueue(false);
											   		for(Player all : playerP2.getAll()) 
											   			plugin.getOneVsOnePlayer(all).setInQueue(false);
											   		
											   		//ArenaJoin.joinArena(playerP1, playerP2, Fmap, true, Fkit, false, "d");
											   		joinA(playerP1, playerP2, Fmap, Fkit);
												} else {
													joinA(Fp1, Fp2, Fmap, Fkit);
												}
											}
											
											
										}
									});
									size = queue.size();
								}
								
								
							   }
							  }
							 }
					  }
					 
					}
				} catch (Exception e) {}
				
				
			}
		});
		
	}
	
	public static void joinGood(Player p1, Player p2, String kit) {
		
			String map = getRandomMap(p1, p2);
			if(map == null) {
				p1.sendMessage("§cEin Fehler beim suchen der Map ist aufgetreten!");
				p2.sendMessage("§cEin Fehler beim suchen der Map ist aufgetreten!");
				return;
			}
			//TODO XXX
			joinA(p1, p2, map, kit);
//			ArenaJoin.joinArena(p1, p2, map, true, kit, false, "d");
		
	}
	
	
	private static boolean comparePlayers(Player p1, Player p2) {
		
		PlayerQuequePrefs prefP1;
		PlayerQuequePrefs prefP2;
		
		
		
		
			if(!plugin.getDBMgr().isConnected()) {
				p1.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				p2.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return false;
			}
			prefP1 = plugin.getDBMgr().getQuequePrefState(p1.getUniqueId());
			prefP2 = plugin.getDBMgr().getQuequePrefState(p2.getUniqueId());
		
		
		if(prefP1 == PlayerQuequePrefs.EnemieKit) {
		 if(prefP2 == PlayerQuequePrefs.EnemieKit || prefP2 == PlayerQuequePrefs.RandomKit) {
		  return false;
		 }	
		}
		
		if(prefP1 == PlayerQuequePrefs.ownKit) {
		 if(prefP2 == PlayerQuequePrefs.ownKit) {
		  return false;
		 }	
		}
	 
	 PlayerBestOfsPrefs Bpref1 = PlayerBestOfsPrefs.BestOf1;
	 PlayerBestOfsPrefs Bpref2 = PlayerBestOfsPrefs.BestOf1;
		
			if(!plugin.getDBMgr().isConnected()) {
				System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return false;
			}
			Bpref1 = plugin.getDBMgr().getQueuePrefState2(p1.getUniqueId());
			Bpref2 = plugin.getDBMgr().getQueuePrefState2(p2.getUniqueId());
		
	 
		if(Bpref1 != Bpref2) {
			return false;
		}
		
		
		
		
		ArrayList<String> allMaps1 = new ArrayList<>();
		ArrayList<String> allMaps2 = new ArrayList<>();
		ArrayList<String> allMapsA = new ArrayList<>();
		
		
		
		
		
		
		File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
		
		
		if(files != null) {
		 for(int i = 0; i < files.length; i++) {
		  if(!files[i].getName().endsWith(".yml")) {
			  continue;
		  }
		  
		   
			   if(plugin.getDBMgr().isMapDisabled(p1.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
					allMaps1.add(files[i].getName().replaceAll(".yml", ""));   
					
				   }
				   if(plugin.getDBMgr().isMapDisabled(p2.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
					allMaps2.add(files[i].getName().replaceAll(".yml", ""));   
				   }
		   
		   
		   allMapsA.add(files[i].getName().replaceAll(".yml", ""));
		
		 }
		}
	
		
		ArrayList<String> remain = allMapsA; // A B C X Y Z
		remain.removeAll(allMaps1);//X Y Z
		remain.removeAll(allMaps2);//X Y Z
		
		if(remain.size() <= 0) {
			return false;
		} 
		
		if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null || plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null) {
			if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() == null || plugin.getOneVsOnePlayer(p1).getPlayerTeam() == null) {
				return false;
			}
			if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null && plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null) {
				if(plugin.getOneVsOnePlayer(p1).getPlayerTeam().getAll().size() > plugin.getOneVsOnePlayer(p2).getPlayerTeam().getAll().size()) {
					return false;
				} else if(plugin.getOneVsOnePlayer(p2).getPlayerTeam().getAll().size() > plugin.getOneVsOnePlayer(p1).getPlayerTeam().getAll().size()) {
					return false;
				}
			}
		}
		
		
			return true;
	}
	
	@SuppressWarnings("unchecked")
	public static String getRandomMap(Player p1, Player p2) {
		
		ArrayList<String> allMaps1 = new ArrayList<>();
		ArrayList<String> allMaps2 = new ArrayList<>();
		ArrayList<String> allMapsA = new ArrayList<>();
		
		File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
		
		if(files != null) {
		 for(int i = 0; i < files.length; i++) {
		  if(!files[i].getName().endsWith(".yml")) {
			  continue;
		  }
		   
		   
			   if(plugin.getDBMgr().isMapDisabled(p1.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
					allMaps1.add(files[i].getName().replaceAll(".yml", ""));   
				   }
				   if(plugin.getDBMgr().isMapDisabled(p2.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
					allMaps2.add(files[i].getName().replaceAll(".yml", ""));   
				   }
		   
		   
		   allMapsA.add(files[i].getName().replaceAll(".yml", ""));
		
		 }
		}
	
		
		
		ArrayList<String> remain = allMapsA; // A B C X Y Z
		ArrayList<String> arenas = (ArrayList<String>) plugin.FreeArenas.clone();
		
		
		
		remain.removeAll(allMaps1);//X Y Z
		remain.removeAll(allMaps2);//X Y Z
		
		if(remain.size() <= 0) {
			return null;
		} else {
		 ArrayList<String> useable = new ArrayList<>();
		 
		 for(String str : arenas) 
		  if(remain.contains(plugin.getPositions().getLayout(str))) useable.add(str);
		 
		 if(useable.size() <= 0) return null;
		 
		 Random r = new Random();
		 
		 int use = r.nextInt(useable.size());
		 
		 if(use < 0) use = 0;
		 if(use >= useable.size())  use = useable.size()-1;
		 
		 return useable.get(use);
		 
		}
	}
	
	public static String getRandomMap(Player p1) {
		ArrayList<String> allMaps1 = new ArrayList<>();
		ArrayList<String> allMaps2 = new ArrayList<>();
		ArrayList<String> allMapsA = new ArrayList<>();
		
		
		File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
		
		
		if(files != null) {
		 for(int i = 0; i < files.length; i++) {
		  if(!files[i].getName().endsWith(".yml")) {
			  continue;
		  }
		   
			   if(plugin.getDBMgr().isMapDisabled(p1.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
					allMaps1.add(files[i].getName().replaceAll(".yml", ""));   
					
				   }
		  
		   
		   allMapsA.add(files[i].getName().replaceAll(".yml", ""));
		
		 }
		}
	
		
		ArrayList<String> remain = allMapsA; // A B C X Y Z
		ArrayList<String> arenas = plugin.FreeArenas;
		remain.removeAll(allMaps1);//X Y Z
		remain.removeAll(allMaps2);//X Y Z
		
		if(remain.size() <= 0) {
			return null;
		} else {
		 ArrayList<String> useable = new ArrayList<>();
		 for(String str : arenas) {
		  if(remain.contains(plugin.getPositions().getLayout(str))) {
		   useable.add(str);
		  }
		 }
		 if(useable.size() <= 0) return null;
		 Random r = new Random();
		 
		 int use = r.nextInt(useable.size());
		 
		 if(use < 0) {
			 use = 0;
		 }
		 if(use >= useable.size()) {
			 use = useable.size()-1;
		 }
		 
		 return useable.get(use);
		 
		}
	}
	
	
	
	private static String getKit(Player p1, Player p2) {
		PlayerQuequePrefs prefP1, prefP2;
		
			if(!plugin.getDBMgr().isConnected()) return null;
			
			prefP1 = plugin.getDBMgr().getQuequePrefState(p1.getUniqueId());
			prefP2 = plugin.getDBMgr().getQuequePrefState(p2.getUniqueId());
		
		
		if(prefP1 == PlayerQuequePrefs.ownKit) {
			  return p1.getUniqueId().toString();
		}
		
		if(prefP1 == PlayerQuequePrefs.EnemieKit) {
			  return p2.getUniqueId().toString();
		}
		
		if(prefP1 == PlayerQuequePrefs.RandomKit) {
		 if(prefP2 == PlayerQuequePrefs.ownKit) {
		  return p2.getUniqueId().toString();
		 }
		 if(prefP2 == PlayerQuequePrefs.EnemieKit) {
		  return p1.getUniqueId().toString();
		 }
		 if(prefP2 == PlayerQuequePrefs.RandomKit) {
		  Random Users = new Random();
		  int useKit = Users.nextInt(plugin.getOneVsOnePlayerSize())+1;
		  
		  UUID kit = null;
		  
		  int a = 0;
		  
		  for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
			  if(a < useKit) {
				  a++;
				  continue;
			  } else {
				  kit = players.getPlayer().getUniqueId();
				  break;
			  }
			  
			  
		  }
		  
		  if(kit == null) return null;
		  
		  return kit.toString();
		 }
		}
		
		return null;
	}
	
	
	public static void joinA(final ArenaTeamPlayer playerP1,final ArenaTeamPlayer playerP2,final String Fmap,final String Fkit) {
		
		
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(plugin.getOneVsOnePlayer(playerP1.getPlayer()).getpState() == PlayerState.InArena) {
					return;
				}
				if(plugin.getOneVsOnePlayer(playerP2.getPlayer()).getpState() == PlayerState.InArena) {
					return;
				}
				ArenaJoin.joinArena(playerP1, playerP2, Fmap, true, Fkit, false, "d");
				
			}
		});
	}
	
	public static void joinA(final Player playerP1,final Player playerP2,final String Fmap,final String Fkit) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(plugin.getOneVsOnePlayer(playerP1).getpState() == PlayerState.InArena) {
					return;
				}
				if(plugin.getOneVsOnePlayer(playerP2).getpState() == PlayerState.InArena) {
					return;
				}
				
				ArenaJoin.joinArena(playerP1, playerP2, Fmap, true, Fkit, false, "d");
			}
		});
		
	}
	
}
