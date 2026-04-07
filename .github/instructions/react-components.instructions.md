---
description: "Use when creating or modifying React components, pages, or UI elements in the NomNom frontend."
applyTo: "frontend/**/*.jsx"
---

# React Component Standards

## Components

- Functional components only — no class components
- Destructure props in the function signature
- Use named exports alongside default export
- File naming: PascalCase for components (`OrderCard.jsx`), camelCase for utilities

## Styling

- Tailwind CSS utility classes only — no CSS modules, styled-components, or inline style objects
- Support dark mode with `dark:` variants on every color utility
- Use responsive prefixes: `sm:`, `md:`, `lg:` for breakpoints
- Prefer `flex` and `grid` for layout

## Patterns

```jsx
export default function OrderCard({ order, onCancel }) {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="rounded-lg border border-gray-200 bg-white p-4 shadow-sm dark:border-gray-700 dark:bg-gray-800">
      <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
        Order #{order.id}
      </h3>
      {/* ... */}
    </div>
  );
}
```

## Conventions

- State management with `useState` and `useReducer` — no Redux unless explicitly added
- Data fetching with `useEffect` + `fetch` or a custom `useFetch` hook
- API base URL from environment variable: `import.meta.env.VITE_API_URL`
- Loading and error states for every data fetch
- Accessible: use semantic HTML, proper ARIA attributes, and keyboard navigation
