package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import com.boolfly.grademanagementrestful.api.dto.post.PostAddRequest;
import com.boolfly.grademanagementrestful.api.dto.post.PostResponse;
import com.boolfly.grademanagementrestful.api.dto.post.PostUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.post.SearchPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Post Resource")
@Validated
public interface PostResource {
    @Operation(summary = "Get posts")
    PageResponse<PostResponse> getPosts(@RequestParam(defaultValue = "0") @Min(0) int page,
                                        @RequestParam(defaultValue = "10") @Min(1) int size,
                                        @RequestBody SearchPostRequest request);

    @Operation(summary = "Add a new post")
    PostResponse addPost(@Valid @RequestBody PostAddRequest request);

    @Operation(summary = "Update a post")
    PostResponse updatePost(@Valid @RequestBody PostUpdateRequest request);

    @Operation(summary = "Deactivate a post")
    void deactivatePost(String postId);
}
