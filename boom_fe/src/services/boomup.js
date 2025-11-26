import api from './api';

export const boomupService = {
  // BoomUP 추가
  addBoomUp: async (cardId) => {
    const response = await api.post(`/boomups/cards/${cardId}`);
    return response.data;
  },

  // BoomUP 취소
  removeBoomUp: async (cardId) => {
    const response = await api.delete(`/boomups/cards/${cardId}`);
    return response.data;
  },
};

