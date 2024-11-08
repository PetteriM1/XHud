package xhud;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;

import java.util.Map;
import java.util.UUID;

public class Main extends PluginBase {

	public void onEnable() {
		saveDefaultConfig();
		String message = getConfig().getString("Message");
		int updateIntervalTicks = Math.max(getConfig().getInt("UpdateIntervalTicks", 10), 1);
		getServer().getScheduler().scheduleDelayedRepeatingTask(this, () -> {
			Map<UUID, Player> players = getServer().getOnlinePlayers();
			for (Player player : players.values()) {
				String money;
				String hud = message
						.replace("<NAME>", player.getName())
						.replace("<WORLD>", player.getLevel().getName())
						.replace("<X>", Integer.toString((int) player.x))
						.replace("<Y>", Integer.toString((int) player.y))
						.replace("<Z>", Integer.toString((int) player.z))
						.replace("<N>", "\n")
						.replace("<PLAYERS>", Integer.toString(players.size()))
						.replace("<MAXPLAYERS>", Integer.toString(getServer().getMaxPlayers()))
						.replace("<PING>", Integer.toString(player.getPing()))
						.replace("<TPS>", Float.toString(getServer().getTicksPerSecond()));

				try {
					Class.forName("me.onebone.economyapi.EconomyAPI");
					money = Double.toString(me.onebone.economyapi.EconomyAPI.getInstance().myMoney(player));
				} catch (Exception e) {
					money = "null";
				}

				player.sendTip(hud.replace("<MONEY>", money).replace("<FNAME>", Main.getFName(player)));
			}
		}, updateIntervalTicks, updateIntervalTicks);
	}

	public static String getFName(Player player) {
		try {
			Class.forName("com.massivecraft.factions.P");
			return com.massivecraft.factions.P.p.getPlayerFactionTag(player);
		} catch (Exception e) {
			return "null";
		}
	}
}

