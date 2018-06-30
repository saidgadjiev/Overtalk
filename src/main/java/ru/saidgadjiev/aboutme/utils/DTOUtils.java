package ru.saidgadjiev.aboutme.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.aboutme.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by said on 04.03.2018.
 */
public final class DTOUtils {

    private static final ModelMapper INSTANCE = new ModelMapper();

    static {
        Converter<Set<UserRole>, Set<String>> userRolesConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return context.getSource().stream().map(userRole -> {
                if (userRole.getRole() == null) {
                    return null;
                }
                return userRole.getRole().getName();
            }).collect(Collectors.toSet());
        };
        Converter<UserProfile, String> userNickNameConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return context.getSource().getNickName();
        };
        Converter<Post, Integer> postLikesCountConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return context.getSource().getLikes().size();
        };
        Converter<Post, Integer> commentCountConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return context.getSource().getComments().size();
        };
        Converter<Post, List<String>> postLikeUsersConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return context.getSource().getLikes()
                    .stream()
                    .map(like -> like.getUser().getUserName())
                    .collect(Collectors.toList());
        };
        Converter<LikeDetails, Post> likeDetailsPostConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            Post post = new Post();

            post.setId(context.getSource().getPostId());

            return post;
        };
        Converter<LikeDetails, Comment> likeDetailsCommentConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            Comment comment = new Comment();

            comment.setId(context.getSource().getCommentId());

            return comment;
        };
        Converter<String, UserProfile> userNameConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            UserProfile userProfile = new UserProfile();

            userProfile.setUserName(context.getSource());

            return userProfile;
        };
        INSTANCE.addMappings(new PropertyMap<Comment, CommentDetails>() {
            @Override
            protected void configure() {
                using(userNickNameConverter).map(source.getUser(), destination.getNickName());
            }
        });
        INSTANCE.addMappings(new PropertyMap<UserProfile, UserDetails>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
                using(userRolesConverter).map(source.getUserRoles(), destination.getRoles());
            }
        });
        INSTANCE.addMappings(new PropertyMap<PostDetails, Post>() {
            @Override
            protected void configure() {
                skip(destination.getCreatedDate());
                using(userNameConverter).map(source.getUserName(), destination.getUser());
            }
        });
        INSTANCE.addMappings(new PropertyMap<CommentDetails, Comment>() {
            @Override
            protected void configure() {
                skip(destination.getCreatedDate());
                using(userNameConverter).map(source.getUser(), destination.getUser());
            }
        });
        INSTANCE.addMappings(new PropertyMap<ProjectDetails, Project>() {
            @Override
            protected void configure() {
                skip(destination.getCreatedDate());
            }
        });
        INSTANCE.addMappings(new PropertyMap<Post, PostDetails>() {
            @Override
            protected void configure() {
                skip(destination.getUserName());
                using(userNickNameConverter).map(source.getUser(), destination.getNickName());
                using(commentCountConverter).map(source, destination.getCommentsCount());
                using(postLikesCountConverter).map(source, destination.getLikesCount());
                using(postLikeUsersConverter).map(source, destination.getLikeUsers());
            }
        });
        INSTANCE.addMappings(new PropertyMap<LikeDetails, Like>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                using(likeDetailsCommentConverter).map(source, destination.getComment());
                using(likeDetailsPostConverter).map(source, destination.getPost());
                using(userNameConverter).map(source.getUser(), destination.getUser());
            }
        });
    }

    private DTOUtils() { }

    public static<S, D> List<D> convert(List<S> objects, Class<D> targetClass) {
        List<D> result = new ArrayList<>();

        for (S object: objects) {
            result.add(INSTANCE.map(object, targetClass));
        }

        return result;
    }

    public static<S, D> D convert(S object, Class<D> targetClass) {
        return INSTANCE.map(object, targetClass);
    }
}
