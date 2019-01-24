package com.ocean;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Ocean on 2019/1/24 16:43.
 */
@Component
public class MessageConsumer {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "springboot.queue",durable = "true"),
                    exchange = @Exchange(value = "springboot.exchange", durable = "true" , type = "topic"),
                    key = "#"
            )
    )

    @RabbitHandler  //代表下面的方法用于接收消息，这个方法运行后将处于等待的状态，有新的消息进来，则自动调用下面的方法进行处理
    //@Payload 获取的消息反序列化为一个对象，用employee引用
    public void handleMessage(@Payload Employee employee, Channel channel ,
                              @Headers Map<String,Object> headers){
        System.out.println("===========================");
        System.out.println("接收到：" + employee.getEmpno()+":" + employee.getName());
        Long tag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("===========================");

    }
}
