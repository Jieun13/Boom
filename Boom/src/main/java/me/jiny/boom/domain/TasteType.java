package me.jiny.boom.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum TasteType {
    SCTL("SCTL", "효율 추구 스마트 유저", "편안한 실내에서 정보를 수집하고 실속을 챙기는 실용주의자"),
    SCTF("SCTF", "지적인 탐구 생활자", "책과 지식의 바다에 깊이 빠져드는 뇌가 섹시한 지식인"),
    SCEL("SCEL", "방구석 힐링 요정", "집에서 가볍게 즐기는 휴식이 최고인 평화주의자"),
    SCEF("SCEF", "감성에 잠긴 철학자", "정적이지만 깊이 있는 예술과 감성을 탐구하는 내면 여행자"),
    SPTL("SPTL", "방구석 크리에이터", "톡톡 튀는 아이디어로 세상을 놀라게 할 콘텐츠를 기획하는 기획자"),
    SPTF("SPTF", "집념의 장인", "정적인 몰입과 끈기로 완벽한 결과물을 만들어내는 장인"),
    SPEL("SPEL", "트렌드세터 몽상가", "감각적인 취향을 바탕으로 자신만의 스타일을 큐레이션하는 힙스터"),
    SPEF("SPEF", "고독한 예술가", "홀로 깊게 몰입하며 독창적인 감성 세계를 창조하는 아티스트"),
    ACTL("ACTL", "발로 뛰는 정보통", "현장을 누비며 누구보다 빠르게 핫한 정보를 캐치하는 리포터"),
    ACTF("ACTF", "필드 위의 전략가", "스포츠 직관이나 야외 활동 중에도 깊이 있게 분석하는 승부사"),
    ACEL("ACEL", "자유로운 영혼의 나그네", "가볍게 떠나 경험하고 즐기며 에너지를 채우는 자유인"),
    ACEF("ACEF", "자연 속의 사색가", "몸은 산과 들에 있지만 머리는 깊은 생각에 잠겨있는 철학자"),
    APTL("APTL", "감각적인 행동대장", "생각을 현실로 만들기 위해 부지런히 움직이는 실행가"),
    APTF("APTF", "끈기 있는 개척자", "목표를 향해 온몸을 던지고 깊게 파고드는 불굴의 도전자"),
    APEL("APEL", "열정적인 무대 체질", "온몸으로 표현하고 사람들과 호흡하며 에너지를 뿜어내는 퍼포머"),
    APEF("APEF", "육각형 갓생 러너", "운동, 공부, 일, 취미 무엇 하나 놓치지 않는 슈퍼맨");

    private final String code;
    private final String name;
    private final String description;

    private static final Map<String, TasteType> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(TasteType::getCode, Function.identity()));

    /**
     * 코드로부터 TasteType을 찾습니다.
     * 
     * @param code 페르소나 유형 코드 (예: "SCTL", "SCTF")
     * @return 해당하는 TasteType, 없으면 null
     */
    public static TasteType fromCode(String code) {
        return CODE_MAP.get(code);
    }

    /**
     * 코드로부터 TasteType을 찾습니다. 없으면 기본값을 반환합니다.
     * 
     * @param code 페르소나 유형 코드
     * @param defaultValue 기본값
     * @return 해당하는 TasteType 또는 기본값
     */
    public static TasteType fromCodeOrDefault(String code, TasteType defaultValue) {
        return CODE_MAP.getOrDefault(code, defaultValue);
    }
}

