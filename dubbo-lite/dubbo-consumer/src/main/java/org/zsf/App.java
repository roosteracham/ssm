package org.zsf;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zsf.service.GreetingService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(new String[]{"classpath:consumer.xml"});
        context.start();
        GreetingService greetService = context.getBean("greetService", GreetingService.class);
        String ret = greetService.sayHi("mhl");
        System.out.println(ret);
    }
}
