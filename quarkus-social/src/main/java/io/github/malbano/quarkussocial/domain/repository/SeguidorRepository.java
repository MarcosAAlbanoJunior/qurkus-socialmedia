package io.github.malbano.quarkussocial.domain.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.malbano.quarkussocial.domain.model.Seguidor;
import io.github.malbano.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class SeguidorRepository implements PanacheRepository<Seguidor>{
	
	@PersistenceContext
    EntityManager entityManager;

	public boolean follows(User seguidor, User user) {

		var params = Parameters.with("seguidor", seguidor)
				.and("user", user);
		PanacheQuery<Seguidor> query = find("seguidor = :seguidor and user = :user", params);
		Optional<Seguidor> result = query.firstResultOptional();
		
		return result.isPresent();
	}
	
	public List<Seguidor> findByUser(Long userId){
        PanacheQuery<Seguidor> query = find("user.id", userId);
        return query.list();
    }

	public void deleteByFollowerAndUser(Long followerId, Long userId) {
		
		var params = Parameters.with("userId", userId).and("followerId", followerId).map();
		delete("seguidor.id =:followerId and user.id =:userId", params);
	}	
}
