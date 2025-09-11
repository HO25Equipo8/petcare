import axios from 'axios';
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function login(email, pass) {
  try {
    const response = await axios.post(`${API_BASE_URL}/login`, { email, pass });
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 401) {
      throw new Error('Credenciales incorrectas');
    }
    throw error;
  }
}
