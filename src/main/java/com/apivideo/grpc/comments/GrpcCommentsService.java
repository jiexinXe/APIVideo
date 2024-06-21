package com.apivideo.grpc.comments;

import com.apivideo.entity.Comments;
import com.apivideo.service.CommentsService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcCommentsService extends CommentsServiceGrpc.CommentsServiceImplBase {

    @Autowired
    private CommentsService commentsService;

    @Override
    public void getComment(CommentRequest request, StreamObserver<CommentReply> responseObserver) {
        Comments comment = commentsService.getById(request.getId());
        CommentReply reply = CommentReply.newBuilder()
                .setId(comment.getCommentId())
                .setContent(comment.getContent())
                .setUserId(comment.getUserId())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
