// src/components/BoomCard.jsx

import React, { useState, useRef, useEffect } from 'react';
import { ThumbsUp, Bookmark, MoreVertical, Edit, Trash2 } from 'lucide-react';
import { CATEGORY_CONFIG } from '../data/constants'; // 상수 불러오기
import { boomupService } from '../services/boomup';
import { collectionService } from '../services/collection';
import { cardService } from '../services/card';
import { useAuth } from '../context/AuthContext';

const BoomCard = ({ data, onUpdate, onUserClick, isOwner = false, onEdit }) => {
    const [isFlipped, setIsFlipped] = useState(false);
    const [isBoomUpped, setIsBoomUpped] = useState(data.isBoomUpped || false);
    const [isCollected, setIsCollected] = useState(data.isCollected || false);
    const [boomUpCount, setBoomUpCount] = useState(data.boomUp || 0);
    const [loading, setLoading] = useState(false);
    const [showMenu, setShowMenu] = useState(false);
    const menuRef = useRef(null);
    const { user } = useAuth();

    // 메뉴 외부 클릭 시 닫기
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setShowMenu(false);
            }
        };
        if (showMenu) {
            document.addEventListener('mousedown', handleClickOutside);
        }
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [showMenu]);

    const handleUserClick = (e) => {
        e.stopPropagation();
        if (data.userId && onUserClick) {
            onUserClick(data.userId);
        }
    };

    // 카테고리에 맞는 색상 및 아이콘 가져오기
    const config = CATEGORY_CONFIG[data.category];
    const categoryColor = config?.color || '#E4007C';
    const CategoryIcon = config?.icon;

    // BoomUP 핸들러
    const handleBoomUp = async (e) => {
        e.stopPropagation();
        if (!user || loading) return;

        try {
            setLoading(true);
            if (isBoomUpped) {
                await boomupService.removeBoomUp(data.id);
                setIsBoomUpped(false);
                setBoomUpCount(prev => Math.max(0, prev - 1));
            } else {
                await boomupService.addBoomUp(data.id);
                setIsBoomUpped(true);
                setBoomUpCount(prev => prev + 1);
            }
            onUpdate?.();
        } catch (error) {
            console.error('BoomUP 실패:', error);
            alert(error.response?.data?.message || 'BoomUP 처리 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    // 수집 핸들러
    const handleCollect = async (e) => {
        e.stopPropagation();
        if (!user || loading) return;

        try {
            setLoading(true);
            if (isCollected) {
                await collectionService.uncollectCard(data.id);
                setIsCollected(false);
            } else {
                await collectionService.collectCard(data.id);
                setIsCollected(true);
            }
            onUpdate?.();
        } catch (error) {
            console.error('수집 실패:', error);
            alert(error.response?.data?.message || '수집 처리 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    // 삭제 핸들러
    const handleDelete = async (e) => {
        e.stopPropagation();
        if (!window.confirm('정말 이 카드를 삭제하시겠습니까?')) {
            return;
        }

        try {
            setLoading(true);
            await cardService.deleteCard(data.id);
            alert('카드가 삭제되었습니다.');
            onUpdate?.();
        } catch (error) {
            console.error('카드 삭제 실패:', error);
            alert(error.response?.data?.message || '카드 삭제 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
            setShowMenu(false);
        }
    };

    // 수정 핸들러
    const handleEdit = (e) => {
        e.stopPropagation();
        setShowMenu(false);
        if (onEdit) {
            onEdit(data);
        }
    };

    return (
        <div
            className="relative w-full h-[360px] cursor-pointer perspective-1000 group" // 높이를 340px -> 360px로 살짝 늘려 여유 확보
            onClick={() => setIsFlipped(!isFlipped)}
        >
            <div
                className={`relative w-full h-full transition-transform duration-700 preserve-3d ${isFlipped ? 'rotate-y-180' : ''}`}
                style={{ transformStyle: 'preserve-3d' }}
            >

                {/* [앞면] */}
                <div
                    className={`absolute w-full h-full backface-hidden bg-white/5 backdrop-blur-md border rounded-2xl p-5 flex flex-col justify-between shadow-lg transition-colors duration-300`}
                    style={{
                        backfaceVisibility: 'hidden',
                        borderColor: `${categoryColor}4D` // 투명도 30%
                    }}
                >
                    {/* 상단 그룹 */}
                    <div>
                        {/* 카테고리 아이콘과 점 3개 버튼 */}
                        <div className="flex justify-between items-center mb-2">
                            <div>
                                {CategoryIcon && <CategoryIcon size={28} color={categoryColor} />}
                            </div>
                            {isOwner && (
                                <div className="relative" ref={menuRef}>
                                    <button
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            setShowMenu(!showMenu);
                                        }}
                                        className="text-[#A6A6A6] hover:text-white transition-colors cursor-pointer"
                                    >
                                        <MoreVertical size={20} />
                                    </button>
                                    {showMenu && (
                                        <div className="absolute top-6 right-0 bg-[#1a192e] border border-white/10 rounded-lg shadow-xl z-50 min-w-[120px] overflow-hidden">
                                            <button
                                                onClick={handleEdit}
                                                className="w-full px-4 py-2 text-left text-sm text-white hover:bg-white/10 flex items-center gap-2 transition-colors"
                                            >
                                                <Edit size={16} />
                                                수정
                                            </button>
                                            <button
                                                onClick={handleDelete}
                                                disabled={loading}
                                                className="w-full px-4 py-2 text-left text-sm text-red-400 hover:bg-white/10 flex items-center gap-2 transition-colors disabled:opacity-50"
                                            >
                                                <Trash2 size={16} />
                                                삭제
                                            </button>
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>

                        <div className="flex justify-between items-start mb-2">
                            <span
                                className="text-xs font-bold px-2 py-1 rounded-full bg-white/10 tracking-wider"
                                style={{ color: categoryColor }}
                            >
                                {data.category} {data.subCategory && `| ${data.subCategory}`}
                            </span>
                            <div className="flex items-center gap-3">
                                <button
                                    onClick={handleBoomUp}
                                    disabled={!user || loading}
                                    className={`flex items-center gap-1.5 text-base transition-colors ${
                                        isBoomUpped 
                                            ? 'text-[#9D4EDC]' 
                                            : 'text-[#A6A6A6] group-hover:text-white'
                                    } ${!user ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}`}
                                >
                                    <ThumbsUp size={20} fill={isBoomUpped ? 'currentColor' : 'none'} /> 
                                    <span className="text-sm font-medium">{boomUpCount}</span>
                                </button>
                                {user && (
                                    <button
                                        onClick={handleCollect}
                                        disabled={loading}
                                        className={`transition-colors ${
                                            isCollected 
                                                ? 'text-[#9D4EDC]' 
                                                : 'text-[#A6A6A6] group-hover:text-white'
                                        } cursor-pointer`}
                                    >
                                        <Bookmark size={20} fill={isCollected ? 'currentColor' : 'none'} />
                                    </button>
                                )}
                            </div>
                        </div>

                        <h3 className="text-xl font-bold text-[#F4F4F3] mb-2 leading-tight line-clamp-2">{data.title}</h3>
                        <p className="text-[#A6A6A6] text-sm line-clamp-3">{data.desc}</p>
                    </div>

                    {/* 하단 그룹 */}
                    <div>
                        {/* 키워드 (Boom Level 바로 위) */}
                        {data.keywords && data.keywords.length > 0 && (
                            <div className="flex flex-wrap gap-1.5 mb-4">
                                {data.keywords.map((k, i) => (
                                    <span key={i} className="text-[10px] text-[#F4F4F3] bg-white/5 border border-white/10 px-2 py-1 rounded-md">
                                        #{k}
                                    </span>
                                ))}
                            </div>
                        )}
                        
                        {/* Boom Level Bar */}
                        <div className="w-full bg-gray-700 h-1.5 rounded-full overflow-hidden">
                            <div
                                className="h-full transition-all duration-500"
                                style={{
                                    width: `${data.level * 20}%`,
                                    backgroundColor: categoryColor,
                                    boxShadow: `0 0 10px ${categoryColor}`
                                }}
                            />
                        </div>
                        <div className="flex justify-between mt-1 text-[10px] text-[#A6A6A6]">
                            <span>Boom Level</span>
                            <span style={{ color: categoryColor }}>{data.level}/5</span>
                        </div>
                    </div>
                </div>

                {/* [뒷면] */}
                <div
                    className="absolute w-full h-full backface-hidden bg-[#0D0C1D] rounded-2xl overflow-hidden border shadow-xl"
                    style={{
                        backfaceVisibility: 'hidden',
                        transform: 'rotateY(180deg)',
                        borderColor: categoryColor
                    }}
                >
                    {data.imageUrl ? (
                        <img
                            src={data.imageUrl}
                            alt="Boom Content"
                            className="w-full h-full object-cover opacity-80 group-hover:opacity-100 transition-opacity"
                        />
                    ) : (
                        <div className="w-full h-full flex items-center justify-center text-[#A6A6A6]">No Image</div>
                    )}

                    {/* 뒷면 내용 오버레이 */}
                    <div className="absolute inset-0 bg-gradient-to-t from-[#0D0C1D] via-transparent to-transparent flex flex-col justify-end p-5">
                        <h3 className="text-white font-bold text-xl shadow-black drop-shadow-md mb-2 leading-tight">{data.title}</h3>

                        {/* (NEW) 작성자 프로필 */}
                        <button
                            onClick={handleUserClick}
                            className="flex items-center gap-2 text-white/90 hover:text-white transition-colors cursor-pointer"
                            disabled={!data.userId || !onUserClick}
                        >
                            <img
                                src={data.authorProfileUrl || 'https://randomuser.me/api/portraits/lego/1.jpg'} // 데이터 없으면 기본값
                                alt={data.author}
                                className="w-6 h-6 rounded-full border border-white/50 object-cover"
                            />
                            <span className="text-xs font-medium">by {data.author || 'User'}</span>
                        </button>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default BoomCard;