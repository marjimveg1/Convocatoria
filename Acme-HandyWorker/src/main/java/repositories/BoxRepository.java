
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	@Query("select b from Box b join b.actor a where b.name=?2 and a.id=?1")
	Box searchBox(int actorId, String nameBox);

	@Query("select b from Box b join b.actor a where a.id=?1")
	Page<Box> findAllBoxByActor(int actorId, Pageable pageable);

	@Query("select b from Box b join b.actor a where a.id=?1")
	Collection<Box> findBoxesByActor(int actorId);

	@Query("select b from Box b join b.messages m where m.id=?1")
	Collection<Box> boxWithMessage(int messageId);

	@Query("select b from Box b join b.messages m where m.id = ?1 and b.actor.id = ?2")
	Box searchBoxByMessageAndActor(int messageId, int actorId);

}
