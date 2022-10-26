package yumyum.demo.src.community.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yumyum.demo.src.user.entity.UserEntity;

import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostForm extends BaseFrom{

    private String imgUrl;

    private String topic;

    private String contents;

}
