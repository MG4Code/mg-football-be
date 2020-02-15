package com.mg.backend.player;

import com.mg.backend.player.data.dto.Player;
import com.mg.backend.player.data.repo.PlayerRepository;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;

@Service
public class PlayerService {

  private final PlayerRepository repo;

  @Autowired
  public PlayerService(PlayerRepository repo) {
    this.repo = repo;
  }

  public Flowable<Player> list() {
    return repo.findAll(Sort.by("shirtNumber"));
  }

  public Single<Player> add(Player player) {
    return repo.save(player);
  }

  public Single<Player> put(String id, Player player) {
    return repo.findById(id)
      .switchIfEmpty(Single.error(new WebApplicationException("No player found with id '" + id + "'", 404)))
      .map(byId -> {
        if (player.getFirstName() != null) {
          byId.setFirstName(player.getFirstName());
        }
        if (player.getLastName() != null) {
          byId.setLastName(player.getLastName());
        }
        if (player.getClub() != null) {
          byId.setClub(player.getClub());
        }
        if (player.getPosition() != null) {
          byId.setPosition(player.getPosition());
        }
        if (player.getShirtNumber() != null) {
          byId.setShirtNumber(player.getShirtNumber());
        }
        return byId;
      }).flatMap(e -> repo.save(e));
  }

  public Completable delete(String id) {
    return repo.deleteById(id);
  }

  public Flowable<Player> findAllForClub(String query) {
    return repo.findByClub(query);
  }

  public Maybe<Player> findPlayerById(String id) {
    return repo.findById(id);
  }

  public Maybe<Player> getFirstGoalKeeper() {
    return repo.getFirstGoalKeeper();
  }
}
