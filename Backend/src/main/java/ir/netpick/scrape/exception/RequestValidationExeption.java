package ir.netpick.scrape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RequestValidationExeption extends RuntimeException{
    public RequestValidationExeption(String message){
        super(message);
    }
}
