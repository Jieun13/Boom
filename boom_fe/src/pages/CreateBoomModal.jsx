import React, { useState, useRef, useEffect } from 'react';
import { X, Image as ImageIcon, Check, ChevronRight, Download, ThumbsUp } from 'lucide-react';
import html2canvas from 'html2canvas';
import { CATEGORY_CONFIG, CATEGORY_DATA, KEYWORD_GROUPS } from '../data/constants';
import BoomCard from '../components/BoomCard'; // 미리보기용 컴포넌트
import { categoryService } from '../services/category';
import { keywordService } from '../services/keyword';
import { cardService } from '../services/card';
import { useAuth } from '../context/AuthContext';

const CreateBoomModal = ({ onClose, editCard = null, onUpdate }) => {
    // Step 관리: 1(카테고리) -> 2(소분류) -> 3(키워드) -> 4(내용) -> 5(레벨) -> 6(완료/다운로드)
    const [step, setStep] = useState(1);
    const { user } = useAuth();
    const isEditMode = !!editCard;

    // exportRef: 실제 다운로드될 (숨겨진) 고해상도 영역을 가리키는 참조
    const exportRef = useRef(null);

    // API 데이터
    const [categories, setCategories] = useState([]);
    const [subCategories, setSubCategories] = useState([]);
    const [keywords, setKeywords] = useState({ feeling: [], action: [], tendency: [] });
    const [loading, setLoading] = useState(false);

    // 입력 데이터 상태
    const [formData, setFormData] = useState({
        categoryId: null,
        category: '',
        subCategoryId: null,
        subCategory: '',
        keywordIds: [],
        keywords: { feeling: '', action: '', tendency: '' },
        title: '',
        desc: '',
        level: 3,
        image: null,
        tempImageUrl: null
    });

    const [keywordStep, setKeywordStep] = useState(0);

    // 카테고리 및 키워드 로드
    useEffect(() => {
        loadCategories();
        loadKeywords();
    }, []);

    // 수정 모드일 때 기존 데이터 로드
    useEffect(() => {
        if (editCard) {
            const feelingKeyword = editCard.keywords?.find(k => k.type === 'FEELING');
            const actionKeyword = editCard.keywords?.find(k => k.type === 'ACTION');
            const tendencyKeyword = editCard.keywords?.find(k => k.type === 'TENDENCY');
            
            setFormData({
                categoryId: editCard.category?.id || null,
                category: editCard.category?.name || '',
                subCategoryId: editCard.subCategory?.id || null,
                subCategory: editCard.subCategory?.name || '',
                keywordIds: editCard.keywords?.map(k => k.id) || [],
                keywords: {
                    feeling: feelingKeyword?.name || '',
                    action: actionKeyword?.name || '',
                    tendency: tendencyKeyword?.name || ''
                },
                title: editCard.name || '',
                desc: editCard.description || '',
                level: editCard.boomLevel || 3,
                image: null,
                tempImageUrl: editCard.imageUrl || null
            });
            
            if (editCard.category?.id) {
                loadSubCategories(editCard.category.id);
            }
            
            // 키워드가 모두 선택되었으면 step 3에서 다음 단계로 이동 가능하도록 설정
            if (feelingKeyword && actionKeyword && tendencyKeyword) {
                setKeywordStep(2);
            } else if (feelingKeyword && actionKeyword) {
                setKeywordStep(2);
            } else if (feelingKeyword) {
                setKeywordStep(1);
            }
            
            // 수정 모드일 때는 내용 입력 단계(step 4)부터 시작
            setStep(4);
        }
    }, [editCard]);

    const loadCategories = async () => {
        try {
            const response = await categoryService.getCategories();
            if (response?.success) {
                setCategories(response.data || []);
            }
        } catch (error) {
            console.error('카테고리 로딩 실패:', error);
            // 실패 시 상수 데이터 사용
        }
    };

    const loadKeywords = async () => {
        try {
            const [feelingRes, actionRes, tendencyRes] = await Promise.all([
                keywordService.getKeywordsByType('FEELING'),
                keywordService.getKeywordsByType('ACTION'),
                keywordService.getKeywordsByType('TENDENCY'),
            ]);

            if (feelingRes?.success) {
                setKeywords(prev => ({ ...prev, feeling: feelingRes.data || [] }));
            }
            if (actionRes?.success) {
                setKeywords(prev => ({ ...prev, action: actionRes.data || [] }));
            }
            if (tendencyRes?.success) {
                setKeywords(prev => ({ ...prev, tendency: tendencyRes.data || [] }));
            }
        } catch (error) {
            console.error('키워드 로딩 실패:', error);
            // 실패 시 상수 데이터 사용
        }
    };

    const loadSubCategories = async (categoryId) => {
        try {
            const response = await categoryService.getSubCategories(categoryId);
            if (response?.success) {
                setSubCategories(response.data || []);
            }
        } catch (error) {
            console.error('소분류 로딩 실패:', error);
        }
    };

    // --- 핸들러 함수들 ---

    const handleCategorySelect = async (category) => {
        const categoryObj = categories.find(c => c.name === category) || 
                           Object.keys(CATEGORY_CONFIG).find(c => c === category);
        const categoryId = categories.find(c => c.name === category)?.id;
        
        setFormData({ 
            ...formData, 
            category: category,
            categoryId: categoryId,
            subCategory: '',
            subCategoryId: null
        });
        
        if (categoryId) {
            await loadSubCategories(categoryId);
        }
        setStep(2);
    };

    const handleSubCatSelect = (subCategory) => {
        const subCategoryObj = subCategories.find(s => s.name === subCategory);
        setFormData({ 
            ...formData, 
            subCategory: subCategory,
            subCategoryId: subCategoryObj?.id || null
        });
        setStep(3);
    };

    const handleKeywordSelect = (type, keyword) => {
        const keywordObj = keywords[type]?.find(k => k.name === keyword);
        const newKeywords = { ...formData.keywords, [type]: keyword };
        const newKeywordIds = [...formData.keywordIds];
        
        // 기존에 선택된 같은 타입의 키워드 제거
        const existingKeyword = keywords[type]?.find(k => 
            formData.keywordIds.includes(k.id)
        );
        if (existingKeyword) {
            const index = newKeywordIds.indexOf(existingKeyword.id);
            if (index > -1) newKeywordIds.splice(index, 1);
        }
        
        // 새 키워드 추가
        if (keywordObj) {
            newKeywordIds.push(keywordObj.id);
        }
        
        setFormData({ 
            ...formData, 
            keywords: newKeywords,
            keywordIds: newKeywordIds
        });

        if (type === 'feeling') setKeywordStep(1);
        else if (type === 'action') setKeywordStep(2);
    };

    // 카드 생성/수정 핸들러
    const handleCreateCard = async () => {
        if (!formData.categoryId || !formData.title || formData.keywordIds.length === 0) {
            alert('필수 항목을 모두 입력해주세요.');
            return;
        }

        try {
            setLoading(true);
            const cardData = {
                categoryId: formData.categoryId,
                subCategoryId: formData.subCategoryId || null,
                name: formData.title,
                description: formData.desc || null,
                imageUrl: formData.tempImageUrl && formData.tempImageUrl.trim() ? formData.tempImageUrl.trim() : null,
                boomLevel: formData.level,
                keywordIds: formData.keywordIds.slice(0, 3), // 최대 3개
            };

            let response;
            if (isEditMode) {
                // 수정 모드
                response = await cardService.updateCard(editCard.id, cardData);
                if (response?.success) {
                    alert('카드가 수정되었습니다.');
                    onUpdate?.();
                    onClose();
                } else {
                    alert(response?.message || '카드 수정에 실패했습니다.');
                }
            } else {
                // 생성 모드
                response = await cardService.createCard(cardData);
                if (response?.success) {
                    // 생성된 카드 데이터로 finalData 업데이트
                    const createdCard = response.data;
                    setFormData(prev => ({
                        ...prev,
                        tempImageUrl: createdCard.imageUrl || prev.tempImageUrl
                    }));
                    setStep(6);
                } else {
                    alert(response?.message || '카드 생성에 실패했습니다.');
                }
            }
        } catch (error) {
            console.error(isEditMode ? '카드 수정 실패:' : '카드 생성 실패:', error);
            alert(error.response?.data?.message || (isEditMode ? '카드 수정 중 오류가 발생했습니다.' : '카드 생성 중 오류가 발생했습니다.'));
        } finally {
            setLoading(false);
        }
    };

    // 이미지 다운로드 핸들러
    const handleDownload = async () => {
        if (!exportRef.current) return;

        try {
            // 숨겨진 영역(exportRef)을 캡처
            const canvas = await html2canvas(exportRef.current, {
                backgroundColor: null, // 투명 배경
                scale: 2, // 레티나 디스플레이 대응 및 고해상도
                useCORS: true, // 외부 이미지 로딩 허용
                logging: false,
            });

            const link = document.createElement('a');
            link.download = `boom-card-${formData.title.replace(/\s+/g, '-').toLowerCase() || 'new'}.png`;
            link.href = canvas.toDataURL('image/png');
            link.click();
        } catch (err) {
            console.error("카드 이미지 저장 실패:", err);
            alert("이미지 저장 중 오류가 발생했습니다.");
        }
    };

    // 현재 테마 컬러
    const themeColor = formData.category ? CATEGORY_CONFIG[formData.category].color : '#E4007C';
    const totalSteps = 6;

    // 최종 데이터 객체 (미리보기 및 다운로드용)
    const finalData = {
        category: formData.category,
        subCategory: formData.subCategory,
        keywords: [formData.keywords.feeling, formData.keywords.action, formData.keywords.tendency].filter(Boolean),
        title: formData.title || '제목 없음',
        desc: formData.desc || '내용이 없습니다.',
        level: formData.level,
        imageUrl: formData.tempImageUrl || 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop',
        boomUp: 0,
        // 작성자 정보
        author: user?.nickname || 'User',
        authorProfileUrl: user?.profileImageUrl || 'https://randomuser.me/api/portraits/lego/1.jpg'
    };


    return (
        <div className="fixed inset-0 z-[60] flex items-center justify-center bg-black/80 backdrop-blur-sm p-4 overflow-y-auto">
            <div className={`bg-[#1a192e] w-full max-w-lg rounded-3xl p-6 border border-white/10 shadow-2xl relative overflow-hidden flex flex-col transition-all duration-300 ${step === 6 ? 'max-w-md' : 'max-h-[90vh]'}`}>

                {/* 배경 효과 */}
                <div
                    className="absolute top-0 right-0 w-40 h-40 opacity-10 blur-[60px] transition-colors duration-500 pointer-events-none"
                    style={{ backgroundColor: themeColor }}
                />

                {step < 6 && (
                    <button onClick={onClose} className="absolute top-4 right-4 text-[#A6A6A6] hover:text-white z-10"><X /></button>
                )}
                
                {isEditMode && (
                    <div className="mb-2 text-sm text-[#0CFED8] font-bold">카드 수정</div>
                )}

                {/* 진행 바 */}
                {step < 6 && (
                    <div className="mt-2 mb-6">
                        <div className="text-sm font-bold mb-1 transition-colors" style={{ color: themeColor }}>
                            STEP {step}/{totalSteps-1}
                        </div>
                        <div className="h-1 w-full bg-gray-700 rounded-full">
                            <div
                                className="h-full transition-all duration-300"
                                style={{ width: `${step * (100/(totalSteps-1))}%`, backgroundColor: themeColor }}
                            />
                        </div>
                    </div>
                )}

                {/* 컨텐츠 영역 */}
                <div className={`flex-1 ${step < 6 ? 'overflow-y-auto custom-scrollbar pr-2' : ''}`}>

                    {/* STEP 1: 카테고리 */}
                    {step === 1 && (
                        <div className="animate-fadeIn">
                            <h2 className="text-2xl font-bold text-white mb-6">어떤 분야의<br/>취향인가요?</h2>
                            <div className="grid grid-cols-2 gap-3">
                                {(categories.length > 0 ? categories : Object.keys(CATEGORY_CONFIG)).map(cat => {
                                    const catName = typeof cat === 'object' ? cat.name : cat;
                                    const Icon = CATEGORY_CONFIG[catName]?.icon;
                                    const color = CATEGORY_CONFIG[catName]?.color || '#E4007C';
                                    const isSelected = formData.category === catName;
                                    return (
                                        <button key={catName} onClick={() => handleCategorySelect(catName)}
                                                className={`p-4 rounded-xl border text-white hover:bg-white/10 transition-all text-left flex items-center gap-3 group ${
                                                    isSelected 
                                                        ? 'bg-white/10 border-white/30' 
                                                        : 'bg-white/5 border-white/10'
                                                }`}>
                                            {Icon && (
                                                <div className="p-2 rounded-lg bg-black/20 group-hover:scale-110 transition-transform" style={{ color }}>
                                                    <Icon size={20} />
                                                </div>
                                            )}
                                            <span className="font-medium">{catName}</span>
                                            {isSelected && <Check size={16} className="ml-auto text-[#0CFED8]" />}
                                        </button>
                                    )
                                })}
                            </div>
                        </div>
                    )}

                    {/* STEP 2: 소분류 */}
                    {step === 2 && (
                        <div className="animate-fadeIn">
                            <h2 className="text-2xl font-bold text-white mb-2">더 구체적으로<br/>알려주세요.</h2>
                            <p className="text-[#A6A6A6] text-sm mb-6">{formData.category} &gt; ...</p>
                            <div className="flex flex-wrap gap-2">
                                {(subCategories.length > 0 
                                    ? subCategories.map(s => s.name)
                                    : (CATEGORY_DATA[formData.category] || [])
                                ).map(sub => {
                                    const isSelected = formData.subCategory === sub;
                                    return (
                                        <button key={sub} onClick={() => handleSubCatSelect(sub)}
                                                className={`px-5 py-3 rounded-full border text-[#F4F4F3] hover:bg-white/10 hover:border-white/30 transition-all flex items-center gap-2 ${
                                                    isSelected 
                                                        ? 'bg-white/10 border-white/30' 
                                                        : 'bg-white/5 border-white/10'
                                                }`}>
                                            {sub}
                                            {isSelected && <Check size={14} className="text-[#0CFED8]" />}
                                        </button>
                                    );
                                })}
                            </div>
                        </div>
                    )}

                    {/* STEP 3: 키워드 */}
                    {step === 3 && (
                        <div className="animate-fadeIn">
                            <h2 className="text-2xl font-bold text-white mb-2">이 취향을<br/>키워드로 표현한다면?</h2>
                            <p className="text-[#A6A6A6] text-sm mb-6">각 단계별로 하나씩 선택해주세요.</p>

                            <div className="flex gap-2 mb-6 border-b border-white/10 pb-2">
                                {['느낌', '행동', '성향'].map((label, idx) => (
                                    <div key={label}
                                         onClick={() => setKeywordStep(idx)}
                                         className={`cursor-pointer text-sm px-3 py-1 rounded-md transition-colors ${keywordStep === idx ? 'text-white font-bold bg-white/10' : 'text-[#A6A6A6]'}`}>
                                        {idx + 1}. {label}
                                        {Object.values(formData.keywords)[idx] && <Check size={12} className="inline ml-1 text-[#0CFED8]"/>}
                                    </div>
                                ))}
                            </div>

                            <div className="min-h-[200px]">
                                {keywordStep === 0 && (
                                    <div className="animate-fadeIn">
                                        <div className="text-[#0CFED8] text-sm mb-3 font-bold">Q. 어떤 느낌인가요?</div>
                                        <div className="flex flex-wrap gap-2">
                                            {(keywords.feeling.length > 0 
                                                ? keywords.feeling.map(k => k.name)
                                                : KEYWORD_GROUPS.feeling.items
                                            ).map(k => {
                                                const isSelected = formData.keywords.feeling === k;
                                                return (
                                                    <button key={k} onClick={() => handleKeywordSelect('feeling', k)}
                                                            className={`px-3 py-2 rounded-lg text-xs border transition-all flex items-center gap-1 ${
                                                                isSelected ? 'text-white' : 'bg-white/5 border-transparent text-[#A6A6A6] hover:bg-white/10'
                                                            }`}
                                                            style={isSelected ? { borderColor: themeColor, backgroundColor: `${themeColor}33` } : {}}>
                                                        {k}
                                                        {isSelected && <Check size={12} className="text-[#0CFED8]" />}
                                                    </button>
                                                );
                                            })}
                                        </div>
                                    </div>
                                )}
                                {keywordStep === 1 && (
                                    <div className="animate-fadeIn">
                                        <div className="text-[#0CFED8] text-sm mb-3 font-bold">Q. 주로 어떻게 하나요?</div>
                                        <div className="flex flex-wrap gap-2">
                                            {(keywords.action.length > 0 
                                                ? keywords.action.map(k => k.name)
                                                : KEYWORD_GROUPS.action.items
                                            ).map(k => {
                                                const isSelected = formData.keywords.action === k;
                                                return (
                                                    <button key={k} onClick={() => handleKeywordSelect('action', k)}
                                                            className={`px-3 py-2 rounded-lg text-xs border transition-all flex items-center gap-1 ${
                                                                isSelected ? 'text-white' : 'bg-white/5 border-transparent text-[#A6A6A6] hover:bg-white/10'
                                                            }`}
                                                            style={isSelected ? { borderColor: themeColor, backgroundColor: `${themeColor}33` } : {}}>
                                                        {k}
                                                        {isSelected && <Check size={12} className="text-[#0CFED8]" />}
                                                    </button>
                                                );
                                            })}
                                        </div>
                                    </div>
                                )}
                                {keywordStep === 2 && (
                                    <div className="animate-fadeIn">
                                        <div className="text-[#0CFED8] text-sm mb-3 font-bold">Q. 나의 성향은?</div>
                                        <div className="flex flex-wrap gap-2">
                                            {(keywords.tendency.length > 0 
                                                ? keywords.tendency.map(k => k.name)
                                                : KEYWORD_GROUPS.tendency.items
                                            ).map(k => {
                                                const isSelected = formData.keywords.tendency === k;
                                                return (
                                                    <button key={k} onClick={() => handleKeywordSelect('tendency', k)}
                                                            className={`px-3 py-2 rounded-lg text-xs border transition-all flex items-center gap-1 ${
                                                                isSelected ? 'text-white' : 'bg-white/5 border-transparent text-[#A6A6A6] hover:bg-white/10'
                                                            }`}
                                                            style={isSelected ? { borderColor: themeColor, backgroundColor: `${themeColor}33` } : {}}>
                                                        {k}
                                                        {isSelected && <Check size={12} className="text-[#0CFED8]" />}
                                                    </button>
                                                );
                                            })}
                                        </div>
                                    </div>
                                )}
                            </div>

                            <div className="mt-6 flex justify-end">
                                {formData.keywords.feeling && formData.keywords.action && formData.keywords.tendency ? (
                                    <button onClick={() => setStep(4)} className="flex items-center gap-2 py-3 px-6 rounded-xl text-white font-bold transition-transform hover:scale-105" style={{ backgroundColor: themeColor }}>
                                        다음 단계 <ChevronRight size={18}/>
                                    </button>
                                ) : (
                                    <div className="text-[#A6A6A6] text-xs py-3">모든 키워드를 선택해주세요.</div>
                                )}
                            </div>
                        </div>
                    )}

                    {/* STEP 4: 내용 입력 */}
                    {step === 4 && (
                        <div className="animate-fadeIn">
                            <h2 className="text-2xl font-bold text-white mb-4">무엇인가요?<br/>소개해주세요.</h2>

                            <div className="mb-6">
                                <label className="block text-[#A6A6A6] text-xs mb-1">제목</label>
                                <input
                                    type="text" value={formData.title}
                                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                                    placeholder="예: NewJeans - Hype Boy"
                                    className="w-full bg-transparent border-b border-white/30 text-white text-lg p-2 focus:outline-none focus:border-white transition-colors placeholder:text-gray-600"
                                />
                            </div>

                            <div className="mb-6">
                                <label className="block text-[#A6A6A6] text-xs mb-1">설명</label>
                                <textarea
                                    value={formData.desc}
                                    onChange={(e) => setFormData({...formData, desc: e.target.value})}
                                    placeholder="왜 좋아하게 되었나요? 자유롭게 기록하세요." rows={3}
                                    className="w-full bg-white/5 rounded-xl text-white p-4 border border-white/10 focus:border-white outline-none resize-none placeholder:text-gray-600"
                                />
                            </div>

                            <div className="mb-6">
                                <label className="block text-[#A6A6A6] text-xs mb-1">이미지 URL (선택사항)</label>
                                <input
                                    type="url"
                                    value={formData.tempImageUrl || ''}
                                    onChange={(e) => setFormData({...formData, tempImageUrl: e.target.value})}
                                    placeholder="https://example.com/image.jpg"
                                    className="w-full bg-white/5 rounded-xl text-white p-4 border border-white/10 focus:border-white outline-none placeholder:text-gray-600"
                                />
                                {formData.tempImageUrl && (
                                    <div className="mt-3 relative w-full h-32 border border-white/10 rounded-xl overflow-hidden">
                                        <img 
                                            src={formData.tempImageUrl} 
                                            alt="preview" 
                                            className="w-full h-full object-cover"
                                            onError={(e) => {
                                                e.target.style.display = 'none';
                                                e.target.nextSibling.style.display = 'flex';
                                            }}
                                        />
                                        <div className="hidden absolute inset-0 items-center justify-center text-[#A6A6A6] text-xs bg-white/5">
                                            이미지를 불러올 수 없습니다
                                        </div>
                                    </div>
                                )}
                            </div>

                            <button onClick={() => setStep(5)} className="w-full mt-6 py-4 rounded-xl text-white font-bold" style={{ backgroundColor: themeColor }}>다음</button>
                        </div>
                    )}

                    {/* STEP 5: 레벨 설정 */}
                    {step === 5 && (
                        <div className="animate-fadeIn">
                            <h2 className="text-2xl font-bold text-white mb-6">얼마나 좋아하나요?<br/>Boom Level!</h2>
                            <div className="flex justify-center items-center h-32">
                                <div className="text-center">
                   <span className="text-6xl font-bold text-transparent bg-clip-text"
                         style={{ backgroundImage: `linear-gradient(to right, #F4F4F3, ${themeColor})` }}>
                     {formData.level}
                   </span>
                                    <span className="text-2xl text-[#A6A6A6]"> / 5</span>
                                    <p className="text-[#A6A6A6] mt-4 text-sm">
                                        {formData.level === 1 && "이제 막 관심을 가지기 시작했어요"}
                                        {formData.level === 3 && "꽤 자주 즐기고 좋아해요"}
                                        {formData.level === 5 && "나를 설명하는 정체성 그 자체!"}
                                    </p>
                                </div>
                            </div>
                            <div className="px-4 mt-8">
                                <input type="range" min="1" max="5" value={formData.level}
                                       onChange={(e) => setFormData({...formData, level: parseInt(e.target.value)})}
                                       className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer"
                                       style={{ accentColor: themeColor }}
                                />
                            </div>
                            <button 
                                onClick={handleCreateCard} 
                                disabled={loading}
                                className="w-full mt-10 py-4 rounded-xl text-white font-bold shadow-lg disabled:opacity-50" 
                                style={{ backgroundColor: themeColor }}
                            >
                                {loading ? (isEditMode ? '수정 중...' : '생성 중...') : (isEditMode ? '카드 수정하기' : '취향 카드 만들기')}
                            </button>
                        </div>
                    )}

                    {/* STEP 6: 미리보기 및 다운로드 */}
                    {step === 6 && (
                        <div className="animate-fadeIn flex flex-col items-center">
                            <h2 className="text-2xl font-bold text-white mb-2 text-center">짜잔! 🎉<br/>나의 Boom 카드가 완성됐어요.</h2>
                            <p className="text-[#A6A6A6] text-sm mb-6 text-center">이미지로 저장해서 친구들에게 공유해보세요!</p>

                            {/* [사용자에게 보이는 부분] : 3D 인터랙티브 카드 */}
                            <div className="w-full max-w-[340px] mb-6 transform hover:scale-[1.02] transition-transform duration-300">
                                <BoomCard data={finalData} />
                            </div>

                            {/* 액션 버튼들 */}
                            <div className="w-full flex gap-3">
                                <button onClick={handleDownload} className="flex-1 flex items-center justify-center gap-2 py-3 rounded-xl text-white font-bold transition-all hover:brightness-110" style={{ backgroundColor: themeColor }}>
                                    <Download size={20}/> 이미지 저장
                                </button>
                                <button onClick={() => {
                                    onClose();
                                    window.location.reload(); // 카드 목록 새로고침
                                }} className="flex-1 py-3 rounded-xl bg-white/10 text-[#A6A6A6] font-bold hover:bg-white/20 hover:text-white transition-all">
                                    닫기
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* ===================================================================================== */}
            {/* [숨겨진 캡처용 영역] : 다운로드 시에만 html2canvas에 의해 렌더링됩니다. */}
            {/* 상단 패딩(pt-2)을 줄여 내용을 위로 올리고, 하단 패딩(pb-8)을 늘려 하단 텍스트를 위로 밀어올림 */}
            {/* ===================================================================================== */}
            <div className="fixed top-[-10000px] left-[-10000px] pointer-events-none">

                {/* 전체 컨테이너 */}
                <div
                    ref={exportRef}
                    className="bg-[#0D0C1D] p-10 flex gap-8 items-center w-[930px] h-[550px]"
                >

                    {/* 2. 왼쪽: 앞면 정보 카드 (p-5 -> px-5 pb-5 pt-2 수정) */}
                    <div
                        className="w-[400px] h-[425px] bg-white/5 border rounded-3xl px-5 pb-5 pt-2 flex flex-col justify-between relative shadow-2xl"
                        style={{ borderColor: themeColor }}
                    >
                        {/* [상단 그룹] */}
                        <div>
                            {/* 카테고리 아이콘 */}
                            <div className="mb-2">
                                {(() => {
                                    const CategoryIcon = CATEGORY_CONFIG[finalData.category]?.icon;
                                    return CategoryIcon ? <CategoryIcon size={32} color={themeColor} /> : null;
                                })()}
                            </div>

                            {/* 카테고리 텍스트 */}
                            <div className="flex justify-between items-start mb-2">
                    <span className="text-sm font-bold px-3 py-1 rounded-full bg-white/10 tracking-wider" style={{ color: themeColor }}>
                        {finalData.category} {finalData.subCategory && `| ${finalData.subCategory}`}
                    </span>
                                <div className="flex items-center text-[#A6A6A6] text-sm">
                                    <ThumbsUp size={16} className="mr-1.5" /> 0
                                </div>
                            </div>

                            {/* 제목 */}
                            <h3 className="text-2xl font-bold text-[#F4F4F3] mb-6 leading-tight break-keep">
                                {finalData.title}
                            </h3>

                            {/* 설명 */}
                            <p className="text-[#A6A6A6] text-sm leading-snug mb-10">
                                {finalData.desc}
                            </p>
                        </div>

                        {/* [하단 그룹] */}
                        <div>
                            {/* 키워드 */}
                            <div className="flex flex-wrap gap-1.5 mb-3">
                                {finalData.keywords.map((k, i) => (
                                    <span key={i} className="text-xs text-[#F4F4F3] bg-white/5 border border-white/10 px-2 py-1 rounded-lg">
                          #{k}
                        </span>
                                ))}
                            </div>

                            {/* Boom Level Bar */}
                            <div className="w-full bg-gray-700 h-2 rounded-full overflow-hidden mb-2">
                                <div className="h-full" style={{ width: `${finalData.level * 20}%`, backgroundColor: themeColor }} />
                            </div>
                            <div className="flex justify-between text-xs text-[#A6A6A6] font-medium">
                                <span>Boom Level</span>
                                <span style={{ color: themeColor }}>{finalData.level}/5</span>
                            </div>
                        </div>
                    </div>


                    {/* 3. 오른쪽: 뒷면 이미지 카드 (p-5 -> px-5 pt-5 pb-8 수정) */}
                    <div
                        className="w-[400px] h-[425px] bg-[#1a192e] rounded-3xl overflow-hidden border relative shadow-2xl"
                        style={{ borderColor: themeColor }}
                    >
                        {finalData.imageUrl ? (
                            <img src={finalData.imageUrl} className="w-full h-full object-cover opacity-80" alt="back" />
                        ) : (
                            <div className="w-full h-full flex items-center justify-center text-[#A6A6A6] text-xl">No Image</div>
                        )}

                        {/* 제목 및 프로필 정보 */}
                        <div className="absolute inset-0 bg-gradient-to-t from-[#0D0C1D] via-transparent to-transparent flex flex-col justify-end px-5 pt-5 pb-8">
                            {/* 제목 */}
                            <h3 className="text-white font-bold text-2xl shadow-black drop-shadow-md break-keep mb-8">
                                {finalData.title}
                            </h3>

                            {/* 생성자 프로필 */}
                            <div className="flex items-center gap-2 text-white">
                                <img
                                    src={finalData.authorProfileUrl}
                                    alt={finalData.author}
                                    className="w-8 h-8 rounded-full border-2 border-white/50 object-cover"
                                />
                                <span className="text-sm font-medium">by {finalData.author}</span>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    );
}

export default CreateBoomModal;