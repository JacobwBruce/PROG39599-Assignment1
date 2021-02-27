package ca.sheridancollege.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.beans.Team;

@Repository
public interface TeamsRepository extends CrudRepository<Team, Integer> {

    public List<Team> findByName(String name);

}
