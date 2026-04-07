const statusColors = {
  PLACED: 'bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300',
  CONFIRMED: 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900/30 dark:text-indigo-300',
  PREPARING: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/30 dark:text-yellow-300',
  OUT_FOR_DELIVERY: 'bg-orange-100 text-orange-800 dark:bg-orange-900/30 dark:text-orange-300',
  DELIVERED: 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300',
  CANCELLED: 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-300',
};

export default function StatusBadge({ status }) {
  const colorClass = statusColors[status] || 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300';
  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${colorClass}`}>
      {status?.replace(/_/g, ' ')}
    </span>
  );
}
