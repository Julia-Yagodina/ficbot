package tg.bots.bot_with_fics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.bots.bot_with_fics.entity.Owner;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query("SELECT o.id FROM Owner o")
    List<Long> findAllIds();
}
