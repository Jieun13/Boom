import api from './api';

export const collectionService = {
  // 카드 수집
  collectCard: async (cardId) => {
    const response = await api.post(`/collections/cards/${cardId}`);
    return response.data;
  },

  // 카드 수집 취소
  uncollectCard: async (cardId) => {
    const response = await api.delete(`/collections/cards/${cardId}`);
    return response.data;
  },

  // 내 수집함 조회
  getMyCollections: async () => {
    const response = await api.get('/collections/me');
    return response.data;
  },
};

