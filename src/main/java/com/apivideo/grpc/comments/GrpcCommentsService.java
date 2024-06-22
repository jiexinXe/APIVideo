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
        try {
            System.out.println("Received getComment request: " + request.getId());
            Comments comment = commentsService.getById(request.getId());
            if (comment != null) {
                CommentReply reply = CommentReply.newBuilder()
                        .setId(comment.getCommentId())
                        .setContent(comment.getContent())
                        .setUserId(comment.getUserId())
                        .build();
                responseObserver.onNext(reply);
            } else {
                responseObserver.onError(new Exception("Comment not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }
}
