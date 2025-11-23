import type { Metadata } from "next";
import "./globals.css";
import { Navigation, MainContent } from "@/components/ui/Navigation";
import { ThemeProvider } from "@/components/ui/ThemeProvider";

export const metadata: Metadata = {
  title: "Nominal - Rocket Launch Tracker",
  description: "Track upcoming rocket launches, space events, and space news",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="dark">
      <body className="antialiased">
        <ThemeProvider>
          <Navigation />
          <MainContent>{children}</MainContent>
        </ThemeProvider>
      </body>
    </html>
  );
}
