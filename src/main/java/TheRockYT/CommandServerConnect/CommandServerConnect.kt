package TheRockYT.CommandServerConnect

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File


class CommandServerConnect : Plugin() {
    companion object {
        var config: Config? = null
        var updater: Updater? = null
        var version: String? = null

        fun replacePlaceholder(obj: Any?): String? {
            var finalString: String? = null
            if (obj is List<*>) {
                for (str in obj as List<*>) {
                    if (finalString == null) {
                        finalString = str as String?
                    } else {
                        finalString = finalString + "\n" + str as String?
                    }
                }
            }
            if (finalString == null) {
                finalString = obj as String?
            }
            if (finalString != null) {
                finalString = finalString.replace("&", "§")
                finalString = finalString.replace("%latest%", updater!!.latest)
                finalString = finalString.replace("%development%", updater!!.latestDevelopment)
                finalString = version?.let { finalString!!.replace("%version%", it) }
            }
            return finalString
        }
        fun broadcast(message: String?, permission: String?) {
            ProxyServer.getInstance().console.sendMessage(message)
            for (player in ProxyServer.getInstance().players) {
                if (player.hasPermission(permission)) {
                    player.sendMessage(message)
                }
            }
        }
    }
    override fun onEnable() {

        val cm: CommandSender = ProxyServer.getInstance().console
        cm.sendMessage("§aLoading §bCommandServerConnect...")
        config = Config(File(dataFolder, "config.yml"))
        version = description.version
        reload()
        ProxyServer.getInstance().pluginManager.registerCommand(this, CommandServerConnectCommand("CommandServerConnect"))
        for(str in config!!.getValues("connect")!!){
            val display: String? = config!!.getString("connect."+str+".display")
            val cmd: String? = config!!.getString("connect."+str+".cmd")
            val server: String? = config!!.getString("connect."+str+".server")
            val permission: String? = config!!.getString("connect."+str+".permission")
            for(str2 in cmd!!.split(";")){

                ProxyServer.getInstance().pluginManager.registerCommand(this, ServerConnectionCommand(str2, display!!, server!!, permission))
            }
        }
        cm.sendMessage("§bCommandServerConnect §aloaded.")
        cm.sendMessage("§eThanks for using §bCommandServerConnect")
    }
    fun reload() {
        val ex: Boolean = config?.file?.exists() == true
        config?.load()
        config?.set("info", "CommandServerConnect v$version by TheRockYT.")
        if(!ex){
            config?.set("connect.portal.server", "portal");
            config?.set("connect.portal.cmd", "portal");
            config?.set("connect.portal.display", "Portal");
            config?.set("connect.portal.permission", "CommandServerConnect.server.portal");

            config?.set("connect.lobby.server", "lobby");
            config?.set("connect.lobby.cmd", "lobby;l;hub");
            config?.set("connect.lobby.display", "Lobby");
        }
        config?.add("permission.updates", "CommandServerConnect.version")
        config?.add("messages.info", "&bCommandServerConnect &6> &bCommandServerConnect &aby TheRockYT")
        config?.add("messages.permission", "&bCommandServerConnect &6> &cYou need the permission \"%permission%\".")
        config?.add("messages.help", "&bCommandServerConnect &6> &cUse \"CommandServerConnect version\" to check for updates.")
        config?.add("messages.connecting", "&bCommandServerConnect &6> &aConnecting you to \"%server%\".")
        config?.add("messages.connected", "&bCommandServerConnect &6> &cYou are already on this server.")
        config?.add("messages.not_exist", "&bCommandServerConnect &6> &cThis server does not exist.")
        config?.add("messages.console", "&bCommandServerConnect &6> &cYou need to be a player.")
        val development_outdated = ArrayList<String>()
        development_outdated.add("&bCommandServerConnect &6> &cYou are using an &4outdated &bdevelopment &cversion of &bCommandServerConnect&c. &4Your version: v%version%. &aLatest release: v%latest%. &bLatest development: v%development%.")
        development_outdated.add("&bCommandServerConnect &6> &aDownload the latest version at https://therockyt.github.io")
        val outdated = ArrayList<String>()
        outdated.add("&bCommandServerConnect &6> &cYou are using an &4outdated &cversion of &bCommandServerConnect&c. &4Your version: v%version%. &aLatest release: v%latest%. &bLatest development: v%development%.")
        outdated.add("&bCommandServerConnect &6> &aDownload the latest version at https://therockyt.github.io.")
        config?.add("messages.update.outdated_development", development_outdated)
        config?.add("messages.update.outdated", outdated)
        config?.add(
            "messages.update.latest",
            "&bCommandServerConnect &6> &aYou are using the latest release of &bCommandServerConnect&a. &aYour version: v%version%. &aLatest release: v%latest%. &bLatest development: v%development%."
        )
        config?.add(
            "messages.update.latest_development",
            "&bCommandServerConnect &6> &aYou are using the latest &bdevelopment &aversion of &bCommandServerConnect&a. &bYour version: v%version%. &aLatest release: v%latest%. &bLatest development: v%development%."
        )
        config?.add("messages.update.checking", "&bCommandServerConnect &6> &aChecking for updates...")
        config?.add("messages.update.check_failed", "&bCommandServerConnect &6> &4Update check failed.")
        config?.save()
        if (updater != null) {
            updater?.stop()
        }
        updater = version?.let { Updater(it, "https://therockyt.github.io/CommandServerConnect/versions.json", this) }
        updater?.check()
        updater?.runUpdater()

    }
    override fun onDisable() {
        // Plugin shutdown logic
    }
}
