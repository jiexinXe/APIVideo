package com.apivideo.grpc;

import com.apivideo.grpc.CommentServiceGrpc;
import com.apivideo.grpc.CommentsProto.CommentRequest;
import com.apivideo.grpc.CommentsProto.CommentResponse;
import com.apivideo.entity.Comments;
import com.apivideo.service.CommentsService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends CommentServiceGrpc.CommentServiceImplBase {

    @Autowired
    private CommentsService commentsService;

    @Override
    public void addComment(CommentRequest request, StreamObserver<CommentResponse> responseObserver) {
        Comments comment = new Comments();
        comment.setVideoId(request.getVideoId());
        comment.setUserId(request.getUserId());
        comment.setContent(request.getContent());

        commentsService.save(comment);

        CommentResponse response = CommentResponse.newBuilder()
                .setStatus(0)
                .setMessage("Comment added successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getComment(CommentRequest request, StreamObserver<CommentRequest> responseObserver) {
        Comments comment = commentsService.getById(request.getCommentId());
        if (comment != null) {
            CommentRequest response = CommentRequest.newBuilder()
                    .setCommentId(comment.getCommentId())
                    .setVideoId(comment.getVideoId())
                    .setUserId(comment.getUserId())
                    .setContent(comment.getContent())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Comment not found"));
        }
    }

    @Override
    public void deleteComment(CommentRequest request, StreamObserver<CommentResponse> responseObserver) {
        boolean deleted = commentsService.removeById(request.getCommentId());
        CommentResponse response = CommentResponse.newBuilder()
                .setStatus(deleted ? 0 : 1)
                .setMessage(deleted ? "Comment deleted successfully" : "Comment deletion failed")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateComment(CommentRequest request, StreamObserver<CommentResponse> responseObserver) {
        Comments comment = commentsService.getById(request.getCommentId());
        if (comment != null) {
            comment.setContent(request.getContent());
            commentsService.updateById(comment);

            CommentResponse response = CommentResponse.newBuilder()
                    .setStatus(0)
                    .setMessage("Comment updated successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Comment not found"));
        }
    }
}
