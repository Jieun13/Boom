package me.jiny.boom.service;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.domain.entity.BoomUP;
import me.jiny.boom.domain.entity.BoomUPId;
import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.User;
import me.jiny.boom.domain.repository.BoomUPRepository;
import me.jiny.boom.domain.repository.CardRepository;
import me.jiny.boom.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoomUPService {

    private final BoomUPRepository boomUPRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public void addBoomUP(Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

        if (boomUPRepository.existsByUserAndCard(user, card)) {
            throw new RuntimeException("이미 BoomUP한 카드입니다");
        }

        BoomUPId id = new BoomUPId(userId, cardId);
        BoomUP boomUP = BoomUP.builder()
                .id(id)
                .user(user)
                .card(card)
                .build();

        boomUPRepository.save(boomUP);

        card.setBoomUpCount(card.getBoomUpCount() + 1);
    }

    public void removeBoomUP(Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

        if (!boomUPRepository.existsByUserAndCard(user, card)) {
            throw new RuntimeException("BoomUP한 카드가 아닙니다");
        }

        boomUPRepository.deleteByUserAndCard(user, card);
        
        // 카드의 boomUpCount 감소
        card.setBoomUpCount(Math.max(0, card.getBoomUpCount() - 1));
    }
}

