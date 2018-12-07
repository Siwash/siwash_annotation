package rpf.study.annotation.invoke;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import rpf.study.annotation.DefineAnnotation.Reflect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import rpf.study.annotation.cacheUtils.SimulateCacheUtils;
import rpf.study.annotation.cglib.MyCglibPoxy;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Component
public class MyListenerProcessor implements BeanPostProcessor {

    @Resource
    SimulateCacheUtils simulateCacheUtils;
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }
/**
 * 若bean为代理后的对象（结尾含$符号），直接用反射获取不到注解，解决办法：
 * 1.使用ReflectionUtil获得的bean注解不会丢失。这里返回的直接是没代理前的bean的Method。
 * 2.使用AnnotationUtils.findAnnotation可以读取到已代被理类的注解，实际上是扫描代理类原来的class的注解。
 * **/
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        Method [] methods= ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        System.out.println("【BeanPostProcessor】：正在初始化："+s);
        if (methods!=null){
            for (Method method : methods) {
                if (method.isAnnotationPresent(Reflect.class)){
                    System.out.println("【BeanPostProcessor】当前class:"+bean.getClass().getName());
                    Reflect autoCache=method.getAnnotation(Reflect.class);
                    if (autoCache!=null){
                        return new MyCglibPoxy(simulateCacheUtils).getInstance(bean);
                    }
                }

            }
        }


        return bean;
    }
}
