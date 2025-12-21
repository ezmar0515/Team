package team

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TeamAuto {
    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // 인자 개수 확인 (/team auto <teamname/random>)
        if (args.size < 2) {
            sender.sendMessage("§c사용법: /team auto <teamname/random>")
            return true
        }

        val board = Bukkit.getScoreboardManager().mainScoreboard
        val target = args[1]

        // 1. 권한 확인(관리자만 사용 가능)
        if (!sender.isOp) {
            sender.sendMessage("§c관리자만 이 명령어를 사용할 수 있습니다.")
            return true
        }

        // 2. 모든 온라인 플레이어 불러오고 그 중에 팀이 없는 사람 판별하기
        val playersWithoutTeam = Bukkit.getOnlinePlayers().filter { player -> board.getEntryTeam(player.name) == null }

        if (playersWithoutTeam.isEmpty()) {
            sender.sendMessage("§c모든 사람이 팀이 있습니다.")
            return true
        }

        // 3. 팀명인지 랜덤인지 파악
        when (target.lowercase()) {
            "random" -> {
                // 3.1. 랜덤일 경우
                sender.sendMessage("§a무작위 팀 배정을 시작합니다.")

                val teams = board.teams.toList() // 존재하는 모든 팀 리스트
                if(teams.isEmpty()) return true // 팀이 하나도 없으면 중단

                // 3.1.1. 무작위로 섞기
                val shuffledPlayers = playersWithoutTeam.shuffled()

                // 3.1.2. 팀에 사람을 배정
                val teamCount = teams.size

                shuffledPlayers.forEachIndexed { index, player ->
                    val targetTeam = teams[index % teamCount]
                    targetTeam.addEntry(player.name)
                    player.sendMessage("§a당신은 이제 §f${targetTeam.name}§a 팀 소속입니다.")
                }
                sender.sendMessage("§a${shuffledPlayers.size}명의 플레이어를 ${teamCount}개의 팀에 배정했습니다.")
            }
            else -> {
                // 3.1. mode가 "random"이 아니라면, 입력한 글자를 팀 이름으로 간주
                val targetTeam = board.getTeam(target)
                if (targetTeam == null) {
                    sender.sendMessage("§c'${target}' 팀을 찾을 수 없습니다.")
                } else {
                    // 해당 팀에 자동으로 사람들을 채워넣는 로직

                    // 3.1.1. 특정 팀(명령어에서 지정된)에 팀이 없는 사람을 모두 집어넣기
                    playersWithoutTeam.forEach { player ->
                        targetTeam.addEntry(player.name)
                        player.sendMessage("§a당신은 이제 §f${targetTeam.name}§a 팀 소속입니다.")
                    }
                    sender.sendMessage("§a팀이 없던 ${playersWithoutTeam.size}명을 ${targetTeam.name} 팀에 배정했습니다.")
                }
            }
        }

        return true
    }
}