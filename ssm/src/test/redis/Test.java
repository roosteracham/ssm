package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        //Jedis jedis = new Jedis("localhost");
        System.out.println("connected..");

        //jedis.set("st", "sb");

        //System.out.println(jedis.get("st"));

        /*Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }*/

        /*jedis.lpush("l1", "1");
        jedis.lpush("l1", "2");
        jedis.lpush("l1", "3");
        jedis.lpush("l1", "4");

        List<String> list = jedis.lrange("l1", 0, -1);

        for (String l: list) {
            System.out.println(l);
        }*/
        /*Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        pipeline.set("pl", "1");*/
        //pipeline.lpush("pl", "2");
        //int a = 1 / 0;
        //pipeline.lpush("pl", "3");
        //pipeline.exec();
    }
}
