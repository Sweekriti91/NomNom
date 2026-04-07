export default function NotificationCard({ notification, onMarkRead }) {
  const { id, message, type, read, createdAt } = notification;

  const typeBadge = {
    ORDER_PLACED: 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300',
    ORDER_CONFIRMED: 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900/30 dark:text-indigo-300',
    ORDER_DELIVERED: 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300',
    ORDER_CANCELLED: 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-300',
    PROMOTION: 'bg-purple-100 text-purple-800 dark:bg-purple-900/30 dark:text-purple-300',
  };

  const badgeClass = typeBadge[type] || 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300';

  return (
    <div className={`card flex items-start gap-4 ${!read ? 'border-l-4 border-l-brand-500' : 'opacity-75'}`}>
      <div className={`mt-1 w-2.5 h-2.5 rounded-full flex-shrink-0 ${read ? 'bg-gray-300 dark:bg-slate-600' : 'bg-brand-500'}`} />
      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 flex-wrap mb-1">
          <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${badgeClass}`}>
            {type?.replace(/_/g, ' ')}
          </span>
          {createdAt && (
            <span className="text-xs text-gray-400 dark:text-gray-500">
              {new Date(createdAt).toLocaleString()}
            </span>
          )}
        </div>
        <p className="text-sm text-gray-700 dark:text-gray-300">{message}</p>
      </div>
      {!read && onMarkRead && (
        <button
          onClick={() => onMarkRead(id)}
          className="text-xs btn-secondary flex-shrink-0"
        >
          Mark Read
        </button>
      )}
    </div>
  );
}
