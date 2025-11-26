import React, { useState, useEffect } from 'react';
import BoomCard from '../components/BoomCard';
import { BarChart2, Edit2 } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { userService } from '../services/user';
import { cardService } from '../services/card';
import { collectionService } from '../services/collection';
import { CATEGORY_CONFIG } from '../data/constants';
import EditProfile from './EditProfile';
import CreateBoomModal from './CreateBoomModal';

const MyBoom = () => {
    const { user } = useAuth();
    const [profile, setProfile] = useState(null);
    const [statistics, setStatistics] = useState(null);
    const [myCards, setMyCards] = useState([]);
    const [collectedCards, setCollectedCards] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showEditProfile, setShowEditProfile] = useState(false);
    const [showEditCard, setShowEditCard] = useState(false);
    const [editingCard, setEditingCard] = useState(null);
    const [activeTab, setActiveTab] = useState('my'); // my, collected

    useEffect(() => {
        if (user) {
            loadData();
        }
    }, [user]);

    const loadData = async () => {
        try {
            setLoading(true);
            
            // 프로필, 통계, 카드 목록 병렬 로드
            const [profileRes, statsRes, myCardsRes, collectedRes] = await Promise.all([
                userService.getMyProfile(),
                userService.getStatistics(user.id),
                cardService.getMyCards(),
                collectionService.getMyCollections(),
            ]);

            if (profileRes?.success) {
                setProfile(profileRes.data);
            }
            if (statsRes?.success) {
                setStatistics(statsRes.data);
            }
            if (myCardsRes?.success) {
                const myCardsList = myCardsRes.data || [];
                // 내 카드 상세 정보 가져오기 (간단한 응답인 경우)
                const detailedMyCards = await Promise.all(
                    myCardsList.map(async (card) => {
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
                setMyCards(detailedMyCards);
            }
            if (collectedRes?.success) {
                const collected = collectedRes.data || [];
                // 수집한 카드의 상세 정보 가져오기
                const detailedCollected = await Promise.all(
                    collected.map(async (card) => {
                        try {
                            const detailRes = await cardService.getCard(card.id);
                            return detailRes?.success ? detailRes.data : card;
                        } catch (error) {
                            console.error(`카드 ${card.id} 상세 정보 로딩 실패:`, error);
                            return card;
                        }
                    })
                );
                setCollectedCards(detailedCollected);
            }
        } catch (error) {
            console.error('데이터 로딩 실패:', error);
        } finally {
            setLoading(false);
        }
    };


    if (loading) {
        return (
            <div className="animate-fadeIn">
                <div className="text-center text-[#A6A6A6] py-12">로딩 중...</div>
            </div>
        );
    }

    return (
        <div className="animate-fadeIn">
            {/* 프로필 헤더 */}
            <div className="flex items-center justify-between gap-4 mb-8 bg-white/5 p-6 rounded-2xl border border-white/10 mt-8">
                <div className="flex items-center gap-4 flex-1">
                    <div className="w-20 h-20 rounded-full bg-gradient-to-br from-[#0CFED8] to-[#9D4EDC] flex items-center justify-center text-3xl font-bold text-[#0D0C1D] overflow-hidden">
                        {profile?.profileImageUrl ? (
                            <img src={profile.profileImageUrl} alt={profile.nickname} className="w-full h-full object-cover" />
                        ) : (
                            (profile?.nickname || user?.nickname || 'ME').charAt(0).toUpperCase()
                        )}
                    </div>
                    <div>
                        <h2 className="text-2xl font-bold text-white">{profile?.nickname || user?.nickname || 'User'}</h2>
                        <p className="text-[#0CFED8] text-sm">{profile?.bio || '취향을 공유해보세요!'}</p>
                        <div className="flex gap-3 mt-2 text-xs text-[#A6A6A6]">
                            <span><b>{myCards.length}</b> Booms</span>
                            <span><b>{statistics?.monthlyStats?.[0]?.totalBoomUps || 0}</b> BoomUps</span>
                        </div>
                    </div>
                </div>
                <button
                    onClick={() => setShowEditProfile(true)}
                    className="flex items-center gap-2 px-4 py-2 rounded-xl bg-white/5 border border-white/10 text-[#A6A6A6] hover:bg-white/10 hover:text-white transition-all"
                >
                    <Edit2 size={16} />
                    <span className="text-sm">프로필 수정</span>
                </button>
            </div>

            {/* 통계 영역 */}
            {statistics && (
                <>
                    <h3 className="text-lg font-bold text-white mb-6 flex items-center gap-2">
                        <BarChart2 size={20} className="text-[#9D4EDC]"/> 취향 분석 리포트
                    </h3>
                    
                    {/* 취향 유형 - 강조 */}
                    {statistics.tasteType && (
                        <div className="bg-gradient-to-br from-[#9D4EDC]/20 to-[#E4007C]/20 p-6 rounded-2xl border-2 border-[#9D4EDC]/30 mb-6 relative overflow-hidden">
                            <div className="absolute top-0 right-0 w-32 h-32 bg-[#9D4EDC]/10 rounded-full blur-3xl -mr-16 -mt-16"></div>
                            <div className="relative z-10">
                                <div className="text-[#A6A6A6] text-xs mb-2 font-medium">나의 취향 유형</div>
                                <div className="text-[#9D4EDC] font-bold text-2xl mb-2">{statistics.tasteType}</div>
                                <div className="text-[#F4F4F3] text-sm leading-relaxed mb-4">{statistics.tasteTypeDescription}</div>
                                
                                {/* PAEF 점수 그래프 */}
                                {(statistics.ascore != null || statistics.pscore != null || statistics.escore != null || statistics.fscore != null) && (
                                    <div className="mt-4 pt-4 border-t border-white/10">
                                        <div className="text-[#A6A6A6] text-xs mb-3 font-medium">나의 취향 점수</div>
                                        <div className="grid grid-cols-1 gap-3">
                                            {statistics.ascore != null && (
                                                <div>
                                                    <div className="flex justify-between items-center mb-1.5">
                                                        <span className="text-white text-xs font-medium">Activity (활동성)</span>
                                                        <span className="text-[#E4007C] text-xs font-bold">{statistics.ascore}</span>
                                                    </div>
                                                    <div className="w-full bg-gray-700/50 h-1.5 rounded-full overflow-hidden">
                                                        <div 
                                                            className="h-full bg-gradient-to-r from-[#E4007C] to-[#FF6B9D] transition-all duration-500"
                                                            style={{ width: `${statistics.ascore}%` }}
                                                        />
                                                    </div>
                                                </div>
                                            )}
                                            {statistics.pscore != null && (
                                                <div>
                                                    <div className="flex justify-between items-center mb-1.5">
                                                        <span className="text-white text-xs font-medium">Productivity (생산성)</span>
                                                        <span className="text-[#0CFED8] text-xs font-bold">{statistics.pscore}</span>
                                                    </div>
                                                    <div className="w-full bg-gray-700/50 h-1.5 rounded-full overflow-hidden">
                                                        <div 
                                                            className="h-full bg-gradient-to-r from-[#0CFED8] to-[#4ECDC4] transition-all duration-500"
                                                            style={{ width: `${statistics.pscore}%` }}
                                                        />
                                                    </div>
                                                </div>
                                            )}
                                            {statistics.escore != null && (
                                                <div>
                                                    <div className="flex justify-between items-center mb-1.5">
                                                        <span className="text-white text-xs font-medium">Emotional (감정적인)</span>
                                                        <span className="text-[#9D4EDC] text-xs font-bold">{statistics.escore}</span>
                                                    </div>
                                                    <div className="w-full bg-gray-700/50 h-1.5 rounded-full overflow-hidden">
                                                        <div 
                                                            className="h-full bg-gradient-to-r from-[#9D4EDC] to-[#C77DFF] transition-all duration-500"
                                                            style={{ width: `${statistics.escore}%` }}
                                                        />
                                                    </div>
                                                </div>
                                            )}
                                            {statistics.fscore != null && (
                                                <div>
                                                    <div className="flex justify-between items-center mb-1.5">
                                                        <span className="text-white text-xs font-medium">Focus (몰입도)</span>
                                                        <span className="text-[#FFA502] text-xs font-bold">{statistics.fscore}</span>
                                                    </div>
                                                    <div className="w-full bg-gray-700/50 h-1.5 rounded-full overflow-hidden">
                                                        <div 
                                                            className="h-full bg-gradient-to-r from-[#FFA502] to-[#FFC947] transition-all duration-500"
                                                            style={{ width: `${statistics.fscore}%` }}
                                                        />
                                                    </div>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}


                    {/* 최다 선택 카테고리 */}
                    <div className="bg-[#1a192e] p-6 rounded-2xl border border-white/10 mb-6">
                        <div className="text-[#A6A6A6] text-xs mb-4 font-medium">최다 선택 카테고리</div>
                        <div className="flex flex-wrap gap-3">
                            {statistics.topCategories?.slice(0, 5).map((item, idx) => {
                                const categoryName = item.category?.name || 'N/A';
                                const categoryConfig = CATEGORY_CONFIG[categoryName];
                                const CategoryIcon = categoryConfig?.icon;
                                const categoryColor = categoryConfig?.color || '#E4007C';
                                return (
                                    <div 
                                        key={idx} 
                                        className="flex items-center gap-2 px-4 py-2 rounded-xl bg-white/5 border border-white/10 hover:border-white/30 transition-all group"
                                        style={{ borderColor: `${categoryColor}40` }}
                                    >
                                        {CategoryIcon && (
                                            <CategoryIcon size={18} color={categoryColor} className="group-hover:scale-110 transition-transform" />
                                        )}
                                        <span className="text-white font-medium text-sm">{categoryName}</span>
                                        <span className="text-[#A6A6A6] text-xs">({item.count})</span>
                                    </div>
                                );
                            })}
                            {(!statistics.topCategories || statistics.topCategories.length === 0) && (
                                <span className="text-[#A6A6A6] text-sm">데이터가 없습니다.</span>
                            )}
                        </div>
                    </div>

                    {/* 나의 취향 키워드 */}
                    <div className="bg-[#1a192e] p-6 rounded-2xl border border-white/10 mb-8">
                        <div className="text-[#A6A6A6] text-xs mb-4 font-medium">나의 취향 키워드</div>
                        <div className="flex flex-wrap gap-2">
                            {statistics.topKeywords?.slice(0, 5).map((item, idx) => (
                                <span 
                                    key={idx} 
                                    className="px-4 py-2 rounded-full bg-gradient-to-r from-[#0CFED8]/20 to-[#9D4EDC]/20 border border-[#0CFED8]/30 text-[#0CFED8] font-medium text-sm hover:scale-105 transition-transform cursor-default"
                                >
                                    #{item.keyword?.name || 'N/A'}
                                    <span className="text-[#A6A6A6] ml-1">({item.count})</span>
                                </span>
                            ))}
                            {(!statistics.topKeywords || statistics.topKeywords.length === 0) && (
                                <span className="text-[#A6A6A6] text-sm">데이터가 없습니다.</span>
                            )}
                        </div>
                    </div>
                </>
            )}

            {/* 내 카드 아카이브 */}
            <div className="mb-4">
                <h3 className="text-lg font-bold text-white mb-4">My Archive</h3>
                <div className="flex gap-2 mb-6">
                    <button
                        onClick={() => setActiveTab('my')}
                        className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                            activeTab === 'my' 
                                ? 'bg-[#E4007C] text-white' 
                                : 'bg-white/5 text-[#A6A6A6] hover:bg-white/10'
                        }`}
                    >
                        내가 만든 카드 ({myCards.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('collected')}
                        className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                            activeTab === 'collected' 
                                ? 'bg-[#E4007C] text-white' 
                                : 'bg-white/5 text-[#A6A6A6] hover:bg-white/10'
                        }`}
                    >
                        내가 수집한 카드 ({collectedCards.length})
                    </button>
                </div>
            </div>

            {activeTab === 'my' && (
                myCards.length === 0 ? (
                    <div className="text-center text-[#A6A6A6] py-12">아직 생성한 카드가 없습니다.</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {myCards.map(card => {
                            const cardData = {
                                id: card.id,
                                category: card.category?.name || '',
                                subCategory: card.subCategory?.name || '',
                                title: card.name || '',
                                desc: card.description || '',
                                level: card.boomLevel || 3,
                                author: card.user?.nickname || user?.nickname || 'User',
                                authorProfileUrl: card.user?.profileImageUrl || user?.profileImageUrl || 'https://randomuser.me/api/portraits/lego/1.jpg',
                                boomUp: card.boomUpCount || 0,
                                keywords: card.keywords?.map(k => k.name) || [],
                                imageUrl: card.imageUrl || null,
                                isBoomUpped: card.isBoomUpped || false,
                                isCollected: card.isCollected || false,
                                userId: card.user?.id,
                            };
                            return (
                                <BoomCard 
                                    key={card.id} 
                                    data={cardData} 
                                    onUpdate={loadData}
                                    isOwner={true}
                                    onEdit={(cardData) => {
                                        setEditingCard(card);
                                        setShowEditCard(true);
                                    }}
                                />
                            );
                        })}
                    </div>
                )
            )}

            {activeTab === 'collected' && (
                collectedCards.length === 0 ? (
                    <div className="text-center text-[#A6A6A6] py-12">아직 수집한 카드가 없습니다.</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {collectedCards.map(card => {
                            const cardData = {
                                id: card.id,
                                category: card.category?.name || '',
                                subCategory: card.subCategory?.name || '',
                                title: card.name || '',
                                desc: card.description || '',
                                level: card.boomLevel || 3,
                                author: card.user?.nickname || 'User',
                                authorProfileUrl: card.user?.profileImageUrl || 'https://randomuser.me/api/portraits/lego/1.jpg',
                                boomUp: card.boomUpCount || 0,
                                keywords: card.keywords?.map(k => k.name) || [],
                                imageUrl: card.imageUrl || null,
                                isBoomUpped: card.isBoomUpped || false,
                                isCollected: true,
                                userId: card.user?.id,
                            };
                            return <BoomCard key={card.id} data={cardData} onUpdate={loadData} />;
                        })}
                    </div>
                )
            )}

            {/* 프로필 수정 모달 */}
            {showEditProfile && (
                <EditProfile 
                    onClose={() => setShowEditProfile(false)} 
                    onUpdate={() => {
                        loadData();
                        setShowEditProfile(false);
                    }}
                />
            )}

            {/* 카드 수정 모달 */}
            {showEditCard && editingCard && (
                <CreateBoomModal 
                    onClose={() => {
                        setShowEditCard(false);
                        setEditingCard(null);
                    }}
                    editCard={editingCard}
                    onUpdate={() => {
                        loadData();
                        setShowEditCard(false);
                        setEditingCard(null);
                    }}
                />
            )}
        </div>
    );
};

export default MyBoom;