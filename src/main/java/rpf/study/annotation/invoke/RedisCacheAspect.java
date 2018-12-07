package rpf.study.annotation.invoke;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import rpf.study.annotation.cacheUtils.SimulateCacheUtils;

import javax.annotation.Resource;

@Component
@Aspect
public class RedisCacheAspect {


    @Resource
    SimulateCacheUtils simulateCacheUtils;

    @Pointcut("@annotation(rpf.study.annotation.DefineAnnotation.AutoCache)")
    public  void setJoinPoint(){}
    @Around(value = "setJoinPoint()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("【AOP】拦截到带@AutoCache注解的方法:"+joinPoint.getSignature().getName());
        String key=joinPoint.getTarget().getClass().toString().concat(".").concat(joinPoint.getSignature().getName());
        if (simulateCacheUtils.isCached(key)){
            System.out.println("【AOP】直接从缓存中读取数据");
            return simulateCacheUtils.getCache(key);
        }else {
            System.out.println("【AOP】缓存里面没有数据，运行方法："+joinPoint.getSignature().getName());
            Object result=joinPoint.proceed(joinPoint.getArgs());
            simulateCacheUtils.writeCache(key,result);
            return  result;
        }
    }
}
