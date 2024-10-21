package com.banny.motd.api.controller.comment;

import com.banny.motd.api.controller.comment.dto.request.CommentCreateRequest;
import com.banny.motd.api.service.comment.CommentService;
import com.banny.motd.domain.user.User;
import com.banny.motd.global.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public Response<Void> commentPost(@PathVariable Long postId, @RequestBody CommentCreateRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        commentService.commentPost(postId, user.getId(), request.getComment());

        return Response.success();
    }

}