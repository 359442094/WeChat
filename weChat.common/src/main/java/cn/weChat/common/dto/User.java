package cn.weChat.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@ToString
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = -280492545073068159L;

    private String username;

    private String password;

}
