import React, { useState, useEffect } from 'react';
import { X, Save } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { userService } from '../services/user';

const EditProfile = ({ onClose, onUpdate }) => {
    const { user, updateUser } = useAuth();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [formData, setFormData] = useState({
        nickname: '',
        bio: '',
        profileImageUrl: '',
    });

    useEffect(() => {
        loadProfile();
    }, []);

    const loadProfile = async () => {
        try {
            const response = await userService.getMyProfile();
            if (response?.success) {
                const profile = response.data;
                setFormData({
                    nickname: profile.nickname || '',
                    bio: profile.bio || '',
                    profileImageUrl: profile.profileImageUrl || '',
                });
            }
        } catch (error) {
            console.error('프로필 로딩 실패:', error);
            setError('프로필을 불러오는데 실패했습니다.');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const updateData = {
                nickname: formData.nickname || undefined,
                bio: formData.bio || undefined,
                profileImageUrl: formData.profileImageUrl || undefined,
            };

            const response = await userService.updateMyProfile(updateData);
            if (response?.success) {
                updateUser(response.data);
                onUpdate?.();
                onClose?.();
            } else {
                setError(response?.message || '프로필 수정에 실패했습니다.');
            }
        } catch (err) {
            console.error('프로필 수정 실패:', err);
            let errorMessage = '프로필 수정 중 오류가 발생했습니다.';
            
            if (err.response) {
                const data = err.response.data;
                if (data && data.message) {
                    errorMessage = data.message;
                } else if (err.response.status === 400) {
                    errorMessage = '입력 정보를 확인해주세요.';
                }
            }
            
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="fixed inset-0 z-[60] flex items-center justify-center bg-black/80 backdrop-blur-sm p-4 overflow-y-auto">
            <div className="bg-[#1a192e] w-full max-w-lg rounded-3xl p-6 border border-white/10 shadow-2xl relative">
                <button 
                    onClick={onClose} 
                    className="absolute top-4 right-4 text-[#A6A6A6] hover:text-white z-10"
                >
                    <X />
                </button>

                <h2 className="text-2xl font-bold text-white mb-6">프로필 수정</h2>

                {error && (
                    <div className="mb-4 p-3 bg-red-500/20 border border-red-500/50 rounded-lg text-red-400 text-sm">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-[#A6A6A6] text-sm mb-2">닉네임</label>
                        <input
                            type="text"
                            value={formData.nickname}
                            onChange={(e) => setFormData({ ...formData, nickname: e.target.value })}
                            placeholder="닉네임을 입력하세요"
                            maxLength={50}
                            className="w-full bg-white/5 border border-white/10 rounded-xl text-white p-3 focus:border-[#E4007C] outline-none"
                        />
                    </div>

                    <div>
                        <label className="block text-[#A6A6A6] text-sm mb-2">자기소개</label>
                        <textarea
                            value={formData.bio}
                            onChange={(e) => setFormData({ ...formData, bio: e.target.value })}
                            placeholder="자기소개를 입력하세요"
                            maxLength={500}
                            rows={4}
                            className="w-full bg-white/5 border border-white/10 rounded-xl text-white p-3 focus:border-[#E4007C] outline-none resize-none"
                        />
                        <div className="text-right text-xs text-[#A6A6A6] mt-1">
                            {formData.bio.length}/500
                        </div>
                    </div>

                    <div>
                        <label className="block text-[#A6A6A6] text-sm mb-2">프로필 이미지 URL</label>
                        <input
                            type="url"
                            value={formData.profileImageUrl}
                            onChange={(e) => setFormData({ ...formData, profileImageUrl: e.target.value })}
                            placeholder="https://example.com/image.jpg"
                            maxLength={500}
                            className="w-full bg-white/5 border border-white/10 rounded-xl text-white p-3 focus:border-[#E4007C] outline-none"
                        />
                        {formData.profileImageUrl && (
                            <div className="mt-3">
                                <div className="text-[#A6A6A6] text-xs mb-2">미리보기</div>
                                <div className="w-20 h-20 rounded-full bg-gradient-to-br from-[#0CFED8] to-[#9D4EDC] flex items-center justify-center overflow-hidden border-2 border-white/20 relative">
                                    <img 
                                        src={formData.profileImageUrl} 
                                        alt="프로필 미리보기" 
                                        className="w-full h-full object-cover"
                                        onError={(e) => {
                                            e.target.style.display = 'none';
                                        }}
                                    />
                                    <div className={`absolute inset-0 flex items-center justify-center text-[#0D0C1D] font-bold text-xl ${formData.profileImageUrl ? '' : 'hidden'}`}>
                                        {(formData.nickname || 'U').charAt(0).toUpperCase()}
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>

                    <div className="flex gap-3 pt-4">
                        <button
                            type="button"
                            onClick={onClose}
                            className="flex-1 py-3 rounded-xl bg-white/10 text-[#A6A6A6] font-bold hover:bg-white/20 hover:text-white transition-all"
                        >
                            취소
                        </button>
                        <button
                            type="submit"
                            disabled={loading}
                            className="flex-1 flex items-center justify-center gap-2 py-3 rounded-xl text-white font-bold bg-gradient-to-r from-[#E4007C] to-[#9D4EDC] hover:opacity-90 transition-opacity disabled:opacity-50"
                        >
                            {loading ? '저장 중...' : (
                                <>
                                    <Save size={18} />
                                    저장
                                </>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EditProfile;

