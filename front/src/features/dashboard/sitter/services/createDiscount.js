import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

/**
 * Crea una nueva regla de descuento para el cuidador.
 * @param {Object} data - Datos de la regla de descuento.
 * @param {string} data.category - Categoría (FRECUENTE, NORMAL, INFRECUENTE, PREMIUM).
 * @param {number} data.minSessionsPerWeek - Sesiones mínimas por semana.
 * @param {number} data.maxSessionsPerWeek - Sesiones máximas por semana.
 * @param {number} data.discount - Descuento.
 * @returns {Promise} Respuesta de la API.
 */
export async function createDiscount({ category, minSessionsPerWeek, maxSessionsPerWeek, discount }) {
  const url = `${API_BASE_URL}/sitter/register/discount/rule`;
  const body = {
    category,
    minSessionsPerWeek,
    maxSessionsPerWeek,
    discount,
  };
  const token = localStorage.getItem('token');
  return axios.post(url, body, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
}
