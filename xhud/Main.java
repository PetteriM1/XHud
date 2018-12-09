package xhud;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class Main extends PluginBase {

    public static Config config;

    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getScheduler().scheduleDelayedRepeatingTask(new Hud(this), 10, 10, true);
    }
}

class Hud extends Thread {

    private Main plugin;

    public Hud(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers().values()) {
            String money;
            String hud = Main.config.getString("Message")
                    .replaceAll("<NAME>", player.getName())
                    .replaceAll("<WORLD>", player.getLevel().getName())
                    .replaceAll("<X>", Integer.toString((int) player.x))
                    .replaceAll("<Y>", Integer.toString((int) player.y))
                    .replaceAll("<Z>", Integer.toString((int) player.z)).replaceAll("<N>", "\n")
                    .replaceAll("<PLAYERS>", Integer.toString(plugin.getServer().getOnlinePlayers().size()))
                    .replaceAll("<MAXPLAYERS>", Integer.toString(plugin.getServer().getMaxPlayers()))
                    .replaceAll("<PING>", Integer.toString(player.getPing()));

            try {
                Class.forName("me.onebone.economyapi.EconomyAPI");
                money = Double.toString(me.onebone.economyapi.EconomyAPI.getInstance().myMoney(player));
            } catch (Exception e) {
                money = "null";
            }

            player.sendPopup(hud.replaceAll("<MONEY>", money));
        }
    }
}
