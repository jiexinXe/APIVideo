package com.apivideo.client;

import com.apivideo.entity.Comments;
import com.apivideo.grpc.CommentsProto.CommentRequest;
import com.apivideo.grpc.CommentsProto.CommentResponse;
import com.apivideo.grpc.CommentServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class CommentsClient {

    @GrpcClient("grpc-server")
    private  CommentServiceGrpc.CommentServiceBlockingStub blockingStub;

    public void addCiomment(int VideoId, int UserId, String Content) {
        CommentRequest request = CommentRequest.newBuilder().setVideoId(VideoId)
                .setUserId(UserId)
                .setContent(Content)
                .build();
        CommentResponse response = this.blockingStub.addComment(request);
        System.out.println("Grpc 请求已发送");
    }

    public Comments getComment(int id) {
        CommentRequest request = CommentRequest.newBuilder().setCommentId(id)
                .build();
        CommentRequest response = this.blockingStub.getComment(request);
        Comments comment = new Comments();
        comment.setContent(response.getContent());
        comment.setUserId(response.getUserId());
        comment.setCommentId(response.getCommentId());
        comment.setVideoId(response.getVideoId());
        return comment;
    }

    public int deleteComment(int id) {
        CommentRequest request = CommentRequest.newBuilder().setCommentId(id)
                .build();
        CommentResponse response = this.blockingStub.deleteComment(request);
        return response.getStatus();
    }

    public void updateComment(Comments comments) {
        CommentRequest request = CommentRequest.newBuilder().setCommentId(comments.getCommentId())
                .setUserId(comments.getUserId())
                .setContent(comments.getContent())
                .setVideoId(comments.getVideoId())
                .build();
        CommentResponse response = this.blockingStub.deleteComment(request);
    }
}
