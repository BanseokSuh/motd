package com.banny.motd.domain.post.infrastructure;

import com.banny.motd.domain.post.infrastructure.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {
}
