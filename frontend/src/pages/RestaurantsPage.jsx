import { useState, useEffect } from 'react';
import { api } from '../api';
import RestaurantCard from '../components/RestaurantCard';

export default function RestaurantsPage() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [menuData, setMenuData] = useState(null);
  const [menuFor, setMenuFor] = useState(null);

  const load = async () => {
    try {
      const data = await api.getRestaurants();
      setRestaurants(Array.isArray(data) ? data : []);
    } catch {
      setRestaurants([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleToggle = async (id) => {
    await api.toggleRestaurant(id);
    load();
  };

  const handleViewMenu = async (restaurant) => {
    if (menuFor === restaurant.id) {
      setMenuFor(null);
      setMenuData(null);
      return;
    }
    try {
      const data = await api.getMenu(restaurant.id);
      setMenuData(Array.isArray(data) ? data : []);
      setMenuFor(restaurant.id);
    } catch {
      setMenuData([]);
      setMenuFor(restaurant.id);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white">🍽️ Restaurants</h1>
      </div>

      {loading ? (
        <p className="text-gray-500 dark:text-gray-400">Loading restaurants…</p>
      ) : restaurants.length === 0 ? (
        <p className="text-gray-500 dark:text-gray-400">No restaurants found. Make sure the backend is running.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {restaurants.map((r) => (
            <div key={r.id}>
              <RestaurantCard
                restaurant={r}
                onToggle={handleToggle}
                onViewMenu={handleViewMenu}
              />
              {menuFor === r.id && (
                <div className="mt-2 card">
                  <h4 className="font-semibold text-gray-900 dark:text-white mb-2">Menu</h4>
                  {menuData && menuData.length > 0 ? (
                    <ul className="space-y-2">
                      {menuData.map((item, i) => (
                        <li key={i} className="flex justify-between text-sm">
                          <span className="text-gray-700 dark:text-gray-300">{item.name}</span>
                          <span className="font-medium text-brand-600 dark:text-brand-400">
                            ${item.price?.toFixed(2)}
                          </span>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-sm text-gray-500 dark:text-gray-400">No menu items found.</p>
                  )}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
