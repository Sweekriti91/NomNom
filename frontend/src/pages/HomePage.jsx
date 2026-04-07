import { useState, useEffect } from 'react';
import { api } from '../api';
import OrderCard from '../components/OrderCard';

export default function HomePage({ onNavigate }) {
  const [stats, setStats] = useState({ orders: 0, restaurants: 0, active: 0 });
  const [recentOrders, setRecentOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const [orders, restaurants] = await Promise.all([
          api.getOrders().catch(() => []),
          api.getRestaurants().catch(() => []),
        ]);
        const orderList = Array.isArray(orders) ? orders : [];
        const restaurantList = Array.isArray(restaurants) ? restaurants : [];
        const activeCount = orderList.filter(
          (o) => !['DELIVERED', 'CANCELLED'].includes(o.status)
        ).length;
        setStats({
          orders: orderList.length,
          restaurants: restaurantList.length,
          active: activeCount,
        });
        setRecentOrders(orderList.slice(-5).reverse());
      } catch {
        // API not available
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const statCards = [
    { label: 'Total Orders', value: stats.orders, icon: '📦', color: 'from-blue-500 to-blue-600' },
    { label: 'Restaurants', value: stats.restaurants, icon: '🍽️', color: 'from-green-500 to-green-600' },
    { label: 'Active Deliveries', value: stats.active, icon: '🚴', color: 'from-orange-500 to-orange-600' },
  ];

  return (
    <div className="space-y-8">
      {/* Hero */}
      <div className="text-center py-12">
        <h1 className="text-5xl font-extrabold text-gray-900 dark:text-white mb-4">
          Welcome to <span className="text-brand-500">NomNom</span>
        </h1>
        <p className="text-xl text-gray-500 dark:text-gray-400">
          Delicious food, delivered fast 🍔
        </p>
        <div className="mt-6 flex justify-center gap-4">
          <button onClick={() => onNavigate('restaurants')} className="btn-primary text-lg px-6 py-3">
            Browse Restaurants
          </button>
          <button onClick={() => onNavigate('orders')} className="btn-secondary text-lg px-6 py-3">
            View Orders
          </button>
        </div>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {statCards.map(({ label, value, icon, color }) => (
          <div
            key={label}
            className={`bg-gradient-to-br ${color} rounded-xl p-6 text-white shadow-lg`}
          >
            <div className="text-3xl mb-2">{icon}</div>
            <div className="text-3xl font-bold">{loading ? '…' : value}</div>
            <div className="text-sm opacity-90">{label}</div>
          </div>
        ))}
      </div>

      {/* Recent Orders */}
      <div>
        <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">Recent Orders</h2>
        {loading ? (
          <p className="text-gray-500 dark:text-gray-400">Loading…</p>
        ) : recentOrders.length === 0 ? (
          <p className="text-gray-500 dark:text-gray-400">No orders yet. Place your first order!</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {recentOrders.map((order) => (
              <OrderCard key={order.id} order={order} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
