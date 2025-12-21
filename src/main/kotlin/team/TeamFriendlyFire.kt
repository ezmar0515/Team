package team

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TeamFriendlyFire {
    fun onCommand (sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 1. 인자 개수 확인 (/team friendlyfire <teamname> <boolean>)
        if (args.size < 3) {
            sender.sendMessage("§c사용법: /team friendlyfire <teamname> <boolean>")
            return true
        }

        // 2. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        val board = Bukkit.getScoreboardManager().mainScoreboard
        val teamName = args[1]

        val targetBoolean = args[2].toBooleanStrictOrNull()
        if (targetBoolean == null) {
            sender.sendMessage("§c값은 true 또는 false여야 합니다.")
            return true
        }

        // 3. 팀이 있는 지 확인
        val targetTeam = board.getTeam(teamName)
        if (targetTeam == null) {
            sender.sendMessage("§c${teamName}이라는 팀이 존재하지 않습니다.")
            return true
        }

        // 4. friendlyfire 설정
        try {
            targetTeam.setAllowFriendlyFire(targetBoolean)
            sender.sendMessage("§a${teamName} 팀의 FriendlyFire가 '${targetBoolean}'로 설정되었습니다.")
        } catch (e: IllegalStateException) {
            sender.sendMessage("§c오류: 팀 상태를 변경할 수 없습니다.")
        }

        return true
    }
}