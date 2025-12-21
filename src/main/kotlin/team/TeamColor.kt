package team

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import net.kyori.adventure.text.format.NamedTextColor

class TeamColor {
    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val board = Bukkit.getScoreboardManager().mainScoreboard
        val teamName = args[1]
        val colorInput = args[2].lowercase()

        // 입력받은 문자열을 실제 색상 객체로 변환
        val targetColor = NamedTextColor.NAMES.value(colorInput)
        if (targetColor == null) {
            sender.sendMessage("§c알 수 없는 색상입니다: ${colorInput}")
            return true
        }

        // 인자 개수 확인 (/team color <teamname> <colorname>)
        if (args.size < 3) {
            sender.sendMessage("§c사용법: team color <teamname> <colorname>")
            return true
        }

        // 1. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        // 2. 팀이 존재하는지 확인
        val targetTeam = board.getTeam(teamName)
        if (targetTeam == null) {
            sender.sendMessage("§c${teamName}이라는 팀이 존재하지 않습니다. 먼저 팀을 생성하세요.")
            return true
        }

        // 3. 그 색을 쓰는 팀이 이미 존재하는지 확인
        val isColorUsed = board.teams.any { it.color() == targetColor } // board.teams: 스코어 보드에 있는 모든 Team 객체들의 집합, it: 반복문 안의 각 팀을 가리키는 코틀린의 예약어
        if (isColorUsed) {
            sender.sendMessage("§c이미 '${colorInput}' 색상을 사용하는 팀이 있습니다.")
            return true
        }

        // 4. 팀의 색 설정
        targetTeam.color(targetColor)
        sender.sendMessage("§a${teamName} 팀의 색상이 ${colorInput}(으)로 설정되었습니다.")

        return true
    }
}