export async function login(email, pass) {
  try {
    const response = await fetch('https://petcare-7yjq.onrender.com/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, pass }),
    });
    if (!response.ok) {
      throw new Error('Credenciales incorrectas');
    }
    const data = await response.json();
    return data;
  } catch (error) {
    throw error;
  }
}
