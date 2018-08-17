package ru.saidgadjiev.aboutme.utils;

import javafx.geometry.Pos;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
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
                        return context.getSource().getUser().getNickName();
                    }
                }).map(source, destination.getNickName());
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
                });
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
