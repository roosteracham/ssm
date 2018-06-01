package test.junit;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:resources/spring/spring-*.xml"})
public class BaseJunitTest extends AbstractTransactionalJUnit4SpringContextTests{

}
