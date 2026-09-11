package com.SIMCHAT_A.SIMCHAT_A.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SseEmitterService {

    // => room emitter map
    private final Map<String, CopyOnWriteArrayList<SseEmitter>> roomEmitters = new ConcurrentHashMap<>();

    // => subscribe sse stream
    public SseEmitter subscribe(String roomId) {
        SseEmitter emitter = new SseEmitter(0L);
        roomEmitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(roomId, emitter));
        emitter.onTimeout(() -> removeEmitter(roomId, emitter));
        emitter.onError((e) -> removeEmitter(roomId, emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Subscribed to real-time chat room: " + roomId));
        } catch (IOException e) {
            removeEmitter(roomId, emitter);
        }

        return emitter;
    }

    // => broadcast msg to room sse emitters
    public void broadcast(String roomId, String messageJson) {
        CopyOnWriteArrayList<SseEmitter> emitters = roomEmitters.get(roomId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("MESSAGE")
                            .data(messageJson));
                } catch (Exception e) {
                    removeEmitter(roomId, emitter);
                }
            }
        }
    }

    // => remove sse emitter
    private void removeEmitter(String roomId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> emitters = roomEmitters.get(roomId);
        if (emitters != null) {
            emitters.remove(emitter);
        }
    }
}
