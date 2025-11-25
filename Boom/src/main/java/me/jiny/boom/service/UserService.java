package me.jiny.boom.service;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.domain.entity.*;
import me.jiny.boom.domain.repository.*;
import me.jiny.boom.dto.mapper.CategoryMapper;
import me.jiny.boom.dto.mapper.KeywordMapper;
import me.jiny.boom.dto.mapper.UserMapper;
import me.jiny.boom.dto.request.UserProfileUpdateRequest;
import me.jiny.boom.dto.response.*;
import me.jiny.boom.util.TasteAnalysisUtil;
import me.jiny.boom.util.TasteAnalysisUtil.TasteScore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final UserScoreRepository userScoreRepository;
    private final UserSimilarityRepository userSimilarityRepository;
    private final MonthlyStatisticsRepository monthlyStatisticsRepository;
    private final UserKeywordStatsRepository userKeywordStatsRepository;

    @Transactional(readOnly = true)
    public UserResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserPublicResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return UserMapper.toPublicResponse(user);
    }

    public UserResponse updateMyProfile(Long userId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        return UserMapper.toResponse(user);
    }

    @Transactional
    public StatisticsResponse getStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // 사용자의 카드 목록 가져오기
        List<Card> userCards = cardRepository.findByUser(user);

        // PAEF 점수 계산 및 UserScore 생성/업데이트
        TasteScore tasteScore = TasteAnalysisUtil.calculateUserTasteScore(userCards);
        String tasteType = TasteAnalysisUtil.determineTasteType(tasteScore);
        String tasteTypeName = TasteAnalysisUtil.getTasteTypeName(tasteType);

        UserScore userScore = userScoreRepository.findByUser(user)
                .orElse(UserScore.builder()
                        .user(user)
                        .build());

        userScore.setActivityScore((int) Math.round(tasteScore.a));
        userScore.setProductivityScore((int) Math.round(tasteScore.p));
        userScore.setEmotionalScore((int) Math.round(tasteScore.e));
        userScore.setFocusScore((int) Math.round(tasteScore.f));
        userScore.setTypeName(tasteTypeName);
        userScoreRepository.save(userScore);

        // 카테고리 통계 계산 (TOP 5)
        Map<Category, Long> categoryCounts = userCards.stream()
                .collect(Collectors.groupingBy(Card::getCategory, Collectors.counting()));

        List<CategoryStatsResponse> topCategories = categoryCounts.entrySet().stream()
                .sorted(Map.Entry.<Category, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> CategoryStatsResponse.builder()
                        .category(CategoryMapper.toResponse(entry.getKey()))
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        // 키워드 통계 계산 (TOP 5)
        Map<Keyword, Long> keywordCounts = userCards.stream()
                .flatMap(card -> card.getCardKeywords().stream())
                .map(CardKeyword::getKeyword)
                .collect(Collectors.groupingBy(keyword -> keyword, Collectors.counting()));

        List<KeywordStatsResponse> topKeywords = keywordCounts.entrySet().stream()
                .sorted(Map.Entry.<Keyword, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> KeywordStatsResponse.builder()
                        .keyword(KeywordMapper.toResponse(entry.getKey()))
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        // 월별 통계 계산
        Map<String, MonthlyStatsData> monthlyStatsMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        for (Card card : userCards) {
            String period = card.getCreatedAt().format(formatter);
            monthlyStatsMap.computeIfAbsent(period, k -> new MonthlyStatsData())
                    .incrementCards();
        }

        for (BoomUP boomUP : user.getBoomUps()) {
            String period = boomUP.getCreatedAt().format(formatter);
            monthlyStatsMap.computeIfAbsent(period, k -> new MonthlyStatsData())
                    .incrementBoomUps();
        }

        List<MonthlyStatsResponse> monthlyStats = monthlyStatsMap.entrySet().stream()
                .sorted(Map.Entry.<String, MonthlyStatsData>comparingByKey().reversed())
                .map(entry -> {
                    YearMonth yearMonth = YearMonth.parse(entry.getKey(), formatter);
                    MonthlyStatsData data = entry.getValue();
                    return MonthlyStatsResponse.builder()
                            .yearMonth(yearMonth)
                            .totalCards(data.cardCount)
                            .totalBoomUps(data.boomUpCount)
                            .build();
                })
                .collect(Collectors.toList());

        // MonthlyStatistics 엔티티 생성/업데이트
        for (Map.Entry<String, MonthlyStatsData> entry : monthlyStatsMap.entrySet()) {
            String period = entry.getKey();
            MonthlyStatsData data = entry.getValue();
            MonthlyStatisticsId id = new MonthlyStatisticsId(period, userId);

            MonthlyStatistics monthlyStat = monthlyStatisticsRepository.findById(id)
                    .orElse(new MonthlyStatistics(period, user, 0, 0));

            monthlyStat.setCardCount(data.cardCount);
            monthlyStat.setBoomUpCount(data.boomUpCount);
            monthlyStatisticsRepository.save(monthlyStat);
        }

        // UserKeywordStats 엔티티 생성/업데이트 (월별 키워드 사용 통계)
        Map<String, Map<Keyword, Long>> keywordStatsByPeriod = new HashMap<>();
        for (Card card : userCards) {
            String period = card.getCreatedAt().format(formatter);
            for (CardKeyword cardKeyword : card.getCardKeywords()) {
                Keyword keyword = cardKeyword.getKeyword();
                keywordStatsByPeriod
                        .computeIfAbsent(period, k -> new HashMap<>())
                        .merge(keyword, 1L, Long::sum);
            }
        }

        for (Map.Entry<String, Map<Keyword, Long>> periodEntry : keywordStatsByPeriod.entrySet()) {
            String period = periodEntry.getKey();
            Map<Keyword, Long> periodKeywordCounts = periodEntry.getValue();

            for (Map.Entry<Keyword, Long> keywordEntry : periodKeywordCounts.entrySet()) {
                Keyword keyword = keywordEntry.getKey();
                Long count = keywordEntry.getValue();
                UserKeywordStatsId id = new UserKeywordStatsId(period, userId, keyword.getId());

                UserKeywordStats keywordStat = userKeywordStatsRepository.findById(id)
                        .orElse(new UserKeywordStats(period, user, keyword, 0));

                keywordStat.setUsageCount(count.intValue());
                userKeywordStatsRepository.save(keywordStat);
            }
        }

        // 100점 만점으로 환산 (0-10 범위를 0-100으로 변환) 및 반올림
        return StatisticsResponse.builder()
                .topCategories(topCategories)
                .topKeywords(topKeywords)
                .monthlyStats(monthlyStats)
                .tasteType(tasteTypeName)
                .pScore((int) Math.round(tasteScore.p * 10))
                .aScore((int) Math.round(tasteScore.a * 10))
                .eScore((int) Math.round(tasteScore.e * 10))
                .fScore((int) Math.round(tasteScore.f * 10))
                .build();
    }

    @Transactional
    public SimilarityResponse getSimilarity(Long currentUserId, Long targetUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NoSuchElementException("Current user not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new NoSuchElementException("Target user not found"));

        if (currentUserId.equals(targetUserId)) {
            throw new RuntimeException("자기 자신과의 유사도를 계산할 수 없습니다");
        }

        // 현재 사용자와 대상 사용자의 카드 목록 가져오기
        List<Card> currentUserCards = cardRepository.findByUser(currentUser);
        List<Card> targetUserCards = cardRepository.findByUser(targetUser);

        // PAEF 점수 계산
        TasteScore currentScore = TasteAnalysisUtil.calculateUserTasteScore(currentUserCards);
        TasteScore targetScore = TasteAnalysisUtil.calculateUserTasteScore(targetUserCards);

        // 유클리드 거리 기반 유사도 계산
        double similarityScore = TasteAnalysisUtil.calculateEuclideanSimilarity(currentScore, targetScore);

        // UserSimilarity 생성 또는 업데이트 (항상 더 작은 ID가 user1이 되도록 정규화)
        User user1, user2;
        if (currentUser.getId() < targetUser.getId()) {
            user1 = currentUser;
            user2 = targetUser;
        } else {
            user1 = targetUser;
            user2 = currentUser;
        }

        UserSimilarity similarity = userSimilarityRepository.findByUser1AndUser2(user1, user2)
                .orElse(new UserSimilarity(user1, user2, similarityScore));

        similarity.setSimilarityScore(similarityScore);
        userSimilarityRepository.save(similarity);

        // 유사도 설명 생성
        String description = getSimilarityDescription(similarityScore);

        return SimilarityResponse.builder()
                .targetUser(UserMapper.toSimpleResponse(targetUser))
                .similarityScore(similarityScore)
                .description(description)
                .build();
    }

    private String getSimilarityDescription(double similarityScore) {
        if (similarityScore >= 0.9) {
            return "거의 동일한 취향을 가지고 있습니다";
        } else if (similarityScore >= 0.8) {
            return "꽤나 유사한 취향을 가지고 있습니다";
        } else if (similarityScore >= 0.6) {
            return "비슷한 취향을 가지고 있습니다";
        } else if (similarityScore >= 0.4) {
            return "일부 유사한 취향을 가지고 있습니다";
        } else {
            return "반대의 취향을 가지고 있습니다";
        }
    }

    // 월별 통계 데이터를 임시로 저장
    private static class MonthlyStatsData {
        int cardCount = 0;
        int boomUpCount = 0;

        void incrementCards() {
            cardCount++;
        }

        void incrementBoomUps() {
            boomUpCount++;
        }
    }
}

