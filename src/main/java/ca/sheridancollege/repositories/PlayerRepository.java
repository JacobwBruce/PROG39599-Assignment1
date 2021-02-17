package ca.sheridancollege.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {

    public List<Player> findByName(String name);

    public List<Player> findByAge(int age);

    public List<Player> findByGender(String gender);

    // also need to search by team

}
