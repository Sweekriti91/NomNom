// Estimated total minutes from order creation until delivery at each status.
// Based on typical restaurant preparation + delivery times:
//   PLACED:           45 min — restaurant hasn't confirmed yet
//   CONFIRMED:        40 min — restaurant accepted, about to start preparing
//   PREPARING:        30 min — food is being cooked
//   OUT_FOR_DELIVERY: 15 min — driver is on the way
const ETA_MINUTES = {
  PLACED: 45,
  CONFIRMED: 40,
  PREPARING: 30,
  OUT_FOR_DELIVERY: 15,
};

export default function DeliveryETA({ status, createdAt }) {
  if (status === 'DELIVERED') {
    return (
      <div className="flex items-center gap-1.5 text-sm text-green-600 dark:text-green-400 font-medium">
        <span aria-hidden="true">🎉</span>
        <span>Delivered</span>
      </div>
    );
  }

  if (status === 'CANCELLED') {
    return (
      <div className="flex items-center gap-1.5 text-sm text-red-500 dark:text-red-400 font-medium">
        <span aria-hidden="true">✕</span>
        <span>Cancelled</span>
      </div>
    );
  }

  const estimatedMinutes = ETA_MINUTES[status];
  if (!estimatedMinutes || !createdAt) return null;

  const createdTime = new Date(createdAt);
  const etaTime = new Date(createdTime.getTime() + estimatedMinutes * 60 * 1000);
  const now = new Date();
  const diffMs = etaTime - now;
  const diffMin = Math.round(diffMs / 60000);

  let label;
  if (diffMin <= 0) {
    label = 'Arriving soon';
  } else if (diffMin === 1) {
    label = '~1 min';
  } else {
    label = `~${diffMin} min`;
  }

  return (
    <div className="flex items-center gap-1.5 text-sm text-orange-600 dark:text-orange-400 font-medium">
      <span aria-hidden="true">🕐</span>
      <span>ETA: {label}</span>
    </div>
  );
}
