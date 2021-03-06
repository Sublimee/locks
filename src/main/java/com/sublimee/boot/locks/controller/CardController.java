package com.sublimee.boot.locks.controller;

import com.sublimee.boot.locks.model.card.Card;
import com.sublimee.boot.locks.service.CardService;
import com.sublimee.boot.locks.service.impl.CardServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/card", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{cardNumber}/balance")
    public String getBalance(@PathVariable UUID cardNumber) {
        Card card = cardService.getCard(cardNumber);
        if (card == null){
            return null;
        }
        return card.getBalance().toString();
    }

    @GetMapping("/evict")
    public void evictCache() {
        cardService.evictCache();
    }

}
