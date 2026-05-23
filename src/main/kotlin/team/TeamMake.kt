package team

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TeamMake {
    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 인자 개수 확인 (/team make <teamname>)
        if (args.size < 2) {
            sender.sendMessage("§c사용법: /team make <팀이름>")
            return true
        }

        val board = Bukkit.getScoreboardManager().mainScoreboard
        val teamName = args[1]

        // 1. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        // 2. 추가하려는 팀이 존재하는지 확인
        val targetTeam = board.getTeam(teamName)

        if (targetTeam != null) {
            sender.sendMessage("§c${teamName}이라는 팀이 존재합니다.")
            return true
        }

        // 3. 팀을 생성
        try {
            board.registerNewTeam(teamName)
            sender.sendMessage("§a[Team] ${teamName} 팀이 생성되었습니다.")
        } catch (e: IllegalArgumentException) {
            sender.sendMessage("§c이미 존재하는 팀 이름입니다.")
        }

        return true
    }
}