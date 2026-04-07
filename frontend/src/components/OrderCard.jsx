import StatusBadge from './StatusBadge';

export default function OrderCard({ order, onUpdateStatus, onCancel }) {
  const { customerName, restaurantId, items, status, totalAmount } = order;

  const nextStatus = {
    PLACED: 'CONFIRMED',
    CONFIRMED: 'PREPARING',
    PREPARING: 'OUT_FOR_DELIVERY',
    OUT_FOR_DELIVERY: 'DELIVERED',
  };

  return (
    <div className="card flex flex-col gap-3">
      <div className="flex items-start justify-between">
        <div>
          <h3 className="text-lg font-bold text-gray-900 dark:text-white">{customerName}</h3>
          <p className="text-sm text-gray-500 dark:text-gray-400">Restaurant #{restaurantId}</p>
        </div>
        <StatusBadge status={status} />
      </div>

      {items && items.length > 0 && (
        <div className="text-sm text-gray-600 dark:text-gray-400">
          {items.map((item, i) => (
            <span key={i}>
              {item.name || item} × {item.quantity || 1}
              {i < items.length - 1 ? ', ' : ''}
            </span>
          ))}
        </div>
      )}

      <div className="text-lg font-bold text-brand-600 dark:text-brand-400">
        ${totalAmount?.toFixed(2) || '0.00'}
      </div>

      <div className="flex gap-2 mt-auto">
        {nextStatus[status] && onUpdateStatus && (
          <button
            onClick={() => onUpdateStatus(order.id, nextStatus[status])}
            className="btn-primary text-sm flex-1"
          >
            → {nextStatus[status].replace(/_/g, ' ')}
          </button>
        )}
        {status !== 'DELIVERED' && status !== 'CANCELLED' && onCancel && (
          <button
            onClick={() => onCancel(order.id)}
            className="text-sm px-3 py-2 rounded-lg bg-red-100 text-red-700 hover:bg-red-200 dark:bg-red-900/30 dark:text-red-400 dark:hover:bg-red-900/50 transition-colors"
          >
            Cancel
          </button>
        )}
      </div>
    </div>
  );
}
