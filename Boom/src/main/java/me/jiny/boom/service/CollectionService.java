package me.jiny.boom.service;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.CardCollection;
import me.jiny.boom.domain.entity.CardCollectionId;
import me.jiny.boom.domain.entity.User;
import me.jiny.boom.domain.repository.CardCollectionRepository;
import me.jiny.boom.domain.repository.CardRepository;
import me.jiny.boom.domain.repository.UserRepository;
import me.jiny.boom.dto.mapper.CardMapper;
import me.jiny.boom.dto.response.CardSimpleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionService {

    private final CardCollectionRepository cardCollectionRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public void addCollection(Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

        if (cardCollectionRepository.existsByUserAndCard(user, card)) {
            throw new RuntimeException("이미 수집한 카드입니다");
        }

        CardCollectionId id = new CardCollectionId(userId, cardId);
        CardCollection collection = CardCollection.builder()
                .id(id)
                .user(user)
                .card(card)
                .build();

        cardCollectionRepository.save(collection);
    }

    public void removeCollection(Long cardId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

        if (!cardCollectionRepository.existsByUserAndCard(user, card)) {
            throw new RuntimeException("수집한 카드가 아닙니다");
        }

        cardCollectionRepository.deleteByUserAndCard(user, card);
    }

    @Transactional(readOnly = true)
    public List<CardSimpleResponse> getMyCollections(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        return cardCollectionRepository.findByUser(user, org.springframework.data.domain.Pageable.unpaged())
                .stream()
                .map(CardCollection::getCard)
                .map(CardMapper::toSimpleResponse)
                .collect(Collectors.toList());
    }
}

