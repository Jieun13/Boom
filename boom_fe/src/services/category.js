import api from './api';

export const categoryService = {
  // 전체 카테고리 조회
  getCategories: async () => {
    const response = await api.get('/categories');
    return response.data;
  },

  // 카테고리별 소분류 조회
  getSubCategories: async (categoryId) => {
    const response = await api.get(`/categories/${categoryId}/subcategories`);
    return response.data;
  },
};

