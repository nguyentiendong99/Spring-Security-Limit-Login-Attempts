package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
public class UserService {
    public static final int MAX_FAILED_ATTEMPT = 3;
    private static final long LOCK_TIME_DURATION = 10 * 60 * 1000; // 24 hours
    @Autowired
    private UserRepository userRepository;

    public void increaseFailedAttempt(User user){
        int newFailedAttempts  = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempt(newFailedAttempts , user.getUsername());
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }
    public boolean unclock(User user){
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis){
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void resetFailedAttempt(String username) {
        userRepository.updateFailedAttempt(0 , username);
    }
}
