import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { authService } from '../services/auth';
import { X } from 'lucide-react';

const Login = ({ onClose, onSwitchToSignup }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nickname, setNickname] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isLogin) {
        // 로그인
        const response = await authService.login(email, password);
        if (response && response.success) {
          login(response.data.user);
          onClose?.();
        } else {
          setError(response?.message || '로그인에 실패했습니다.');
        }
      } else {
        // 회원가입
        const response = await authService.signup(email, password, nickname);
        if (response && response.success) {
          // 회원가입 후 자동 로그인
          const loginResponse = await authService.login(email, password);
          if (loginResponse && loginResponse.success) {
            login(loginResponse.data.user);
            onClose?.();
          } else {
            setError(loginResponse?.message || '로그인에 실패했습니다.');
          }
        } else {
          setError(response?.message || '회원가입에 실패했습니다.');
        }
      }
    } catch (err) {
      console.error('인증 오류:', err);
      
      // 다양한 에러 케이스 처리
      let errorMessage = '오류가 발생했습니다.';
      
      if (err.response) {
        // 서버 응답이 있는 경우
        const data = err.response.data;
        if (data && data.message) {
          errorMessage = data.message;
        } else if (data && typeof data === 'string') {
          errorMessage = data;
        } else if (err.response.status === 400) {
          errorMessage = '잘못된 요청입니다. 입력 정보를 확인해주세요.';
        } else if (err.response.status === 401) {
          errorMessage = '이메일 또는 비밀번호가 올바르지 않습니다.';
        } else if (err.response.status === 409) {
          errorMessage = '이미 존재하는 이메일입니다.';
        } else if (err.response.status >= 500) {
          errorMessage = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
        }
      } else if (err.request) {
        // 요청은 보냈지만 응답을 받지 못한 경우
        errorMessage = '서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.';
      } else if (err.message) {
        // 기타 에러
        errorMessage = err.message;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center bg-black/80 backdrop-blur-sm p-4">
      <div className="bg-[#1a192e] w-full max-w-md rounded-3xl p-6 border border-white/10 shadow-2xl relative">
        {onClose && (
          <button onClick={onClose} className="absolute top-4 right-4 text-[#A6A6A6] hover:text-white z-10">
            <X />
          </button>
        )}

        <h2 className="text-2xl font-bold text-white mb-6 text-center">
          {isLogin ? '로그인' : '회원가입'}
        </h2>

        {error && (
          <div className="mb-4 p-3 bg-red-500/20 border border-red-500/50 rounded-lg text-red-400 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          {!isLogin && (
            <div>
              <label className="block text-[#A6A6A6] text-sm mb-1">닉네임</label>
              <input
                type="text"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                placeholder="닉네임을 입력하세요"
                className="w-full bg-white/5 border border-white/10 rounded-xl text-white p-3 focus:border-[#E4007C] outline-none"
                required={!isLogin}
              />
            </div>
          )}

          <div>
            <label className="block text-[#A6A6A6] text-sm mb-1">이메일</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="이메일을 입력하세요"
              className="w-full bg-white/5 border border-white/10 rounded-xl text-white p-3 focus:border-[#E4007C] outline-none"
              required
            />
          </div>

          <div>
            <label className="block text-[#A6A6A6] text-sm mb-1">비밀번호</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="비밀번호를 입력하세요"
              className="w-full bg-white/5 border border-white/10 rounded-xl text-white p-3 focus:border-[#E4007C] outline-none"
              required
              minLength={8}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3 rounded-xl text-white font-bold bg-gradient-to-r from-[#E4007C] to-[#9D4EDC] hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {loading ? '처리 중...' : (isLogin ? '로그인' : '회원가입')}
          </button>
        </form>

        <div className="mt-4 text-center">
          <button
            onClick={() => {
              setIsLogin(!isLogin);
              setError('');
            }}
            className="text-[#0CFED8] hover:text-[#0CFED8]/80 text-sm"
          >
            {isLogin ? '계정이 없으신가요? 회원가입' : '이미 계정이 있으신가요? 로그인'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;

