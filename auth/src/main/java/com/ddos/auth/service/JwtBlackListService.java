package com.ddos.auth.service;


import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlackListService {
    // Потокобезопасное множество для хранения токенов
    private final Set<String> blacklist = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
