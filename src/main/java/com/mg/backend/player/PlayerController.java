package com.mg.backend.player;

import com.mg.backend.player.data.dto.Player;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/player")
public class PlayerController {

  private final PlayerService service;

  @Autowired
  public PlayerController(PlayerService service) {
    this.service = service;
  }

  @GetMapping
  public Flowable<Player> list() {
    return service.list();
  }

  @GetMapping("id")
  public Maybe<Player> findPlayer(@RequestParam("q") String id) {
    return service.findPlayerById(id);
  }

  @GetMapping("club")
  public Flowable<Player> findClub(@RequestParam("q") String query) {
    return service.findAllForClub(query);
  }

  @PostMapping
  public Single<Player> add(@RequestBody Player player) {
    return service.add(player);
  }

  @PutMapping("/{id}")
  public Single<Player> put(@PathVariable("id") String id, @RequestBody Player player) {
    return service.put(id, player);
  }

  @DeleteMapping("/{id}")
  public Completable delete(@PathVariable("id") String id) {
    return service.delete(id);
  }

  @GetMapping("goalkeeper")
  public Maybe<Player> getFirstGoalKeeper() {
    return service.getFirstGoalKeeper();
  }

}
