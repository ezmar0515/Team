package team

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TeamLeave {
    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 인자 개수 확인 (/team leave <player>)
        if (args.size < 2) {
            sender.sendMessage("§c사용법: /team add <플레이어>")
            return true
        }

        val board = Bukkit.getScoreboardManager().mainScoreboard
        val target = args[1]

        // 1. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        when {
            // 1. "all" 입력 시: 모든 온라인 플레이어 퇴출
            target.equals("all", ignoreCase = true) -> {
                Bukkit.getOnlinePlayers().forEach { player ->
                    board.getEntryTeam(player.name)?.removeEntry(player.name)
                }
                sender.sendMessage("§a모든 플레이어를 팀에서 퇴장시켰습니다.")
            }
            // 2. 입력값이 팀 이름인 경우: 해당 팀원 전원 퇴출
            board.getTeam(target) != null -> {
                val targetTeam = board.getTeam(target)!!
                // 팀의 모든 엔트리를 복사해서 반복문 돌리기
                targetTeam.entries.toList().forEach { entry ->
                    targetTeam.removeEntry(entry)
                }
                sender.sendMessage("§a'${targetTeam}'의 모든 플레이어를 팀에서 퇴장시켰습니다.")
            }

            else -> {
                val player = Bukkit.getPlayer(target)
                if (player == null) {
                    sender.sendMessage("§c'${target}'은(는) 존재하지 않는 팀이거나 오프라인 플레이어입니다.")
                } else {
                    val currentTeam = board.getEntryTeam(player.name)
                    if (currentTeam == null) {
                        sender.sendMessage("§c'${player.name}' 플레이어는 소속된 팀이 없습니다.")
                    } else {
                        currentTeam.removeEntry(player.name)
                        sender.sendMessage("§a'${player.name}' 플레이어를 '${currentTeam.name}' 팀에서 퇴장시켰습니다.")
                    }
                }
            }
        }
        return true
    }
}