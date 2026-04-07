import { useState, useEffect } from 'react';
import { api } from '../api';
import OrderCard from '../components/OrderCard';

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({
    customerName: '',
    restaurantId: '',
    items: '',
    totalAmount: '',
  });
  const [submitting, setSubmitting] = useState(false);

  const load = async () => {
    try {
      const [orderData, restData] = await Promise.all([
        api.getOrders().catch(() => []),
        api.getRestaurants().catch(() => []),
      ]);
      setOrders(Array.isArray(orderData) ? orderData : []);
      setRestaurants(Array.isArray(restData) ? restData : []);
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const items = form.items
        .split(',')
        .map((s) => s.trim())
        .filter(Boolean)
        .map((name) => ({ name, quantity: 1 }));
      await api.createOrder({
        customerName: form.customerName,
        restaurantId: Number(form.restaurantId),
        items,
        totalAmount: parseFloat(form.totalAmount),
      });
      setForm({ customerName: '', restaurantId: '', items: '', totalAmount: '' });
      load();
    } catch {
      // ignore
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdateStatus = async (id, status) => {
    await api.updateOrderStatus(id, status);
    load();
  };

  const handleCancel = async (id) => {
    await api.cancelOrder(id);
    load();
  };

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900 dark:text-white">📦 Orders</h1>

      {/* Create Order Form */}
      <div className="card">
        <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Create New Order</h2>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="label">Customer Name</label>
            <input
              className="input"
              value={form.customerName}
              onChange={(e) => setForm({ ...form, customerName: e.target.value })}
              required
              placeholder="John Doe"
            />
          </div>
          <div>
            <label className="label">Restaurant</label>
            <select
              className="input"
              value={form.restaurantId}
              onChange={(e) => setForm({ ...form, restaurantId: e.target.value })}
              required
            >
              <option value="">Select restaurant…</option>
              {restaurants.map((r) => (
                <option key={r.id} value={r.id}>{r.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="label">Items (comma separated)</label>
            <input
              className="input"
              value={form.items}
              onChange={(e) => setForm({ ...form, items: e.target.value })}
              required
              placeholder="Burger, Fries, Coke"
            />
          </div>
          <div>
            <label className="label">Total Amount ($)</label>
            <input
              className="input"
              type="number"
              step="0.01"
              min="0"
              value={form.totalAmount}
              onChange={(e) => setForm({ ...form, totalAmount: e.target.value })}
              required
              placeholder="25.99"
            />
          </div>
          <div className="md:col-span-2">
            <button type="submit" className="btn-primary" disabled={submitting}>
              {submitting ? 'Placing Order…' : 'Place Order'}
            </button>
          </div>
        </form>
      </div>

      {/* Orders List */}
      {loading ? (
        <p className="text-gray-500 dark:text-gray-400">Loading orders…</p>
      ) : orders.length === 0 ? (
        <p className="text-gray-500 dark:text-gray-400">No orders yet. Create one above!</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {[...orders].reverse().map((order) => (
            <OrderCard
              key={order.id}
              order={order}
              onUpdateStatus={handleUpdateStatus}
              onCancel={handleCancel}
            />
          ))}
        </div>
      )}
    </div>
  );
}
