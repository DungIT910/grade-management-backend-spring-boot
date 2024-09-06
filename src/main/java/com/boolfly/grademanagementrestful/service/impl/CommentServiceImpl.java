package com.boolfly.grademanagementrestful.service.impl;

import com.boolfly.grademanagementrestful.api.dto.comment.CommentAddRequest;
import com.boolfly.grademanagementrestful.api.dto.comment.CommentUpdateRequest;
import com.boolfly.grademanagementrestful.domain.Comment;
import com.boolfly.grademanagementrestful.domain.CommentRelation;
import com.boolfly.grademanagementrestful.domain.Post;
import com.boolfly.grademanagementrestful.exception.comment.CommentNotFoundException;
import com.boolfly.grademanagementrestful.exception.post.PostNotFoundException;
import com.boolfly.grademanagementrestful.repository.CommentRelationRepository;
import com.boolfly.grademanagementrestful.repository.CommentRepository;
import com.boolfly.grademanagementrestful.repository.PostRepository;
import com.boolfly.grademanagementrestful.service.CommentService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentRelationRepository commentRelationRepository;

    @Override
    public Comment addCommentToPost(CommentAddRequest request, String postId) {
        Long postLongId = TSID.from(postId).toLong();

        Post post = postRepository.findById(postLongId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment newComment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .build();

        commentRepository.save(newComment);

        commentRelationRepository.save(
                CommentRelation.builder()
                        .ancestor(newComment)
                        .descendant(newComment)
                        .depth(0)
                        .build()
        );

        return newComment;
    }

    @Override
    @Transactional
    public Comment addReplytoComment(CommentAddRequest request, Long ancestorId) {
        Comment immediateAncestor = commentRepository.findById(ancestorId)
                .orElseThrow(CommentNotFoundException::new);

        Comment newComment = Comment.builder()
                .content(request.getContent())
                .post(immediateAncestor.getPost())
                .build();

        commentRepository.save(newComment);

        List<CommentRelation> newAncestorRelations = commentRelationRepository.findAllByDescendant_Id(ancestorId)
                .stream()
                .map(ancestorRelation -> CommentRelation.builder()
                        .ancestor(ancestorRelation.getAncestor())
                        .descendant(newComment)
                        .depth(ancestorRelation.getDepth() + 1)
                        .build()
                )
                .collect(Collectors.toList());

        newAncestorRelations.add(CommentRelation.builder()
                .ancestor(newComment)
                .descendant(newComment)
                .depth(0)
                .build());

        commentRelationRepository.saveAll(newAncestorRelations);

        return newComment;
    }

    @Override
    public Comment updateComment(CommentUpdateRequest request, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        commentRepository.deleteAll(
                comment.getDescendantRelations()
                        .stream()
                        .map(CommentRelation::getDescendant)
                        .collect(Collectors.toSet())
        );
        commentRepository.delete(comment);
    }

    @Override
    public Page<Comment> getCommentsByPostId(int page, int size, String postId) {
        Long postLongId = TSID.from(postId).toLong();

        return commentRepository.findAllRootCommentsByPost_Id(PageRequest.of(page, size), postLongId);
    }

    @Override
    public Page<Comment> getImmediateReplies(int page, int size, Long commentId) {
        return commentRepository.findAllImmediateDescendants(PageRequest.of(page, size), commentId);
    }
}
