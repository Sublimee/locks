package com.sublimee.boot.locks.service;

import com.sublimee.boot.locks.model.card.Card;

import java.util.UUID;

public interface CardService {

    Card getCard(UUID id);

    void evictCache();

}
