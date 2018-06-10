package ru.saidgadjiev.aboutme.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.model.UserDetails;

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
        Converter<Post, Integer> commentCountConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }

            return context.getSource().getComments().size();
        };
        INSTANCE.addMappings(new PropertyMap<Comment, CommentDetails>() {
            @Override
            protected void configure() {
                using(userNickNameConverter).map(source.getUser(), destination.getNickName());
            }
        });
        INSTANCE.addMappings(new PropertyMap<Post, PostDetails>() {
            @Override
            protected void configure() {
                using(userNickNameConverter).map(source.getUser(), destination.getNickName());
            }
        });
        INSTANCE.addMappings(new PropertyMap<Post, PostDetails>() {
            @Override
            protected void configure() {
                using(commentCountConverter).map(source, destination.getCommentsCount());
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
            }
        });
        INSTANCE.addMappings(new PropertyMap<CommentDetails, Comment>() {
            @Override
            protected void configure() {
                skip(destination.getCreatedDate());
            }
        });
        INSTANCE.addMappings(new PropertyMap<ProjectDetails, Project>() {
            @Override
            protected void configure() {
                skip(destination.getCreatedDate());
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
