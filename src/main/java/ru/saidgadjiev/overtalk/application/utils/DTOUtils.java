package ru.saidgadjiev.overtalk.application.utils;

import ru.saidgadjiev.overtalk.application.model.dao.Comment;
import ru.saidgadjiev.overtalk.application.model.dao.Post;
import ru.saidgadjiev.overtalk.application.model.web.CommentDetails;
import ru.saidgadjiev.overtalk.application.model.web.PostDetails;

/**
 * Created by said on 04.03.2018.
 */
public class DTOUtils {

    private DTOUtils() {}

    public static PostDetails convert(Post post) {
        PostDetails postDetails = new PostDetails();

        postDetails.setId(post.getId());
        postDetails.setTitle(post.getTitle());
        postDetails.setContent(post.getContent());
        postDetails.setCreatedDate(post.getCreatedDate());

        for (Comment comment: post.getComments()) {
            postDetails.getComments().add(convert(comment));
        }

        return postDetails;
    }

    public static CommentDetails convert(Comment comment) {
        CommentDetails commentDetails = new CommentDetails();

        commentDetails.setId(comment.getId());
        commentDetails.setContent(comment.getContent());
        commentDetails.setCreatedDate(comment.getCreatedDate());

        return commentDetails;
    }
}
