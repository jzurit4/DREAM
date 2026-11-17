import { createContext, useContext, useCallback } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { apiAuth } from '../utils/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [session, setSession] = useLocalStorage('pz_session', null); // { token, user }

  const user = session?.user ?? null;
  const admin = session?.user?.role === 'ROLE_ADMIN';

  const login = useCallback(async (identifier, password) => {
    const data = await apiAuth('/api/auth/login', {
      method: 'POST',
      body: { identifier, password },
    });
    setSession(data);   // token, user: { id, nombre, email, rol}
    return data.user;
  }, [setSession]);

  const loginUser  = useCallback(({ email, password }) => login(email, password), [login]);
  const loginAdmin = useCallback(async (usuario, contrasenia) => {
    const u = await login(usuario, contrasenia);
    return u.role === 'ROLE_ADMIN';
  }, [login]);

  const logout = useCallback(() => setSession(null), [setSession]);

  return (
    <AuthContext.Provider
      value={{
        user, admin,
        token: session?.token ?? null,
        loginUser, loginAdmin,
        logoutUser: logout, logoutAdmin: logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth debe usarse dentro de AuthProvider');
  return ctx;
};