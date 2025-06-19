package tg.bots.bot_with_fics.service;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import tg.bots.bot_with_fics.entity.UserState;
import tg.bots.bot_with_fics.repository.UserStateRepository;

@Service
@RequiredArgsConstructor
public class UserStateService {

    private final UserStateRepository userStateRepository;




    @Transactional
    public void save(UserState userState) {
        userStateRepository.save(userState);
    }


    @Transactional
    public UserState findById(long id) {
       UserState userState = userStateRepository.findById(id).orElse(new UserState());
       userState.setUserId(id);
       return userState;
    }
}
