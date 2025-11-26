// src/data/mockData.js

export const MOCK_CARDS = [
    {
        id: 1,
        category: 'MUSIC',
        title: 'NewJeans - Hype Boy',
        desc: '요즘 하루종일 이 노래만 들음. 청량함 그 자체! 안무도 너무 힙해서 계속 보게 됨.',
        level: 5,
        author: 'Minji',
        boomUp: 120, // likes -> boomUp
        keywords: ['K-POP', '청량', '여름'],
        imageUrl: 'https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=2070&auto=format&fit=crop' // 음악 관련 이미지
    },
    {
        id: 2,
        category: 'BOOK',
        title: '불편한 편의점',
        desc: '마음이 따뜻해지는 소설. 퇴근길에 읽기 딱 좋음. 등장인물들이 살아있는 것 같음.',
        level: 4,
        author: 'BookWorm',
        boomUp: 85,
        keywords: ['힐링', '소설', '베스트셀러'],
        imageUrl: 'https://images.unsplash.com/photo-1544947950-fa07a98d237f?q=80&w=800&auto=format&fit=crop' // 책 관련 이미지
    },
    {
        id: 3,
        category: 'MOVIE',
        title: 'Interstellar',
        desc: '다시 봐도 웅장함. 우주 덕후라면 필청. 한스 짐머 음악이 진짜 압권임.',
        level: 5,
        author: 'SpaceX',
        boomUp: 230,
        keywords: ['SF', '우주', '명작'],
        imageUrl: 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?q=80&w=2072&auto=format&fit=crop' // 우주 관련 이미지
    },
];