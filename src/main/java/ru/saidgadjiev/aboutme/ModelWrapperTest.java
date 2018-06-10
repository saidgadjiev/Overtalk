package ru.saidgadjiev.aboutme;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.model.PostDetails;

/**
 * Created by said on 19.03.2018.
 */
public class ModelWrapperTest {

    public static void main(String[] args) {
        PostDetails postDetails = new PostDetails();

        postDetails.setContent("test content");
        postDetails.setTitle("test title");
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<PostDetails, Post>() {
            @Override
            protected void configure() {
                skip().setCreatedDate(null);
            }
        });

        System.out.println(modelMapper.map(postDetails, Post.class));
    }
}
