package handlers;

public class handlerException extends Exception{
    public String errorMessage;
    public String errorCode;

    public void setErrorMessage(String message){
        this.errorMessage = message;
    }

    public void setErrorCode(String code){
        this.errorCode = code;
    }
}
