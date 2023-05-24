package io.github.malbano.quarkussocial.domain.repository;

import javax.enterprise.context.ApplicationScoped;

import io.github.malbano.quarkussocial.domain.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post>{

}
