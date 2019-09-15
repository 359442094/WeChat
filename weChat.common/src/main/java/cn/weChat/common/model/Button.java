package cn.weChat.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Button {

    private String name;

    private String type;

    private Button[] sub_button;

}
