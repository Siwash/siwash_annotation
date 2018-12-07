package rpf.study.annotation.cglib;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import rpf.study.annotation.DefineAnnotation.Reflect;
import rpf.study.annotation.cacheUtils.SimulateCacheUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
@Component
public class MyCglibPoxy implements MethodInterceptor {
    //@Autowired 注解不会起作用
    SimulateCacheUtils simulateCacheUtils;

    private  Object target;

    public MyCglibPoxy(SimulateCacheUtils simulateCacheUtils) {
        this.simulateCacheUtils = simulateCacheUtils;
    }


    public  Object getInstance(Object target){
        this.target=target;
        Enhancer enhancer=new Enhancer();
        System.out.println("【cglib】--【oldClassName】:"+target.getClass().getName());
        int proxyClassIndex=target.getClass().getName().indexOf("$");
        String realClassName=proxyClassIndex==-1? target.getClass().getName():target.getClass().getName().substring(0,proxyClassIndex);
        System.out.println("【cglib】--【realClassName】:"+realClassName);
        try {
            enhancer.setSuperclass(Class.forName(realClassName));
            enhancer.setCallback(this);
            return enhancer.create();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Object intercept(Object bean, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.isAnnotationPresent(Reflect.class)){
            Class type = target.getClass();
            int proxyClassIndex=target.getClass().getName().indexOf("$");
            String realClassName=proxyClassIndex==-1? target.getClass().getName():target.getClass().getName().substring(0,proxyClassIndex);
            String key=realClassName.concat(".").concat(method.getName());
            System.out.println("【cglib】拦截到带@Reflect注解的方法:"+method.getName());
            if (method.isAnnotationPresent(Reflect.class)){
                if (simulateCacheUtils.isCached(key)){
                    System.out.println("【cglib】直接从缓存中读取数据");
                    return simulateCacheUtils.getCache(key);
                }else {
                    System.out.println("【cglib】缓存里面没有数据，运行方法："+method.getName());
                    Object result=method.invoke(target,objects);
                    simulateCacheUtils.writeCache(key,result);
                    return  result;
                }
            }
        }



        return method.invoke(target,objects);
    }
}
