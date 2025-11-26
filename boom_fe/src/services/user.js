import api from './api';

export const userService = {
  // 내 프로필 조회
  getMyProfile: async () => {
    const response = await api.get('/users/me');
    return response.data;
  },

  // 프로필 수정
  updateMyProfile: async (profileData) => {
    const response = await api.put('/users/me', profileData);
    return response.data;
  },

  // 다른 사용자 프로필 조회
  getUserProfile: async (userId) => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
  },

  // 취향 유사도 조회
  getSimilarity: async (userId) => {
    const response = await api.get(`/users/${userId}/similarity`);
    return response.data;
  },

  // 취향 통계 조회
  getStatistics: async (userId) => {
    const response = await api.get(`/users/${userId}/statistics`);
    return response.data;
  },
};

