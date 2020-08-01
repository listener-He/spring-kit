package org.hehh.cloud.cache.topic;

import org.hehh.cloud.cache.MoreCacheManager;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;


/**
 * @author: HeHui
 * @date: 2020-07-31 17:14
 * @description: redis cache topic 实现
 */
public  class RedisCacheTopicAdapter extends RedisMessageListenerContainer implements CacheTopicAdapter {




    /**
     *  topic拼接符
     */
    private final String TOPIC_PREFIX = "^";
    private final String TOPIC_SUFFIX = "$";
    private final String TOPIC_JOIN = "|";


    /**
     *  redis 实现 缓存topic
     *
     * @param valueSerializer   值序列化器
     * @param connectionFactory 连接工厂
     * @param cacheManager      缓存管理器
     * @param topicName         主题名称
     */
    public RedisCacheTopicAdapter(RedisSerializer<?> valueSerializer, RedisConnectionFactory connectionFactory, MoreCacheManager cacheManager,String... topicName) {

        super();
        assert valueSerializer != null : "redis 序列化不能为空";
        assert connectionFactory != null : "redis 连接工厂不能为空";
        assert cacheManager != null : "二级缓存对象不能为空";


        this.setConnectionFactory(connectionFactory);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < topicName.length; i++) {
            sb.append(TOPIC_PREFIX).append(topicName[i]).append(TOPIC_SUFFIX);
            if(i < (topicName.length -1)){
                sb.append(TOPIC_JOIN);
            }
        }

        this.addMessageListener(new CacheTopicRedisMessageListenerAdapter(cacheManager,valueSerializer),new PatternTopic(sb.toString()));

    }







    /**
     *  topic-redis监听器适配
     */
    static class CacheTopicRedisMessageListenerAdapter extends MessageListenerAdapter {


        private final MoreCacheManager cacheManager;

        private  RedisSerializer<?> valueSerializer;


        public CacheTopicRedisMessageListenerAdapter(MoreCacheManager cacheManager,RedisSerializer<?> valueSerializer){
            assert valueSerializer != null : "redis-value 序列化不能为空";
            this.valueSerializer = valueSerializer;
            this.cacheManager = cacheManager;
        }




        /**
         * Standard Redis {@link MessageListener} entry point.
         * <p>
         * Delegates the message to the target listener method, with appropriate conversion of the message argument. In case
         * of an exception, the {@link #handleListenerException(Throwable)} method will be invoked.
         *
         * @param message the incoming Redis message
         * @param pattern
         * @see #handleListenerException
         */
        @Override
        public void onMessage(Message message, byte[] pattern) {
            if(message != null){

                try {
                    CacheNotice notice =   (CacheNotice)valueSerializer.deserialize(message.getBody());
                    if(notice != null && null != cacheManager && notice.getLevel() > 1){
                        this.cacheManager.getCacheManager(notice.getLevel() - 1).ifPresent(manager -> {
                            if(StringUtils.hasText(notice.getCacheName())
                                && manager.getCache(notice.getCacheName()) != null){
                                Cache cache = manager.getCache(notice.getCacheName());

                                switch (notice.getType()){
                                    case PUT:
                                       this.cacheManager.getCacheManager(notice.getLevel()).ifPresent(two -> {
                                           Cache.ValueWrapper wrapper = two.getCache(notice.getCacheName()).get(notice.getKey());
                                           if(wrapper != null){
                                               cache.put(notice.getKey(),wrapper.get());
                                           }
                                       });
                                        break;
                                    case EVICT:
                                        cache.evict(notice.getKey());
                                        break;
                                    case CLEAR:
                                        cache.clear();
                                        break;
                                    default:
                                        break;
                                }
                            }

                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}
