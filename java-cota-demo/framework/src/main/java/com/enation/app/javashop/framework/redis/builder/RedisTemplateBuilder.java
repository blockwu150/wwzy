package com.enation.app.javashop.framework.redis.builder;

import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.redis.configure.IRedisBuilder;
import com.enation.app.javashop.framework.redis.configure.JedisSetting;
import com.enation.app.javashop.framework.redis.configure.RedisConnectionConfig;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * redisTemplateBuilder 待优化
 *
 * @author jianghongyan
 * @version v1.0.0
 * @since v1.0.0 2017年4月10日 下午7:52:35
 */
@Component
public class RedisTemplateBuilder {
    private static Logger logger = LoggerFactory.getLogger(RedisTemplateBuilder.class);

    @Autowired
    private List<IRedisBuilder> redisBuilder;

    @Autowired
    private RedisConnectionConfig config;

    /**
     * 构建锁
     */
    private static final Lock LOCK = new ReentrantLock();

    public RedisTemplate<String, Object> build() {


        JedisSetting.loadPoolConfig(config);

        RedisTemplate<String, Object> redisTemplate = null;

        while (true) {
            try {
                LOCK.tryLock(10, TimeUnit.MILLISECONDS);
                if (redisTemplate == null) {

                    IRedisBuilder redisBuilder = this.getRedisBuilder();
                    RedisConnectionFactory lettuceConnectionFactory = redisBuilder.buildConnectionFactory(config);

                    // 初始化连接pool
                    redisTemplate = new RedisTemplate<String, Object>();
                    redisTemplate.setConnectionFactory(lettuceConnectionFactory);
                    redisTemplate.setKeySerializer(new StringRedisSerializer());
                    redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
                    redisTemplate.setEnableTransactionSupport(false);
                    return redisTemplate;
                }
            } catch (Throwable e) {
                // 容错
                logger.error(e.getMessage(), e);

                break;
            } finally {
                LOCK.unlock();
            }
            try {
                // 活锁
                TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return redisTemplate;
    }

    private IRedisBuilder getRedisBuilder() {
        for (IRedisBuilder builder : redisBuilder) {
            if (builder.getType().name().equals(config.getType())) {
                return builder;
            }
        }
        throw new ServiceException(SystemErrorCodeV1.INVALID_CONFIG_PARAMETER, "错误的redis 配置类型，请检查");
    }

}
