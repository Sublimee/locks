package com.sublimee.boot.locks.service.impl;

import com.sublimee.boot.locks.model.card.Card;
import com.sublimee.boot.locks.model.card.VersionedCard;
import com.sublimee.boot.locks.repository.card.CardRepository;
import com.sublimee.boot.locks.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CardServiceImpl implements ApplicationListener<ApplicationReadyEvent>, CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        cardRepository.persist(new VersionedCard());
    }

    @Override
    @Cacheable(
            value = "cardCache",
            key = "#id", unless = "#result == null")
    public Card getCard(UUID id) {
        Card result = cardRepository.find(id);
        return result;
    }

    @CacheEvict(value = "cardCache", allEntries = true)
    public void evictCache() {
    }

}
