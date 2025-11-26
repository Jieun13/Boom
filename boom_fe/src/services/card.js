import api from './api';

export const cardService = {
  // 카드 생성
  createCard: async (cardData) => {
    const response = await api.post('/cards', cardData);
    return response.data;
  },

  // 카드 상세 조회
  getCard: async (cardId) => {
    const response = await api.get(`/cards/${cardId}`);
    return response.data;
  },

  // 카드 수정
  updateCard: async (cardId, cardData) => {
    const response = await api.put(`/cards/${cardId}`, cardData);
    return response.data;
  },

  // 카드 삭제
  deleteCard: async (cardId) => {
    const response = await api.delete(`/cards/${cardId}`);
    return response.data;
  },

  // 내 카드 목록 조회
  getMyCards: async () => {
    const response = await api.get('/cards/me');
    return response.data;
  },

  // 사용자 카드 목록 조회
  getUserCards: async (userId) => {
    const response = await api.get(`/cards/users/${userId}`);
    return response.data;
  },

  // 최근 카드 조회
  getRecentCards: async () => {
    const response = await api.get('/cards/explore/recent');
    return response.data;
  },

  // 인기 카드 랭킹
  getRankingCards: async () => {
    const response = await api.get('/cards/explore/ranking');
    return response.data;
  },

  // 추천 카드
  getRecommendationCards: async () => {
    const response = await api.get('/cards/explore/recommendations');
    return response.data;
  },

  // 카드 이미지 다운로드
  downloadCardImage: async (cardId) => {
    const response = await api.get(`/cards/${cardId}/download`, {
      responseType: 'blob',
    });
    return response.data;
  },
};

