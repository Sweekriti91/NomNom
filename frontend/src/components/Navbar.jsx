const navItems = [
  { key: 'home', label: '🏠 Home' },
  { key: 'restaurants', label: '🍽️ Restaurants' },
  { key: 'orders', label: '📦 Orders' },
  { key: 'notifications', label: '🔔 Notifications' },
];

export default function Navbar({ currentPage, onNavigate }) {
  return (
    <nav className="bg-white dark:bg-slate-800 shadow-md border-b border-gray-100 dark:border-slate-700 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <button
            onClick={() => onNavigate('home')}
            className="flex items-center gap-2 text-2xl font-bold text-brand-500 hover:text-brand-600 transition-colors"
          >
            🍔 NomNom
          </button>

          {/* Nav links */}
          <div className="hidden md:flex items-center gap-1">
            {navItems.map(({ key, label }) => (
              <button
                key={key}
                onClick={() => onNavigate(key)}
                className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors duration-200 ${currentPage === key
                    ? 'bg-brand-100 dark:bg-brand-900/30 text-brand-700 dark:text-brand-400'
                    : 'text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-slate-700'
                  }`}
              >
                {label}
              </button>
            ))}
          </div>
        </div>

        {/* Mobile nav */}
        <div className="md:hidden flex gap-1 pb-3 overflow-x-auto">
          {navItems.map(({ key, label }) => (
            <button
              key={key}
              onClick={() => onNavigate(key)}
              className={`px-3 py-1.5 rounded-lg text-xs font-medium whitespace-nowrap transition-colors duration-200 ${currentPage === key
                  ? 'bg-brand-100 dark:bg-brand-900/30 text-brand-700 dark:text-brand-400'
                  : 'text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-slate-700'
                }`}
            >
              {label}
            </button>
          ))}
        </div>
      </div>
    </nav>
  );
}
