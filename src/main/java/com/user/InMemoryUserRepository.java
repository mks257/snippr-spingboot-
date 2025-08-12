package com.snippr.api.user;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    @Override
    public User save(User user) {
        if (user.getId() == null) user.setId(idGen.incrementAndGet());
        if (user.getCreatedAt() == null) user.setCreatedAt(Instant.now());
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public List<User> findAll() { return new ArrayList<>(store.values()); }
}
