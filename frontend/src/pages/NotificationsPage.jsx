import { useState, useEffect } from 'react';
import { api } from '../api';
import NotificationCard from '../components/NotificationCard';

const TYPES = [
  'ORDER_PLACED',
  'ORDER_CONFIRMED',
  'ORDER_DELIVERED',
  'ORDER_CANCELLED',
  'PROMOTION',
];

export default function NotificationsPage() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ orderId: '', message: '', type: TYPES[0] });
  const [submitting, setSubmitting] = useState(false);

  const load = async () => {
    try {
      const data = await api.getNotifications();
      setNotifications(Array.isArray(data) ? data : []);
    } catch {
      setNotifications([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await api.createNotification({
        orderId: form.orderId ? Number(form.orderId) : null,
        message: form.message,
        type: form.type,
      });
      setForm({ orderId: '', message: '', type: TYPES[0] });
      load();
    } catch {
      // ignore
    } finally {
      setSubmitting(false);
    }
  };

  const handleMarkRead = async (id) => {
    await api.markNotificationRead(id);
    load();
  };

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900 dark:text-white">🔔 Notifications</h1>

      {/* Create Notification Form */}
      <div className="card">
        <h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Send Notification</h2>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="label">Order ID (optional)</label>
            <input
              className="input"
              type="number"
              value={form.orderId}
              onChange={(e) => setForm({ ...form, orderId: e.target.value })}
              placeholder="1"
            />
          </div>
          <div>
            <label className="label">Type</label>
            <select
              className="input"
              value={form.type}
              onChange={(e) => setForm({ ...form, type: e.target.value })}
            >
              {TYPES.map((t) => (
                <option key={t} value={t}>{t.replace(/_/g, ' ')}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="label">Message</label>
            <input
              className="input"
              value={form.message}
              onChange={(e) => setForm({ ...form, message: e.target.value })}
              required
              placeholder="Your order is on the way!"
            />
          </div>
          <div className="md:col-span-3">
            <button type="submit" className="btn-primary" disabled={submitting}>
              {submitting ? 'Sending…' : 'Send Notification'}
            </button>
          </div>
        </form>
      </div>

      {/* Notifications List */}
      {loading ? (
        <p className="text-gray-500 dark:text-gray-400">Loading notifications…</p>
      ) : notifications.length === 0 ? (
        <p className="text-gray-500 dark:text-gray-400">No notifications yet.</p>
      ) : (
        <div className="space-y-3">
          {[...notifications].reverse().map((n) => (
            <NotificationCard key={n.id} notification={n} onMarkRead={handleMarkRead} />
          ))}
        </div>
      )}
    </div>
  );
}
