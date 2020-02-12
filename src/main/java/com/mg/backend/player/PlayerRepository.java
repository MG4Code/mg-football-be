package com.mg.backend.player;

import io.reactivex.Flowable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.RxJava2SortingRepository;


public interface PlayerRepository extends RxJava2SortingRepository<Player, String> {

  @Query("{ 'club': {'$regex': ?0, '$options': 'i'}}")
  Flowable<Player> findByClub(String club);

}
