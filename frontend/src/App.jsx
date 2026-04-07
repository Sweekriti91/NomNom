import { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import HomePage from './pages/HomePage';
import RestaurantsPage from './pages/RestaurantsPage';
import OrdersPage from './pages/OrdersPage';
import NotificationsPage from './pages/NotificationsPage';

export default function App() {
  const [page, setPage] = useState('home');

  // Apply saved theme on mount
  useEffect(() => {
    const saved = localStorage.getItem('theme');
    if (saved === 'dark' || (!saved && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, []);

  const renderPage = () => {
    switch (page) {
      case 'restaurants': return <RestaurantsPage />;
      case 'orders': return <OrdersPage />;
      case 'notifications': return <NotificationsPage />;
      default: return <HomePage onNavigate={setPage} />;
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-gray-50 dark:bg-slate-900 text-gray-900 dark:text-gray-100">
      <Navbar currentPage={page} onNavigate={setPage} />

      <main className="flex-1 max-w-7xl mx-auto w-full px-4 sm:px-6 lg:px-8 py-8">
        {renderPage()}
      </main>

      <footer className="bg-white dark:bg-slate-800 border-t border-gray-100 dark:border-slate-700 py-6">
        <div className="max-w-7xl mx-auto px-4 text-center text-sm text-gray-500 dark:text-gray-400">
          <p>🍔 <span className="font-semibold text-brand-500">NomNom</span> — Food Delivery Demo App</p>
          <p className="mt-1">Built with React + Vite + Tailwind CSS</p>
        </div>
      </footer>
    </div>
  );
}
