package team

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import net.kyori.adventure.text.Component

class TeamSuffix {
    fun onCommand (sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 인자 개수 확인 (/team suffix <teamname> <suffix>)
        if (args.size < 3) {
            sender.sendMessage("§c사용법: /team suffix <팀이름> <suffix>")
            return true
        }

        val board = Bukkit.getScoreboardManager().mainScoreboard
        val teamName = args[1]
        val suffix = args.slice(2 until args.size).joinToString(" ")

        // 1. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        // 2. 팀이 있는 지 확인
        val targetTeam = board.getTeam(teamName)

        if (targetTeam == null) {
            sender.sendMessage("§c${teamName}이라는 팀이 존재하지 않습니다.")
            return true
        }

        // 3. suffix 추가
        try {
            targetTeam.suffix(Component.text(suffix))
            sender.sendMessage("§a${teamName} 팀의 접두사가 '${suffix}'(으)로 설정되었습니다.")
        } catch (e: IllegalStateException) {
            sender.sendMessage("§c오류: 팀이 등록 해제된 상태입니다.")
        }

        return true
    }
}