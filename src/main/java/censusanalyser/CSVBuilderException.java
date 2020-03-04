package censusanalyser;

public class CSVBuilderException extends RuntimeException{
    enum ExceptionType{
        UNABLE_TO_PASS
    }
    CSVBuilderException.ExceptionType type;

    public CSVBuilderException(String message, CSVBuilderException.ExceptionType type){
        super(message);
        this.type=type;
    }
}
