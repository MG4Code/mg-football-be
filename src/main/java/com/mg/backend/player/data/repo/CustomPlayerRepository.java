package com.mg.backend.player.data.repo;

import com.mg.backend.player.data.dto.Player;
import io.reactivex.Maybe;

public interface CustomPlayerRepository {

  Maybe<Player> getFirstGoalKeeper();

}
