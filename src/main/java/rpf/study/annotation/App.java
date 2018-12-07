package rpf.study.annotation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rpf.study.annotation.service.UserService;

public class App {
    public static void main(String[] args) {
        BeanFactory beanFactory=new ClassPathXmlApplicationContext("application.xml");
        UserService userService= (UserService) beanFactory.getBean("userService");
        for (int i = 0; i < 5; i++) {
            String var1= userService.shit();
            System.out.println("【UserService】[shit()]方法运行结果："+var1);
        }
        System.out.println("--------------------------------");
        for (int i = 0; i < 5; i++) {
        System.out.println("【UserService】[eat()]方法运行结果："+userService.eat());
        }

        System.out.println("【APP】最终的UserService= "+userService.getClass().getName());
    }
}
