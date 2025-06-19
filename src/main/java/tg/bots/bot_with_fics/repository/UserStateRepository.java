package tg.bots.bot_with_fics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bots.bot_with_fics.entity.UserState;

@Repository
public interface UserStateRepository extends JpaRepository<UserState, Long> {
}
