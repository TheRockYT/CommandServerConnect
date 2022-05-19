package TheRockYT.CommandServerConnect

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command


class ServerConnectionCommand(name: String?, private val display: String, private val server: String, private val perm: String?) : Command(name){

    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        if (sender != null) {
            if(sender is ProxiedPlayer){
                if(perm == null || sender.hasPermission(perm)){
                    sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.connecting"))?.replace("%server%", display))
                    if(sender.server.info.name.equals(server, true)){
                        sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.connected"))?.replace("%server%", display))

                    }else{
                        val serverInfo: ServerInfo? = ProxyServer.getInstance().getServerInfo(server);
                        if(serverInfo != null){
                            sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.connecting"))?.replace("%server%", display))
                            sender.connect(serverInfo)
                        }else{
                            sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.not_exist"))?.replace("%server%", display))

                        }

                    }
                }else{
                    sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.permission"))?.replace("%permission%", perm))
                }
            }else{
                sender.sendMessage(CommandServerConnect.replacePlaceholder(CommandServerConnect.config?.get("messages.console")))
            }
        }
    }
}