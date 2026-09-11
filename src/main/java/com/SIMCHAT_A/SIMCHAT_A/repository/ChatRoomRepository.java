package com.SIMCHAT_A.SIMCHAT_A.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // => REDIS KEY PATTERNS
    private static final String ROOM_KEY_PREFIX = "chatroom:";
    private static final String PARTICIPANTS_SUFFIX = ":participants";
    private static final String ALL_ROOMS_KEY = "chatrooms:all";

    // => check if room exist
    public boolean exists(String roomId) {
        Boolean isMember = redisTemplate.opsForSet().isMember(ALL_ROOMS_KEY, roomId);
        return Boolean.TRUE.equals(isMember);
    }

    // => create room in redis
    public boolean saveRoomAtomic(String roomId, String roomName) {
        Long added = redisTemplate.opsForSet().add(ALL_ROOMS_KEY, roomId);
        if (added == null || added == 0) {
            return false;
        }

        String roomKey = ROOM_KEY_PREFIX + roomId;
        Map<String, String> roomData = new HashMap<>();
        roomData.put("roomId", roomId);
        roomData.put("roomName", roomName);
        roomData.put("createdAt", Instant.now().toString());

        redisTemplate.opsForHash().putAll(roomKey, roomData);
        return true;
    }

    // added participants
    public void addParticipant(String roomId, String participant) {
        String key = ROOM_KEY_PREFIX + roomId + PARTICIPANTS_SUFFIX;
        redisTemplate.opsForSet().add(key, participant);
    }

    // => check if participant in room
    public boolean isParticipantInRoom(String roomId, String participant) {
        String key = ROOM_KEY_PREFIX + roomId + PARTICIPANTS_SUFFIX;
        Boolean isMember = redisTemplate.opsForSet().isMember(key, participant);
        return Boolean.TRUE.equals(isMember);
    }

    // => get all cure context switching
    public Set<String> getParticipants(String roomId) {
        String key = ROOM_KEY_PREFIX + roomId + PARTICIPANTS_SUFFIX;
        return redisTemplate.opsForSet().members(key);
    }

    public boolean deleteRoom(String roomId) {
        if (!exists(roomId)) {
            return false;
        }
        redisTemplate.opsForSet().remove(ALL_ROOMS_KEY, roomId);
        redisTemplate.delete(ROOM_KEY_PREFIX + roomId);
        redisTemplate.delete(ROOM_KEY_PREFIX + roomId + PARTICIPANTS_SUFFIX);
        redisTemplate.delete(ROOM_KEY_PREFIX + roomId + ":messages");
        return true;
    }
}