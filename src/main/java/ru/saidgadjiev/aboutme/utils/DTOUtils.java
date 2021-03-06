package ru.saidgadjiev.aboutme.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.model.CategoryDetails;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.PostDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public final class DTOUtils {

    private static final ModelMapper INSTANCE = new ModelMapper();

    static {
        INSTANCE.addMappings(new PropertyMap<Category, CategoryDetails>() {
            @Override
            protected void configure() {
                using(new Converter<Category, Integer>() {
                    @Override
                    public Integer convert(MappingContext<Category, Integer> context) {
                        return context.getSource().getPosts().size();
                    }
                }).map(source, destination.getPostsCount());
            }
        });

        INSTANCE.addMappings(new PropertyMap<Comment, CommentDetails>() {
            @Override
            protected void configure() {
                using(new Converter<Comment, String>() {
                    @Override
                    public String convert(MappingContext<Comment, String> context) {
                        return context.getSource().getUser().getNickname();
                    }
                }).map(source, destination.getNickname());
            }
        });

        INSTANCE.addMappings(new PropertyMap<Post, PostDetails>() {
            @Override
            protected void configure() {
                using(new Converter<Post, Integer>() {
                    @Override
                    public Integer convert(MappingContext<Post, Integer> context) {
                        return context.getSource().getComments().size();
                    }
                }).map(source, destination.getCommentsCount());
                using(new Converter<Post, Integer>() {
                    @Override
                    public Integer convert(MappingContext<Post, Integer> context) {
                        return context.getSource().getLikes().size();
                    }
                }).map(source, destination.getLikesCount());
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
