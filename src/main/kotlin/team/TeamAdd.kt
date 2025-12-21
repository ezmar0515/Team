package team

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TeamAdd {
    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 인자 개수 확인 (/team add <player> <teamname>)
        if (args.size < 3) {
            sender.sendMessage("§c사용법: /team add <플레이어> <팀이름>")
            return true
        }

        val board = Bukkit.getScoreboardManager().mainScoreboard
        val playerName = args[1]
        val teamName = args[2]

        // 1. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        // 2. 플레이어가 있는 지 확인
        val targetPlayer = Bukkit.getPlayer(playerName)
        if (targetPlayer == null) {
            sender.sendMessage("§c${playerName}이/가 현재 서버에 없습니다.")
            return true
        }

        // 3. 플레이어에게 팀이 있는 지 확인
        if (board.getEntityTeam(targetPlayer) != null) {
            sender.sendMessage("§플레이어가 이미 팀이 있습니다. 팀에서 제거하고 다시 시도하십시오.")
            return true
        }

        // 4. 추가하려는 팀이 존재하는지 확인
        val targetTeam = board.getTeam(teamName)
        if (targetTeam == null) {
            sender.sendMessage("§c${teamName}이라는 팀이 존재하지 않습니다. 먼저 팀을 생성하세요.")
            return true
        }

        // 5. 팀에 추가
        targetTeam.addEntry(targetPlayer.name)
        sender.sendMessage("§a${targetPlayer}님을 ${targetTeam}에 추가했습니다.")
        targetPlayer.sendMessage("§a당신은 이제 ${targetTeam.displayName} 팀 소속입니다.")

        return true
    }
}