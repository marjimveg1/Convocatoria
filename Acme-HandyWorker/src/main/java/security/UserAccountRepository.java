
package security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

	@Query("select u from UserAccount u where u.username = ?1")
	UserAccount findByUsername(String username);

	@Query("select a.userAccount from Actor a where a.id = ?1")
	UserAccount findByActorId(int actorId);
}
