package me.jiny.boom.service;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.domain.entity.*;
import me.jiny.boom.domain.repository.*;
import me.jiny.boom.dto.mapper.CardMapper;
import me.jiny.boom.dto.request.CardCreateRequest;
import me.jiny.boom.dto.request.CardUpdateRequest;
import me.jiny.boom.dto.response.CardResponse;
import me.jiny.boom.dto.response.CardSimpleResponse;
import me.jiny.boom.util.TasteAnalysisUtil;
import me.jiny.boom.util.TasteAnalysisUtil.TasteScore;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final KeywordRepository keywordRepository;

    public CardResponse create(CardCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        SubCategory subCategory = request.getSubCategoryId() != null
                ? subCategoryRepository.findById(request.getSubCategoryId())
                .orElseThrow(() -> new NoSuchElementException("SubCategory not found"))
                : null;

        Card card = Card.builder()
                .user(user)
                .category(category)
                .subCategory(subCategory)
                .name(request.getName())
                .description(request.getDescription())
                .boomLevel(request.getBoomLevel())
                .build();

        if (request.getKeywordIds() != null && !request.getKeywordIds().isEmpty()) {
            List<Keyword> keywords = keywordRepository.findAllById(request.getKeywordIds());
            for (Keyword keyword : keywords) {
                CardKeyword cardKeyword = new CardKeyword(); // builder X
                cardKeyword.setCard(card);
                cardKeyword.setKeyword(keyword);
                card.getCardKeywords().add(cardKeyword);
            }
        }

        Card savedCard = cardRepository.save(card);
        return CardMapper.toResponse(savedCard, userId);
    }

    @Transactional(readOnly = true)
    public CardResponse getById(Long cardId, Long userId) {
        Card card = cardRepository.findByIdWithAll(cardId)
            .orElseThrow(() -> new NoSuchElementException("Card not found"));
        return CardMapper.toResponse(card, userId);
    }
    
    private void validateCardOwner(Card card, Long userId) {
        if (!card.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다");
        }
    }

    @Transactional
    public CardResponse update(Long cardId, CardUpdateRequest request, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

        validateCardOwner(card, userId);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("Category not found"));
            card.setCategory(category);
        }

        if (request.getSubCategoryId() != null) {
            SubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("SubCategory not found"));
            card.setSubCategory(subCategory);
        }

        if (request.getName() != null) card.setName(request.getName());
        if (request.getDescription() != null) card.setDescription(request.getDescription());
        if (request.getBoomLevel() != null) card.setBoomLevel(request.getBoomLevel());

        if (request.getKeywordIds() != null) {
            card.getCardKeywords().clear();
            cardRepository.flush();

            List<Keyword> keywords = keywordRepository.findAllById(request.getKeywordIds());
            for (Keyword keyword : keywords) {
                CardKeyword cardKeyword = new CardKeyword();
                cardKeyword.setCard(card);
                cardKeyword.setKeyword(keyword);
                card.getCardKeywords().add(cardKeyword);
            }
        }

        return CardMapper.toResponse(card, userId);
    }


    public void delete(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NoSuchElementException("Card not found"));
        
        validateCardOwner(card, userId);
        
        cardRepository.delete(card);
    }

    @Transactional(readOnly = true)
    public List<CardSimpleResponse> getUserCards(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
        return cardRepository.findByUser(user).stream()
            .map(CardMapper::toSimpleResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardSimpleResponse> getRecentCards() {
        return cardRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(CardMapper::toSimpleResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardSimpleResponse> getRankingCards() {
        return cardRepository.findAllByOrderByBoomUpCountDesc().stream()
            .map(CardMapper::toSimpleResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardSimpleResponse> getRecommendedCards(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 현재 사용자의 카드 목록 가져오기
        List<Card> userCards = cardRepository.findByUser(user);

        // 현재 사용자의 PAEF 점수 계산
        TasteScore userTasteScore = TasteAnalysisUtil.calculateUserTasteScore(userCards);

        // 모든 카드 조회 (자신의 카드는 제외)
        List<Card> allCards = cardRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(card -> !card.getUser().getId().equals(userId))
                .collect(Collectors.toList());

        // 각 카드 작성자의 PAEF 점수 계산 및 유사도 계산
        return allCards.stream()
                .map(card -> {
                    // 카드 작성자의 모든 카드 목록 가져오기
                    List<Card> cardOwnerCards = cardRepository.findByUser(card.getUser());
                    
                    // 카드 작성자의 APEF 점수 계산
                    TasteScore cardOwnerTasteScore = TasteAnalysisUtil.calculateUserTasteScore(cardOwnerCards);

                    // 유클리드 거리 기반 유사도 계산
                    double similarity = TasteAnalysisUtil.calculateEuclideanSimilarity(userTasteScore, cardOwnerTasteScore);

                    return new CardWithSimilarity(card, similarity);
                })
                .sorted(Comparator
                        .comparing((CardWithSimilarity c) -> c.similarity).reversed() // 유사도 높은 순
                        .thenComparing(c -> c.card.getCreatedAt(), Comparator.reverseOrder()) // 같은 유사도면 최신순
                )
                .limit(4)
                .map(c -> CardMapper.toSimpleResponse(c.card))
                .collect(Collectors.toList());
    }

    // 카드와 유사도를 함께 저장하는 내부 클래스
    private static class CardWithSimilarity {
        Card card;
        double similarity;

        CardWithSimilarity(Card card, double similarity) {
            this.card = card;
            this.similarity = similarity;
        }
    }
}

