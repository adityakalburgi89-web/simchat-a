package com.SIMCHAT_A.SIMCHAT_A.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final RedisTemplate<String, String> redisTemplate;
    // => REDIS KEY PATTERNS
    private static final String ROOM_KEY_PREFIX = "chatroom";
    private static final String PARTICIPANTS_SUFFIX = ":participants";
    private static final String ALL_ROOM_KEY = "chatroom : all";

    // => check if room exist
    public boolean exist(String roomId) {
        Boolean exist = redisTemplate.hasKey(roomId);
        return Boolean.TRUE.equals(exist);
    }

    // => create room in redis
    public void saveRoom(String roomId, String name) {
        String roomKey = roomId;
        redisTemplate.opsForHash().put(roomKey, "name", name);
        redisTemplate.opsForHash().put(roomKey, "createdAt", java.time.Instant.now().toString());
        redisTemplate.opsForSet().add(ALL_ROOM_KEY, roomId);

    }
    // added participants

    public void addParticipants(String roomId, String username) {
        String participantKey = roomId + PARTICIPANTS_SUFFIX;
        redisTemplate.opsForSet().add(participantKey, username);
    }

    // => check if participant in room
    public boolean isParticipantinRoom(String roomId, String username) {
        String participantKey = roomId + PARTICIPANTS_SUFFIX;
        Boolean isMember = redisTemplate.opsForSet().isMember(participantKey, username);
        return Boolean.TRUE.equals(isMember);
    }

    // => get all cure context switching
    public Set<String> getParticipant(String roomId) {
        String participantKey = roomId + PARTICIPANTS_SUFFIX;
        return redisTemplate.opsForSet().members(participantKey);
    }

}