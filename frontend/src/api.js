const BASE_URL = '/api';

export const api = {
  // restaurants
  getRestaurants: () => fetch(`${BASE_URL}/restaurants`).then(r => r.json()),
  getRestaurant: (id) => fetch(`${BASE_URL}/restaurants/${id}`).then(r => r.json()),
  createRestaurant: (data) =>
    fetch(`${BASE_URL}/restaurants`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(r => r.json()),
  toggleRestaurant: (id) =>
    fetch(`${BASE_URL}/restaurants/${id}/toggle`, { method: 'PUT' }).then(r => r.json()),
  getMenu: (id) => fetch(`${BASE_URL}/restaurants/${id}/menu`).then(r => r.json()),

  // orders
  getOrders: () => fetch(`${BASE_URL}/orders`).then(r => r.json()),
  getOrder: (id) => fetch(`${BASE_URL}/orders/${id}`).then(r => r.json()),
  createOrder: (data) =>
    fetch(`${BASE_URL}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(r => r.json()),
  updateOrderStatus: (id, status) =>
    fetch(`${BASE_URL}/orders/${id}/status?status=${encodeURIComponent(status)}`, {
      method: 'PUT',
    }).then(r => r.json()),
  cancelOrder: (id) =>
    fetch(`${BASE_URL}/orders/${id}/cancel`, { method: 'PUT' }).then(r => r.json()),

  // notifications
  getNotifications: () => fetch(`${BASE_URL}/notifications`).then(r => r.json()),
  createNotification: (data) =>
    fetch(`${BASE_URL}/notifications`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    }).then(r => r.json()),
  markNotificationRead: (id) =>
    fetch(`${BASE_URL}/notifications/${id}/read`, { method: 'PUT' }).then(r => r.json()),
};
