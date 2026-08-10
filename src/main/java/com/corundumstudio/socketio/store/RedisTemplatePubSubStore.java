/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.corundumstudio.socketio.store;

import com.corundumstudio.socketio.store.pubsub.PubSubListener;
import com.corundumstudio.socketio.store.pubsub.PubSubMessage;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import com.corundumstudio.socketio.store.pubsub.PubSubType;
import io.netty.util.internal.PlatformDependent;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * Redis-backed {@link PubSubStore} implemented on top of Spring's
 * {@link RedisTemplate} and {@link RedisMessageListenerContainer}.
 *
 * <p>Publishes Socket.IO pub/sub messages to channels keyed by their
 * {@link PubSubType} and registers listeners that are invoked when matching messages
 * arrive. The originating node id is attached to every published message so that
 * subscribers can filter messages emitted by themselves if needed.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class RedisTemplatePubSubStore implements PubSubStore {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final RedisMessageListenerContainer listenerContainer;
    private final Long nodeId;

    private final ConcurrentMap<String, Queue<MessageListener>> map = PlatformDependent.newConcurrentHashMap();

    /**
     * Construct a pub/sub store backed by the given Redis template and listener
     * container.
     * @param redisTemplate the Redis template used to publish messages
     * @param listenerContainer the listener container used to subscribe to channels
     * @param nodeId the unique id of the publishing node
     */
    public RedisTemplatePubSubStore(RedisTemplate<Object, Object> redisTemplate,
                                    RedisMessageListenerContainer listenerContainer,
                                    Long nodeId) {
        this.redisTemplate = redisTemplate;
        this.listenerContainer = listenerContainer;
        this.nodeId = nodeId;
    }

    /**
     * Publish a pub/sub message to the channel identified by its type, tagging the
     * message with the current node id.
     * @param type the pub/sub message type (channel name)
     * @param msg the message to publish
     */
    @Override
    public void publish(PubSubType type, PubSubMessage msg) {
        msg.setNodeId(nodeId);
        redisTemplate.convertAndSend(type.toString(), msg);
    }

    /**
     * Subscribe to messages of the given type, forwarding them to the supplied
     * listener.
     * @param type the pub/sub message type (channel name)
     * @param listener the listener notified on each incoming message
     * @param clazz the expected message payload type
     * @param <T> the pub/sub message type
     */
    @Override
    public <T extends PubSubMessage> void subscribe(PubSubType type, final PubSubListener<T> listener, Class<T> clazz) {
        String name = type.toString();

        MessageListener msgListener = (message, pattern) -> {
            PubSubMessage msg = (PubSubMessage) message;
            //if (!nodeId.equals(msg.getNodeId())) {
                listener.onMessage((T) msg);
           // }
        };
        listenerContainer.addMessageListener(msgListener, new ChannelTopic(name));

        Queue<MessageListener> list = map.get(name);
        if (list == null) {
            list = new ConcurrentLinkedQueue<MessageListener>();
            Queue<MessageListener> oldList = map.putIfAbsent(name, list);
            if (oldList != null) {
                list = oldList;
            }
        }
        list.add(msgListener);
    }

    /**
     * Unsubscribe all listeners registered for the given pub/sub type.
     * @param type the pub/sub message type (channel name) to unsubscribe from
     */
    @Override
    public void unsubscribe(PubSubType type) {
        String name = type.toString();
        Queue<MessageListener> regListeners = map.remove(name);
        for (MessageListener listener : regListeners) {
        	 listenerContainer.removeMessageListener(listener);
        }
    }

    /**
     * Release any resources held by this store; the RedisTemplate-based implementation
     * has nothing to release so this is a no-op.
     */
    @Override
    public void shutdown() {
    }

}
