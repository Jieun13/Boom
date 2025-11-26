package me.jiny.boom.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.jiny.boom.domain.TasteType;
import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.Category;

import java.util.List;

public class TasteAnalysisUtil {

    // 사용자의 평균 PAEF 점수 계산 DTO
    @Getter
    @AllArgsConstructor
    public static class TasteScore {
        public double a;
        public double p;
        public double e;
        public double f;
    }


    // boomLevel 가중치를 적용한 APEF 점수 계산
    public static TasteScore calculateUserTasteScore(List<Card> userCards) {
        if (userCards == null || userCards.isEmpty()) {
            return new TasteScore(0, 0, 0, 0);
        }

        double weightedSumA = 0;
        double weightedSumP = 0;
        double weightedSumE = 0;
        double weightedSumF = 0;

        int totalWeight = 0; // 가중치 합

        for (Card card : userCards) {
            Category cat = card.getCategory();

            // boomLevel이 null이거나 0일 경우 1로 처리
            int weight = (card.getBoomLevel() != null && card.getBoomLevel() > 0)
                    ? card.getBoomLevel()
                    : 1;

            // 가중치 누적 (분자)
            weightedSumA += (cat.getAScore() != null ? cat.getAScore() : 0) * weight;
            weightedSumP += (cat.getPScore() != null ? cat.getPScore() : 0) * weight;
            weightedSumE += (cat.getEScore() != null ? cat.getEScore() : 0) * weight;
            weightedSumF += (cat.getFScore() != null ? cat.getFScore() : 0) * weight;

            // 총 가중치 합산 (분모)
            totalWeight += weight;
        }

        // 분모가 0이면 0 리턴
        if (totalWeight == 0) return new TasteScore(0, 0, 0, 0);

        // 가중 평균 계산
        return new TasteScore(
                weightedSumA / totalWeight,
                weightedSumP / totalWeight,
                weightedSumE / totalWeight,
                weightedSumF / totalWeight
        );
    }

    // 점수를 기반으로 16가지 유형 문자열 반환
    // a/A → S/A, p/P → C/P, e/E → T/E, f/F → L/F
    public static String determineTasteType(TasteScore score) {
        StringBuilder type = new StringBuilder();
        double threshold = 5; // 기준점

        type.append(score.a >= threshold ? "A" : "S");
        type.append(score.p >= threshold ? "P" : "C");
        type.append(score.e >= threshold ? "E" : "T");
        type.append(score.f >= threshold ? "F" : "L");

        return type.toString();
    }

    // 취향 유형 이름 반환
    public static String getTasteTypeName(String tasteType) {
        TasteType type = TasteType.fromCode(tasteType);
        return type != null ? type.getName() : "미분류";
    }

    // 취향 유형 설명 반환
    public static String getTasteTypeDescription(String tasteType) {
        TasteType type = TasteType.fromCode(tasteType);
        return type != null ? type.getDescription() : "설명 없음";
    }

    // 취향 유사도 계산 (유클리드 거리 기반)
    public static double calculateEuclideanSimilarity(TasteScore user1, TasteScore user2) {
        // 1. 각 속성별 차이의 제곱 계산
        double diffA = Math.pow(user1.a - user2.a, 2);
        double diffP = Math.pow(user1.p - user2.p, 2);
        double diffE = Math.pow(user1.e - user2.e, 2);
        double diffF = Math.pow(user1.f - user2.f, 2);

        // 2. 유클리드 거리 계산
        double distance = Math.sqrt(diffA + diffP + diffE + diffF);

        // 3. 최대 가능한 거리 (4개 항목 모두 0점 vs 10점 차이일 때 = 20)
        double maxDistance = 20.0;

        // 4. 거리를 유사도(%)로 환산
        // -> 거리가 0이면 유사도 1, 거리가 20이면 유사도 0
        double similarity = 1 - (distance / maxDistance);

        // 음수 방지 및 퍼센트 변환 (0.0 ~ 1.0)
        return Math.max(0.0, similarity);
    }
}