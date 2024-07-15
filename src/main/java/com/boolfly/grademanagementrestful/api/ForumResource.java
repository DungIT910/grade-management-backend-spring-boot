package com.boolfly.grademanagementrestful.api;

import com.boolfly.grademanagementrestful.api.dto.forum.ForumAddRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumResponse;
import com.boolfly.grademanagementrestful.api.dto.forum.ForumUpdateRequest;
import com.boolfly.grademanagementrestful.api.dto.forum.SearchForumRequest;
import com.boolfly.grademanagementrestful.api.dto.general.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Forum Resource")
@Validated
public interface ForumResource {
    @Operation(summary = "Get all forums")
    PageResponse<ForumResponse> getForums(@RequestParam(defaultValue = "0") @Min(0) int page,
                                          @RequestParam(defaultValue = "10") @Min(1) int size,
                                          @RequestBody SearchForumRequest request);

    @Operation(summary = "Add a new forum")
    ForumResponse addForum(@Valid @RequestBody ForumAddRequest request);

    @Operation(summary = "Update a forum")
    ForumResponse updateForum(@Valid @RequestBody ForumUpdateRequest request);

    @Operation(summary = "Deactivate a forum")
    void deactivateForum(String forumId);
}
