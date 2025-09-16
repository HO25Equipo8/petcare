import axios from 'axios';
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const getProfile = async (userId) => {
  const token = localStorage.getItem('token');
  const response = await axios.get(
    `${API_BASE_URL}/public/${userId}/profile`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      withCredentials: true,
    }
  );
  return response.data;
};
