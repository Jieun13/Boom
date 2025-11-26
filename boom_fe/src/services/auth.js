import api from './api';
import { storage } from '../utils/storage';

export const authService = {
  // 회원가입
  signup: async (email, password, nickname) => {
    try {
      const response = await api.post('/auth/signup', {
        email,
        password,
        nickname: nickname || undefined,
      });
      return response.data;
    } catch (error) {
      // 에러를 그대로 throw하여 호출자가 처리할 수 있도록 함
      throw error;
    }
  },

  // 로그인
  login: async (email, password) => {
    try {
      const response = await api.post('/auth/login', {
        email,
        password,
      });
      
      if (response.data && response.data.success) {
        const { accessToken, refreshToken, user } = response.data.data;
        if (accessToken && refreshToken && user) {
          storage.setAccessToken(accessToken);
          storage.setRefreshToken(refreshToken);
          storage.setUser(user);
        }
      }
      
      return response.data;
    } catch (error) {
      // 에러를 그대로 throw하여 호출자가 처리할 수 있도록 함
      throw error;
    }
  },

  // 로그아웃
  logout: () => {
    storage.clear();
  },

  // 토큰 갱신
  refreshToken: async () => {
    const refreshToken = storage.getRefreshToken();
    if (!refreshToken) {
      throw new Error('No refresh token');
    }

    const axios = require('axios').default;
    const response = await axios.post(`${api.defaults.baseURL}/auth/refresh`, {}, {
      headers: {
        'Refresh-Token': refreshToken,
      },
    });

    if (response.data.success) {
      const { accessToken, refreshToken: newRefreshToken, user } = response.data.data;
      storage.setAccessToken(accessToken);
      storage.setRefreshToken(newRefreshToken);
      storage.setUser(user);
    }

    return response.data;
  },

  // 현재 사용자 정보 가져오기
  getCurrentUser: () => {
    return storage.getUser();
  },

  // 로그인 여부 확인
  isAuthenticated: () => {
    return !!storage.getAccessToken();
  },
};

