package site.inferno_team.TempLogger.models;


import java.io.Serializable;

public record ResponseHelper<T extends Serializable> (
        String msg,
        int code,
        T data
){

}
