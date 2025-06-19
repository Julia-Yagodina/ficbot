package tg.bots.bot_with_fics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.bots.bot_with_fics.entity.Fanfic;

import java.util.List;
import java.util.Optional;

@Repository
public interface FanficRepository extends JpaRepository<Fanfic, Integer> {
    @Query("SELECT f.id FROM Fanfic f")
    List<Integer> findAllIds();

    @Query("SELECT f.id FROM Fanfic f WHERE f.mood = :mood")
    List<Integer> findAllIdsByMood(@Param("mood") String mood);

    @Query("SELECT f.id FROM Fanfic f WHERE f.mood = :mood AND f.isCheck = :check")
    List<Integer> findAllIdsByMoodAndCheck(@Param ("mood") String mood, @Param("check") boolean check);

    @Modifying
    @Query("DELETE FROM Fanfic f WHERE f.title = :title AND f.author = :author")
    void deleteFic(@Param("title") String title, @Param("author") String author);

    @Query("SELECT f FROM Fanfic f WHERE f.title = :title AND f.author = :author")
    Optional<Fanfic> findFanfic(@Param("title") String title, @Param("author") String author);
}
