package com.lishiyan.listener;


import com.lishiyan.api.GoodsClient;
import com.lishiyan.service.IndexService;
import com.shop.bean.Spu;
import org.elasticsearch.search.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsClient client;

    /**
     * 处理insert和update的消息
     *
     * @param id
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.create.index.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))
        public void listenCreate(Long id) throws Exception{
        if(id==null){
            return;
        }
        Spu spu = client.querySpuById(id).getBody();
        indexService.buildGoods(spu);

    }



}
