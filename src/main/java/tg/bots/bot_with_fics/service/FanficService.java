package tg.bots.bot_with_fics.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tg.bots.bot_with_fics.entity.Fanfic;
import tg.bots.bot_with_fics.repository.FanficRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class FanficService {

    private final FanficRepository fanficRepository;

    @Transactional
    public int delete(String title, String author) {
        Optional<Fanfic> fic = fanficRepository.findFanfic(title, author);
        if (fic.isPresent()) {
            fanficRepository.deleteFic(title, author);
            return 1;
        }
        return 0;
    }


    @Transactional
    public List<Integer> getAllFanficIds() {
        return fanficRepository.findAllIds();
    }

    public int getRandomId(List<Integer> list) {
        Random random = new Random();
        if (list.isEmpty()) return -1;
        return list.get(random.nextInt(list.size()));
    }

    @Transactional
    public Optional<Fanfic> getFanficById(int id) {
        return fanficRepository.findById(id);
    }

    public Fanfic getRandomFanfic() {
        int id = getRandomId(getAllFanficIds());
        if (id<0) return null;
        return getFanficById(id).get();
    }

    @Transactional
    public List<Integer> getIds(String mood) {
        return fanficRepository.findAllIdsByMood(mood);
    }
//для раздела HOT
    public Fanfic getRandomFanficByMood(String state) {
        String mood = state.toLowerCase();
        int id = getRandomId(getIds(mood));
        if (id<0) return null;
       return getFanficById(id).get();

    }
// для всех остальных разделов
    @Transactional
    public List<Integer> getIdsByMoodAndCheck(String state) {
        String[] mood = state.toLowerCase().split("_");
        return fanficRepository.findAllIdsByMoodAndCheck(mood[0], Boolean.parseBoolean(mood[1]));


    }
    public Fanfic getRandomFanficByMoodAndCheck(String state) {
        int id = getRandomId(getIdsByMoodAndCheck(state));
        if (id<0) return null;
        return getFanficById(id).get();

    }

    public int createFic(Fanfic fanfic) {
        Optional<Fanfic> fic = fanficRepository.findFanfic(fanfic.getTitle(), fanfic.getAuthor());
        if (!fic.isPresent()) {
            fanficRepository.save(fanfic);
            return 1;
        }
            return 0;
    }


}
