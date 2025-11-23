'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { cn } from '@/lib/utils';

const navItems = [
  { href: '/for-you', label: 'For You', icon: '🚀' },
  { href: '/', label: 'Dashboard', icon: '🏠' },
  { href: '/launches', label: 'Launches', icon: '🛸' },
  { href: '/news', label: 'News', icon: '📰' },
  { href: '/settings', label: 'Settings', icon: '⚙️' },
];

export function Navigation() {
  const pathname = usePathname();

  return (
    <>
      {/* Mobile Bottom Navigation */}
      <nav className="md:hidden fixed bottom-0 left-0 right-0 bg-highlight border-t border-muted/20 z-50">
        <div className="flex justify-around items-center h-16">
          {navItems.map((item) => {
            const isActive = pathname === item.href;
            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  'flex flex-col items-center justify-center flex-1 h-full transition-colors',
                  isActive
                    ? 'text-accent'
                    : 'text-muted hover:text-foreground'
                )}
              >
                <span className="text-2xl mb-1">{item.icon}</span>
                <span className="text-xs font-medium">{item.label}</span>
              </Link>
            );
          })}
        </div>
      </nav>

      {/* Desktop Side Navigation */}
      <nav className="hidden md:block fixed left-0 top-0 bottom-0 w-64 bg-highlight border-r border-muted/20 z-50">
        <div className="p-6">
          <h1 className="text-2xl font-bold text-accent mb-8">Nominal</h1>

          <ul className="space-y-2">
            {navItems.map((item) => {
              const isActive = pathname === item.href;
              return (
                <li key={item.href}>
                  <Link
                    href={item.href}
                    className={cn(
                      'flex items-center gap-3 px-4 py-3 rounded-lg transition-colors',
                      isActive
                        ? 'bg-accent/20 text-accent'
                        : 'text-muted hover:bg-muted/10 hover:text-foreground'
                    )}
                  >
                    <span className="text-2xl">{item.icon}</span>
                    <span className="font-medium">{item.label}</span>
                  </Link>
                </li>
              );
            })}
          </ul>
        </div>
      </nav>
    </>
  );
}

export function MainContent({ children }: { children: React.ReactNode }) {
  return (
    <div className="md:ml-64 pb-16 md:pb-0">
      <main className="min-h-screen">
        {children}
      </main>
    </div>
  );
}
