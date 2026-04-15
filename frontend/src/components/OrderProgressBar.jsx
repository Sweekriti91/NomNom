const STEPS = ['PLACED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'];

const STEP_LABELS = {
  PLACED: 'Placed',
  CONFIRMED: 'Confirmed',
  PREPARING: 'Preparing',
  OUT_FOR_DELIVERY: 'Out for Delivery',
  DELIVERED: 'Delivered',
};

export default function OrderProgressBar({ status }) {
  if (status === 'CANCELLED') {
    return (
      <div className="flex items-center gap-2 py-2">
        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400">
          Order Cancelled
        </span>
      </div>
    );
  }

  const currentIndex = STEPS.indexOf(status);

  return (
    <div className="w-full py-2" aria-label="Order progress">
      <div className="flex items-center">
        {STEPS.map((step, index) => {
          const isCompleted = index < currentIndex;
          const isCurrent = index === currentIndex;

          return (
            <div key={step} className="flex items-center flex-1 last:flex-none">
              {/* Step circle */}
              <div className="flex flex-col items-center">
                <div
                  className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold transition-colors
                    ${isCompleted
                      ? 'bg-green-500 text-white dark:bg-green-600'
                      : isCurrent
                        ? 'bg-brand-600 text-white dark:bg-brand-500 ring-2 ring-brand-300 dark:ring-brand-700'
                        : 'bg-gray-200 text-gray-400 dark:bg-gray-700 dark:text-gray-500'
                    }`}
                  aria-current={isCurrent ? 'step' : undefined}
                >
                  {isCompleted ? '✓' : index + 1}
                </div>
                <span
                  className={`mt-1 text-[10px] leading-tight text-center max-w-[52px] hidden sm:block
                    ${isCompleted || isCurrent
                      ? 'text-gray-700 dark:text-gray-300 font-medium'
                      : 'text-gray-400 dark:text-gray-500'
                    }`}
                >
                  {STEP_LABELS[step]}
                </span>
              </div>

              {/* Connector line (not after last step) */}
              {index < STEPS.length - 1 && (
                <div
                  className={`flex-1 h-1 mx-1 rounded transition-colors
                    ${index < currentIndex
                      ? 'bg-green-500 dark:bg-green-600'
                      : 'bg-gray-200 dark:bg-gray-700'
                    }`}
                />
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
