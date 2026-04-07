export default function RestaurantCard({ restaurant, onToggle, onViewMenu }) {
  const { name, cuisine, rating, open } = restaurant;
  const stars = '⭐'.repeat(Math.min(Math.round(rating || 0), 5));

  return (
    <div className="card flex flex-col gap-3">
      <div className="flex items-start justify-between">
        <div>
          <h3 className="text-lg font-bold text-gray-900 dark:text-white">{name}</h3>
          <p className="text-sm text-gray-500 dark:text-gray-400">{cuisine}</p>
        </div>
        <span
          className={`px-2.5 py-0.5 rounded-full text-xs font-semibold ${
            open
              ? 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300'
              : 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-300'
          }`}
        >
          {open ? 'Open' : 'Closed'}
        </span>
      </div>

      <div className="text-sm">{stars || 'No rating'}</div>

      <div className="flex gap-2 mt-auto">
        {onViewMenu && (
          <button onClick={() => onViewMenu(restaurant)} className="btn-primary text-sm flex-1">
            View Menu
          </button>
        )}
        {onToggle && (
          <button onClick={() => onToggle(restaurant.id)} className="btn-secondary text-sm">
            {open ? 'Close' : 'Open'}
          </button>
        )}
      </div>
    </div>
  );
}
