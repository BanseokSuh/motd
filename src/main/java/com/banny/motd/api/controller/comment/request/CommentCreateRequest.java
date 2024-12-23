package com.banny.motd.api.controller.comment.request;

import com.banny.motd.api.service.comment.request.CommentCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    @Size(min = 1, max = 1_000, message = "댓글은 1자 이상 1,000자 이하로 입력해주세요.")
    private String comment;

    public CommentCreateServiceRequest toServiceRequest() {
        return CommentCreateServiceRequest.builder()
                .comment(comment)
                .build();
    }

}
