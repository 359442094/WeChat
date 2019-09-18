package cn.weChat.common;

public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String errorCode,String message) {
        super(errorCode+message);
    }

}
