package cn.weChat.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@ToString
@Getter
@Setter
public class UserLoginResponse implements Serializable {

    private static final long serialVersionUID = 1913027305381287193L;

    private User user;

}
