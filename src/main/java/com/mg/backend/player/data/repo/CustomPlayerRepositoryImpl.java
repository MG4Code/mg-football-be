package com.mg.backend.player.data.repo;

import com.mg.backend.player.data.dto.Player;
import com.mg.backend.player.data.dto.Position;
import io.reactivex.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CustomPlayerRepositoryImpl implements CustomPlayerRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public Maybe<Player> getFirstGoalKeeper() {
    Query query = new Query(Criteria.where("position").is(Position.GOAL_KEEPER));
    List<Player> players = mongoTemplate.find(query, Player.class);
    if (!players.isEmpty()) {
      return Maybe.just(players.get(0));
    } else {
      return Maybe.empty();
    }
  }
}
