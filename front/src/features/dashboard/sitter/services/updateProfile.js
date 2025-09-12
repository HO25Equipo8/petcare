import axios from 'axios';
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

/**
 * Actualiza el perfil del usuario.
 * @param {Object} profileData - Datos del perfil a actualizar.
 * @param {string} profileData.name - Nombre del usuario.
 * @param {string} profileData.phone - Teléfono del usuario.
 * @param {Object} profileData.location - Dirección del usuario.
 * @param {string} profileData.location.street - Calle.
 * @param {string} profileData.location.number - Número.
 * @param {string} profileData.location.city - Ciudad.
 * @param {string} profileData.location.province - Provincia.
 * @param {string} profileData.location.country - País.
 * @param {string[]} profileData.professionalRoles - Roles profesionales.
 * @returns {Promise} Respuesta de la API.
 */
export const updateProfile = async (profileData) => {
  try {
    const token = localStorage.getItem('token');
    const response = await axios.put(
      `${API_BASE_URL}/user/update-profile`,
      profileData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      }
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};
