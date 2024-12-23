package com.banny.motd.domain.comment.infrastructure.entity;

import com.banny.motd.domain.comment.Comment;
import com.banny.motd.domain.user.infrastructure.entity.UserEntity;
import com.banny.motd.global.entity.BaseEntity;
import com.banny.motd.global.enums.TargetType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE \"comment\" SET deleted_at = NOW() where id = ?")
@Table(name = "\"comment\"", indexes = {
        @Index(name = "index_comment_user_id_target_type_target_id", columnList = "user_id, target_type, target_id"),
})
@Entity
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "target_type", nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Column(name = "target_id", nullable = false, columnDefinition = "BIGINT")
    private Long targetId;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Builder
    private CommentEntity(Long id, UserEntity user, TargetType targetType, Long targetId, String comment) {
        this.id = id;
        this.user = user;
        this.targetType = targetType;
        this.targetId = targetId;
        this.comment = comment;
    }

    public static CommentEntity from(Comment comment) {
        return CommentEntity.builder()
                .id(comment.getId())
                .user(UserEntity.from(comment.getAuthor()))
                .targetType(comment.getTargetType())
                .targetId(comment.getTargetId())
                .comment(comment.getComment())
                .build();
    }

    public static CommentEntity of(UserEntity userEntity, TargetType targetType, Long targetId, String comment) {
        return CommentEntity.builder()
                .user(userEntity)
                .targetType(targetType)
                .targetId(targetId)
                .comment(comment)
                .build();
    }

    public Comment toDomain() {
        return Comment.builder()
                .id(id)
                .author(user.toDomain())
                .targetType(targetType)
                .targetId(targetId)
                .comment(comment)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .deletedAt(getDeletedAt())
                .build();
    }

}
