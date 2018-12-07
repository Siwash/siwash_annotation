package rpf.study.annotation.service;

import org.springframework.stereotype.Service;
import rpf.study.annotation.DefineAnnotation.AutoCache;
import rpf.study.annotation.DefineAnnotation.Reflect;

@Service
public class UserService {

    @AutoCache
    public String shit(){
        System.out.println("shit^shit^shit");
        return "200";
    }
    @Reflect
    public String eat(){
        System.out.println("chi^chi^chi");
        return "300";
    }

}
