import React from 'react';
import { Plus, User, LogOut } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const Navbar = ({ setActivePage }) => {
    const { logout } = useAuth();

    const handleLogout = () => {
        if (window.confirm('로그아웃 하시겠습니까?')) {
            logout();
            window.location.reload();
        }
    };

    return (
        <nav className="fixed bottom-0 w-full bg-[#0D0C1D]/80 backdrop-blur-lg border-t border-white/10 z-50 md:top-0 md:bottom-auto md:border-b md:border-t-0 px-6 py-4">
            <div className="max-w-4xl mx-auto flex justify-between items-center">
                <div className="hidden md:block text-2xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-[#0CFED8] to-[#E4007C] cursor-pointer" onClick={() => setActivePage('home')}>
                    Boom!
                </div>

            <div className="flex justify-between w-full md:w-auto gap-8 text-[#A6A6A6]">
                <button onClick={() => setActivePage('create')} className="relative top-[-20px] md:top-0">
                    <div className="bg-gradient-to-r from-[#E4007C] to-[#9D4EDC] p-4 rounded-full shadow-[0_0_20px_rgba(228,0,124,0.5)] hover:shadow-[0_0_30px_rgba(228,0,124,0.8)] transition-all transform hover:-translate-y-1">
                        <Plus size={28} color="white" />
                    </div>
                </button>
                <button onClick={() => setActivePage('profile')} className="flex flex-col items-center hover:text-[#F4F4F3] transition-colors">
                    <User size={24} />
                    <span className="text-[10px] mt-1">My Boom</span>
                </button>
                <button onClick={handleLogout} className="hidden md:flex items-center hover:text-[#F4F4F3] transition-colors">
                    <LogOut size={20} />
                </button>
            </div>
            </div>
        </nav>
    );
};

export default Navbar;