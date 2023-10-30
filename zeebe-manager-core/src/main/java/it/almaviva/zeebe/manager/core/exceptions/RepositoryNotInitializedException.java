package it.almaviva.zeebe.manager.core.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RepositoryNotInitializedException extends Exception{

    static enum EErrorCode {
        INIT_FAILED;
    }

    private EErrorCode errorCode;

    public RepositoryNotInitializedException(String message, Throwable cause, EErrorCode errorCode){
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RepositoryNotInitializedException(String message, Throwable cause){
        super(message, cause);
        this.errorCode = EErrorCode.INIT_FAILED;
    }
    
    public RepositoryNotInitializedException(Throwable cause){
        super(cause);
        this.errorCode = EErrorCode.INIT_FAILED;
    }
}