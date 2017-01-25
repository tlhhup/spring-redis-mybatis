package com.tlh.ssmr.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

public class SystemRedisCache implements Cache {

	/**
	 * redis 模板
	 */
	private RedisTemplate<String, Object> redisTemplate;
	private String cacheName=this.getClass().getName();
	private long timeOut;
	
	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#getName()
	 */
	@Override
	public String getName() {
		return this.cacheName;
	}

	@Override
	public Object getNativeCache() {
		//设置缓存的提供商
		return this.redisTemplate;
	}

	@Override
	public ValueWrapper get(Object key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}else{
			final String finalKey;
			if(key instanceof String){
				finalKey=(String) key;
			}else{
				finalKey=key.toString();
			}
			Object value = redisTemplate.execute(new RedisCallback<Object>() {
				
				/* (non-Javadoc)
				 * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.springframework.data.redis.connection.RedisConnection)
				 */
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] key = finalKey.getBytes();
					byte[] value = connection.get(key);
					if(value==null){
						return null;
					}
					return SerializableUtil.unserialize(value);
				}
				
			});
			return value!=null?new SimpleValueWrapper(value):null;
		}
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		if (StringUtils.isEmpty(key) || null == type) {
            return null;
        } else {
            final String finalKey;
            if (key instanceof String) {
                finalKey = (String) key;
            } else {
                finalKey = key.toString();
            }
            Object value = redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] key = finalKey.getBytes();
                    byte[] value = connection.get(key);
                    if (value == null) {
                        return null;
                    }
                    return SerializableUtil.unserialize(value);
                }
            });
            if (type != null && type.isInstance(value) && null != value) {
                return (T) value;
            } else {
                return null;
            }
        }
	}

	@Override
	public void put(Object key, Object value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        } else {
            final String finalKey;
            if (key instanceof String) {
                finalKey = (String) key;
            } else {
                finalKey = key.toString();
            }
            if (!StringUtils.isEmpty(finalKey)) {
                final Object finalValue = value;
                redisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection connection) {
                        connection.set(finalKey.getBytes(), SerializableUtil.serialize(finalValue));
                        // 设置超时间
                        connection.expire(finalKey.getBytes(), timeOut);
                        return true;
                    }
                });
            }
        }
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		ValueWrapper valueWrapper = get(key);
		if(valueWrapper==null){
			put(key, value);
			return null;
		}else{
			return valueWrapper;
		}
	}

	@Override
	public void evict(Object key) {
		if (null != key) {
            final String finalKey;
            if (key instanceof String) {
                finalKey = (String) key;
            } else {
                finalKey = key.toString();
            }
            if (!StringUtils.isEmpty(finalKey)) {
                redisTemplate.execute(new RedisCallback<Long>() {
                    public Long doInRedis(RedisConnection connection) throws DataAccessException {
                        return connection.del(finalKey.getBytes());
                    }
                });
            }
        }
	}

	@Override
	public void clear() {
		redisTemplate.execute(new RedisCallback<String>() {
			/* (non-Javadoc)
			 * @see org.springframework.data.redis.core.RedisCallback#doInRedis(org.springframework.data.redis.connection.RedisConnection)
			 */
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "success";
			}
		});
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

}
