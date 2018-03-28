package ru.saidgadjiev.overtalk.application.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.SourceGetter;
import ru.saidgadjiev.overtalk.application.domain.Comment;
import ru.saidgadjiev.overtalk.application.domain.Post;
import ru.saidgadjiev.overtalk.application.domain.UserProfile;
import ru.saidgadjiev.overtalk.application.domain.UserRole;
import ru.saidgadjiev.overtalk.application.model.CommentDetails;
import ru.saidgadjiev.overtalk.application.model.PostDetails;
import ru.saidgadjiev.overtalk.application.model.UserDetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by said on 04.03.2018.
 */
public class DTOUtils {

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
        INSTANCE.addMappings(new PropertyMap<Post, PostDetails>() {
            @Override
            protected void configure() {
                skip().setCreatedDate(null);
            }
        });
        INSTANCE.addMappings(new PropertyMap<UserProfile, UserDetails>() {
            @Override
            protected void configure() {
                skip().setPassword(null);
                using(userRolesConverter).map(source.getUserRoles(), destination.getRoles());
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
