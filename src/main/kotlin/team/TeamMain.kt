package team

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender



class TeamMain : JavaPlugin() {
    // Team 인스턴스 생성
    private val teamAddExecutor = TeamAdd()
    private val teamAutoExecutor = TeamAuto()
    private val teamColorExecutor = TeamColor()
    private val teamDisplayNameExecutor = TeamDisplayName()
    private val teamFriendlyFireExecutor = TeamFriendlyFire()
    private val teamLeaveExecutor = TeamLeave()
    private val teamMakeExecutor = TeamMake()
    private val teamPrefixExecutor = TeamPrefix()
    private val teamSuffixExecutor = TeamSuffix()

    override fun onEnable() {
        getCommand("team")?.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender,
                           command: Command,
                           label: String,
                           args: Array<out String>
                        ): Boolean {
        if (!command.name.equals("team", ignoreCase = true)) {
            return false
        }

        // 인수가 없는 경우 처리
        if (args.isEmpty()) {
            sender.sendMessage("§b[Team] 사용법: /team <add|auto|displayname|friendlyfire|leave|prefix|suffix>")
            return true
        }

        // 명령어 분기 처리
        return when (args[0].lowercase()) {
            "add" -> teamAddExecutor.onCommand(sender, command, label, args)
            "auto" -> teamAutoExecutor.onCommand(sender, command, label, args)
            "color" -> teamColorExecutor.onCommand(sender, command, label, args)
            "displayname" -> teamDisplayNameExecutor.onCommand(sender, command, label, args)
            "friendlyfire" -> teamFriendlyFireExecutor.onCommand(sender, command, label, args)
            "leave" -> teamLeaveExecutor.onCommand(sender, command, label, args)
            "make" -> teamMakeExecutor.onCommand(sender, command, label, args)
            "prefix" -> teamPrefixExecutor.onCommand(sender, command, label, args)
            "suffix" -> teamSuffixExecutor.onCommand(sender, command, label, args)
            else -> {
                sender.sendMessage("§c알 수 없는 명령어입니다: /team ${args[0]}")
                false
            }
        }
    }
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String>? {
        // /team <여기>
        if (args.size == 1) {
            return listOf("add", "auto", "color", "displayname", "friendlyfire", "leave", "make", "prefix", "suffix")
                .filter { it.startsWith(args[0].lowercase()) } // 친 글자로 시작하는 것만 필터링
        }

        // /team auto <여기> 또는 /team color <여기>
        if (args.size == 2) {
            val board = Bukkit.getScoreboardManager().mainScoreboard
            return board.teams.map { it.name }.filter { it.startsWith(args[1].lowercase()) }
        }

        return emptyList()
    }
}
