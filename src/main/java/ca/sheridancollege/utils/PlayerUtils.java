package ca.sheridancollege.utils;

import java.util.Random;

import ca.sheridancollege.beans.Player;
import ca.sheridancollege.beans.Team;
import ca.sheridancollege.repositories.TeamsRepository;

public class PlayerUtils {

    public void removePlayerFromTeam(Player player, TeamsRepository teamRepo) {
        if (player.getTeam() != null) {
            Team team = teamRepo.findById(player.getTeam().getId()).get();

            for (int i = 0; i < team.getPlayers().size(); i++) {
                if (team.getPlayers().get(i).getId() == player.getId()) {
                    // check if the player is the captain
                    boolean flag = false;
                    if (team.getPlayers().get(i).getName().equals(team.getCaptain())) {
                        flag = true;
                    }
                    team.getPlayers().remove(i);
                    if (flag) {
                        Random rand = new Random();
                        team.setCaptain(team.getPlayers().get(rand.nextInt(team.getPlayers().size())).getName());
                    }
                }
            }

            teamRepo.save(team);
        }
    }

}
