// src/App.jsx
import React, { useState } from 'react';
import { Menu } from 'lucide-react';
import { AuthProvider, useAuth } from './context/AuthContext';
import BackgroundGlow from './components/BackgroundGlow';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import MyBoom from './pages/MyBoom';
import UserProfile from './pages/UserProfile';
import CreateBoomModal from './pages/CreateBoomModal';
import Login from './pages/Login';

function AppContent() {
    const [activePage, setActivePage] = useState('home'); // home, profile, create, userProfile
    const [viewingUserId, setViewingUserId] = useState(null);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [showLogin, setShowLogin] = useState(false);
    const { user, loading } = useAuth();

    const handleNav = (page) => {
        if (page === 'create') {
            if (user) {
                setShowCreateModal(true);
            } else {
                setShowLogin(true);
            }
        } else {
            setActivePage(page);
        }
    }

    if (loading) {
        return (
            <div className="min-h-screen bg-[#0D0C1D] flex items-center justify-center">
                <div className="text-white">로딩 중...</div>
            </div>
        );
    }

    if (!user) {
        return (
            <div className="min-h-screen bg-[#0D0C1D] font-sans selection:bg-[#E4007C] selection:text-white relative">
                <BackgroundGlow />
                <Login onClose={() => setShowLogin(false)} />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-[#0D0C1D] font-sans selection:bg-[#E4007C] selection:text-white relative">
            <BackgroundGlow />

            {/* 모바일 헤더 */}
            <header className="md:hidden fixed top-0 w-full p-4 z-50 flex justify-between items-center bg-[#0D0C1D]/50 backdrop-blur-sm">
                <div className="text-xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-[#0CFED8] to-[#E4007C]">Boom!</div>
                <Menu className="text-white" />
            </header>

            {/* 메인 컨텐츠 영역 */}
            <main className="pt-20 pb-28 px-4 max-w-4xl mx-auto relative z-10 min-h-screen">
                {activePage === 'home' && (
                    <Home 
                        onUserClick={(userId) => { setViewingUserId(userId); setActivePage('userProfile'); }} 
                        onCreateClick={() => handleNav('create')}
                    />
                )}
                {activePage === 'profile' && <MyBoom />}
                {activePage === 'userProfile' && viewingUserId && (
                    <UserProfile 
                        userId={viewingUserId} 
                        onBack={() => { setViewingUserId(null); setActivePage('home'); }}
                        onUserClick={(userId) => { setViewingUserId(userId); }}
                    />
                )}
            </main>

            {/* 모달 및 네비게이션 */}
            {showCreateModal && <CreateBoomModal onClose={() => setShowCreateModal(false)} />}
            {showLogin && <Login onClose={() => setShowLogin(false)} />}
            <Navbar setActivePage={handleNav} />

        </div>
    );
}

export default function App() {
    return (
        <AuthProvider>
            <AppContent />
        </AuthProvider>
    );
}