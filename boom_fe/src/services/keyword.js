import api from './api';

export const keywordService = {
  // 전체 키워드 조회
  getKeywords: async () => {
    const response = await api.get('/keywords');
    return response.data;
  },

  // 타입별 키워드 조회
  getKeywordsByType: async (type) => {
    const response = await api.get(`/keywords/type/${type}`);
    return response.data;
  },
};

