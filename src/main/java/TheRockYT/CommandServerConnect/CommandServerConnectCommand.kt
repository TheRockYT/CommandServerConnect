package TheRockYT.CommandServerConnect

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

class CommandServerConnectCommand(name: String?) : Command(name) {
    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        if(args!!.isEmpty()){
            sender!!.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.info")))
        }else if(args[0].equals("version", true) || args[0].equals("updates", true)){
            val perm: String? = CommandServerConnect.config?.getString("permission.updates")
            if(sender!!.hasPermission(perm)){
                sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.update.checking")))
                val oldState: Updater.CheckState? = CommandServerConnect.updater?.state
                CommandServerConnect.updater?.check()
                if (oldState == CommandServerConnect.updater?.state) {
                    sender.sendMessage(
                        CommandServerConnect.replacePlaceholder(
                            CommandServerConnect.config?.get("messages.update." + oldState.toString().toLowerCase())
                        )
                    )
                }
            }else{
                sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.permission"))?.replace("%permission%", perm!!))
            }
        }else if(args[0].equals("reload", true)){
            val perm: String? = CommandServerConnect.config?.getString("permission.reload")
            if(sender!!.hasPermission(perm)){
                sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.reload_start")))
                CommandServerConnect.instance?.reload()
                sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.reload_end")))

            }else{
                sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.permission"))?.replace("%permission%", perm!!))
            }
        }else{
            sender!!.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.help")))

        }
    }
}