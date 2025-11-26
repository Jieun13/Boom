import axios from 'axios';
import { storage } from '../utils/storage';

// API 기본 URL (환경 변수로 설정 가능)
// 개발 환경에서는 프록시를 사용하므로 상대 경로 사용
const API_URL = process.env.REACT_APP_API_URL || 
  (process.env.NODE_ENV === 'development' ? '/api' : 'http://localhost:8080/api');

// 개발 환경에서 API URL 로그 출력
if (process.env.NODE_ENV === 'development') {
  console.log('API URL:', API_URL);
  console.log('프록시를 사용합니다. 백엔드 서버가 http://localhost:8080에서 실행 중이어야 합니다.');
}

// axios 인스턴스 생성
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10초 타임아웃
});

// 요청 인터셉터: 토큰 자동 추가
api.interceptors.request.use(
  (config) => {
    const token = storage.getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 토큰 갱신 처리
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 401 에러이고, 아직 재시도하지 않은 경우
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = storage.getRefreshToken();
        if (!refreshToken) {
          storage.clear();
          window.location.href = '/login';
          return Promise.reject(error);
        }

        // 토큰 갱신 요청
        const response = await axios.post(`${API_URL}/auth/refresh`, {}, {
          headers: {
            'Refresh-Token': refreshToken,
          },
        });

        const { accessToken, refreshToken: newRefreshToken, user } = response.data.data;
        storage.setAccessToken(accessToken);
        storage.setRefreshToken(newRefreshToken);
        storage.setUser(user);

        // 원래 요청 재시도
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        storage.clear();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;

