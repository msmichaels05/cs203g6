package com.amateuraces.player;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    Long save(Player player);
    int update(Player player);
    int deleteById(Long id);
    List<Player> findAll();

    // Using Optional - the return value of this method may contain a null value
    Optional<Player> findById(Long id);
 
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 * For the purpose of this exercise, CrudRepository would also be sufficient
 */
@Repository
public interface PlayerRepository extends JpaRepository <Player, Long>{
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
}
