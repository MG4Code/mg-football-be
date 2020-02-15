package com.mg.backend.player.data.repo;

import com.mg.backend.player.data.dto.Player;
import com.mg.backend.player.data.dto.Position;
import io.reactivex.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class CustomPlayerRepositoryImpl implements CustomPlayerRepository {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public CustomPlayerRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Maybe<Player> getFirstGoalKeeper() {
    var query = new Query(Criteria.where("position").is(Position.GOAL_KEEPER));
    var players = mongoTemplate.find(query, Player.class);
    if (!players.isEmpty()) {
      return Maybe.just(players.get(0));
    } else {
      return Maybe.empty();
    }
  }
}
