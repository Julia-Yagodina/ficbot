package tg.bots.bot_with_fics.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tg.bots.bot_with_fics.entity.Owner;
import tg.bots.bot_with_fics.repository.OwnerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerService {
    private final OwnerRepository ownerRepository;

    @Transactional
    public void createOwner(long chatId) {
        Owner owner = new Owner(chatId);
        if (!ownerRepository.existsById(chatId)) ownerRepository.save(owner);
    }

    @Transactional
    public List<Long> getOwners() {
        return ownerRepository.findAllIds();
    }

    public boolean hasOwner(long chatId) {
        List<Long> owners = getOwners();
        return owners.contains(chatId);

    }
}
