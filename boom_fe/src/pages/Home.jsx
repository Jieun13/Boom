import React, { useState, useEffect, useRef } from 'react';
import BoomCard from '../components/BoomCard';
import { cardService } from '../services/card';

const Home = ({ onUserClick, onCreateClick }) => {
    const [cards, setCards] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('recent'); // recent, ranking, recommendations

    useEffect(() => {
        loadCards();
    }, [activeTab]);

    const loadCards = async () => {
        try {
            setLoading(true);
            let response;
            
            if (activeTab === 'recent') {
                response = await cardService.getRecentCards();
            } else if (activeTab === 'ranking') {
                response = await cardService.getRankingCards();
            } else if (activeTab === 'recommendations') {
                response = await cardService.getRecommendationCards();
            }

            if (response?.success) {
                const cardsList = response.data || [];
                // 카드 상세 정보 가져오기 (간단한 응답인 경우)
                const detailedCards = await Promise.all(
                    cardsList.map(async (card) => {
                        // 이미 상세 정보가 있으면 그대로 사용
                        if (card.category && card.keywords) {
                            return card;
                        }
                        // 상세 정보가 없으면 가져오기
                        try {
                            const detailRes = await cardService.getCard(card.id);
                            return detailRes?.success ? detailRes.data : card;
                        } catch (error) {
                            console.error(`카드 ${card.id} 상세 정보 로딩 실패:`, error);
                            return card;
                        }
                    })
                );
                setCards(detailedCards);
            }
        } catch (error) {
            console.error('카드 로딩 실패:', error);
            setCards([]);
        } finally {
            setLoading(false);
        }
    };

    // API 응답 데이터를 컴포넌트가 기대하는 형식으로 변환
    const transformCardData = (card) => {
        return {
            id: card.id,
            category: card.category?.name || '',
            subCategory: card.subCategory?.name || '',
            title: card.name || '',
            desc: card.description || '',
            level: card.boomLevel || 3,
            author: card.user?.nickname || 'User',
            authorProfileUrl: card.user?.profileImageUrl || 'https://randomuser.me/api/portraits/lego/1.jpg',
            userId: card.user?.id,
            boomUp: card.boomUpCount || 0,
            keywords: card.keywords?.map(k => k.name) || [],
            imageUrl: card.imageUrl || null,
            isBoomUpped: card.isBoomUpped || false,
            isCollected: card.isCollected || false,
        };
    };

    const handleCardUpdate = () => {
        loadCards();
    };

    const cardsSectionRef = useRef(null);
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                entries.forEach((entry) => {
                    if (entry.isIntersecting) {
                        setIsVisible(true);
                    }
                });
            },
            { threshold: 0.1 }
        );

        if (cardsSectionRef.current) {
            observer.observe(cardsSectionRef.current);
        }

        return () => {
            if (cardsSectionRef.current) {
                observer.unobserve(cardsSectionRef.current);
            }
        };
    }, []);

    return (
        <div className="animate-fadeIn">
            {/* 첫 번째 섹션: 중앙 배치 */}
            <section className="min-h-[calc(100vh-8rem)] flex flex-col items-center justify-center text-center px-4">
                <h1 className="text-4xl md:text-6xl font-bold text-white mb-6">
                    요즘 나의 <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#E4007C] to-[#9D4EDC]">Boom</span>은?
                </h1>
                <button
                    onClick={onCreateClick}
                    className="mt-8 px-8 py-4 bg-gradient-to-r from-[#E4007C] to-[#9D4EDC] text-white font-bold rounded-full shadow-[0_0_30px_rgba(228,0,124,0.5)] hover:shadow-[0_0_40px_rgba(228,0,124,0.8)] transition-all transform hover:-translate-y-1 hover:scale-105"
                >
                    나의 Boom 카드 생성하기
                </button>
            </section>

            {/* 두 번째 섹션: 카드 목록 (스크롤 시 나타남) */}
            <section 
                ref={cardsSectionRef}
                className={`transition-all duration-1000 ${
                    isVisible 
                        ? 'opacity-100 translate-y-0' 
                        : 'opacity-0 translate-y-10'
                }`}
            >
                <div className="flex justify-between items-end mb-6">
                    <h2 className="text-xl font-bold text-white">
                        {activeTab === 'recent' && '최근 Booms 📅'}
                        {activeTab === 'ranking' && 'Trending Booms 🔥'}
                        {activeTab === 'recommendations' && '추천 Booms ⭐'}
                    </h2>
                    <div className="flex gap-2">
                        <button
                            onClick={() => setActiveTab('recent')}
                            className={`px-3 py-1 rounded-full text-sm transition-colors ${
                                activeTab === 'recent' 
                                    ? 'bg-[#E4007C] text-white' 
                                    : 'bg-white/5 text-[#A6A6A6] hover:bg-white/10'
                            }`}
                        >
                            최근
                        </button>
                        <button
                            onClick={() => setActiveTab('ranking')}
                            className={`px-3 py-1 rounded-full text-sm transition-colors ${
                                activeTab === 'ranking' 
                                    ? 'bg-[#E4007C] text-white' 
                                    : 'bg-white/5 text-[#A6A6A6] hover:bg-white/10'
                            }`}
                        >
                            인기
                        </button>
                        <button
                            onClick={() => setActiveTab('recommendations')}
                            className={`px-3 py-1 rounded-full text-sm transition-colors ${
                                activeTab === 'recommendations' 
                                    ? 'bg-[#E4007C] text-white' 
                                    : 'bg-white/5 text-[#A6A6A6] hover:bg-white/10'
                            }`}
                        >
                            추천
                        </button>
                    </div>
                </div>
                
                {loading ? (
                    <div className="text-center text-[#A6A6A6] py-12">로딩 중...</div>
                ) : cards.length === 0 ? (
                    <div className="text-center text-[#A6A6A6] py-12">카드가 없습니다.</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {cards.map(card => (
                            <BoomCard 
                                key={card.id} 
                                data={transformCardData(card)} 
                                onUpdate={handleCardUpdate}
                                onUserClick={onUserClick}
                            />
                        ))}
                    </div>
                )}
            </section>
        </div>
    );
};

export default Home;