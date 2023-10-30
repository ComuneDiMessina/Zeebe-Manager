package it.almaviva.zeebe.manager.core.exceptions;

public class GitTransactionException extends Exception{
    
    static enum EGitErrorCode {
        COMMIT_FAILED,
        ADD_FAILED,
        LOG_FAILED,
        UNSPECIFIED_ERROR;
    }

    private EGitErrorCode errorCode;

    public GitTransactionException(String message, Throwable cause, EGitErrorCode errorCode){
        super(message, cause);
        this.errorCode = errorCode;
    }

    public GitTransactionException(String message, Throwable cause){
        super(message, cause);
        this.errorCode = EGitErrorCode.UNSPECIFIED_ERROR;
    }
    
    public GitTransactionException(Throwable cause){
        super(cause);
        this.errorCode = EGitErrorCode.UNSPECIFIED_ERROR;
    }
}