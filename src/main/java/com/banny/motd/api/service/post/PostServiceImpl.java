package com.banny.motd.api.service.post;

import com.banny.motd.api.service.comment.CommentService;
import com.banny.motd.api.service.post.request.PostCreateServiceRequest;
import com.banny.motd.api.service.post.request.PostModifyServiceRequest;
import com.banny.motd.api.service.reaction.ReactionService;
import com.banny.motd.domain.comment.Comment;
import com.banny.motd.domain.post.Post;
import com.banny.motd.domain.post.PostDetail;
import com.banny.motd.domain.post.infrastructure.PostRepository;
import com.banny.motd.domain.reaction.Reaction;
import com.banny.motd.domain.user.User;
import com.banny.motd.domain.user.infrastructure.UserRepository;
import com.banny.motd.global.dto.request.SearchRequest;
import com.banny.motd.global.exception.ApiStatusType;
import com.banny.motd.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReactionService reactionService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Post createPost(PostCreateServiceRequest request, Long userId) {
        User user = userRepository.getById(userId);

        Post post = Post.builder()
                .author(user)
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .build();

        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getPostList(SearchRequest request) {
        return postRepository.getPostList(request);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetail getPost(Long postId) {
        Post post = postRepository.getById(postId);
        User user = userRepository.getById(post.getAuthor().getId());

        List<Reaction> likeList = reactionService.getLikeListByPostId(postId);
        List<Comment> commentList = commentService.getCommentListByPostId(postId);

        return PostDetail.builder()
                .post(post)
                .user(user)
                .commentList(commentList)
                .likeList(likeList)
                .build();
    }

    @Override
    @Transactional
    public void modifyPost(Long postId, PostModifyServiceRequest request, Long userId) {
        Post post = postRepository.getById(postId);
        User user = userRepository.getById(userId);

        if (!post.isAuthor(user.getId())) {
            throw new ApplicationException(ApiStatusType.FAIL_INVALID_PERMISSION, String.format("%s has no permission with the post", user.getLoginId()));
        }

        post.setPost(request.getContent());

        postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = userRepository.getById(userId);
        Post post = postRepository.getById(postId);

        if (!post.isAuthor(user.getId())) {
            throw new ApplicationException(ApiStatusType.FAIL_INVALID_PERMISSION, String.format("%s has no permission with the post", user.getLoginId()));
        }

        postRepository.delete(post);
    }

}
