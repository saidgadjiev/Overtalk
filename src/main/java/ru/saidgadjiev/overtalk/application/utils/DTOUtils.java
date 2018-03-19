package ru.saidgadjiev.overtalk.application.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import ru.saidgadjiev.overtalk.application.domain.Comment;
import ru.saidgadjiev.overtalk.application.domain.Post;
import ru.saidgadjiev.overtalk.application.model.CommentDetails;
import ru.saidgadjiev.overtalk.application.model.PostDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public class DTOUtils {

    private static final ModelMapper INSTANCE = new ModelMapper();

    static {
        INSTANCE.addMappings(new PropertyMap<PostDetails, Post>() {
            @Override
            protected void configure() {
                skip().setCreatedDate(null);
            }
        });
    }

    private DTOUtils() {}

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
