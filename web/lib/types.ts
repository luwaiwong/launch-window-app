// API Response Types based on The Space Devs API

export interface Launch {
  id: string;
  sd_id?: string;
  type: "launch";
  name: string;
  image?: string;
  rocket: {
    id?: number;
    configuration: {
      id?: number;
      full_name: string;
      name?: string;
      family?: string;
      variant?: string;
    };
  };
  launch_service_provider: {
    id?: number;
    name: string;
    type?: string;
  };
  pad: {
    id?: number;
    name: string;
    location: {
      id?: number;
      name: string;
      country_code?: string;
    };
    wiki_url?: string;
    map_url?: string;
    map_image?: string;
    description?: string;
    latitude?: string;
    longitude?: string;
    total_launch_count?: number;
  };
  mission?: {
    id?: number;
    name: string;
    description: string;
    type?: string;
    agencies?: Agency[];
    orbit?: {
      id?: number;
      name: string;
      abbrev?: string;
    };
    vid_urls?: VideoUrl[];
  };
  net: string; // Network Expected Time (ISO date string)
  net_precision?: {
    id?: number;
    name: string; // "Month", "Day", "Hour", "Minute", "Second"
    abbrev?: string;
    description?: string;
  };
  window_start?: string;
  window_end?: string;
  status: {
    id: number;
    name: string; // "Go", "TBD", "Success", "Failure", "Partial Failure", etc.
    abbrev?: string;
    description?: string;
  };
  holdreason?: string;
  failreason?: string;
  webcast_live?: boolean;
  infographic?: string;
  program?: Program[];
}

export interface Agency {
  id: number;
  name: string;
  type?: string;
  country_code?: string;
  administrator?: string;
  founding_year?: string;
  logo_url?: string;
  image_url?: string;
  nation_url?: string;
  info_url?: string;
  launchers?: string;
  consecutive_successful_launches?: number;
  total_launch_count?: number;
  successful_landings?: number;
  description?: string;
}

export interface VideoUrl {
  priority: number;
  title?: string;
  description?: string;
  feature_image?: string;
  url: string;
}

export interface Program {
  id: number;
  name: string;
  description?: string;
  image_url?: string;
  start_date?: string;
  end_date?: string;
}

export interface Event {
  id: number;
  name: string;
  description: string;
  date: string;
  date_precision?: {
    id?: number;
    name: string;
    abbrev?: string;
    description?: string;
  };
  location?: string;
  feature_image?: string;
  type: {
    id?: number;
    name: string;
  };
  launches?: Launch[];
  webcast_live?: boolean;
  news_url?: string;
  video_url?: string;
}

export interface Article {
  id: number;
  title: string;
  url: string;
  image_url?: string;
  news_site?: string;
  summary?: string;
  published_at: string;
  updated_at?: string;
  featured?: boolean;
  launches?: Array<{
    launch_id: string;
    provider?: string;
  }>;
  events?: Array<{
    event_id: number;
  }>;
}

// API Response Wrapper Types
export interface ApiResponse<T> {
  count: number;
  next: string | null;
  previous: string | null;
  results: T[];
}

// User Settings Types
export interface NotificationSettings {
  enabled: boolean;
  twentyFourHours: boolean;
  twelveHours: boolean;
  oneHour: boolean;
  thirtyMinutes: boolean;
  tenMinutes: boolean;
  launches: boolean;
  events: boolean;
}

export interface ForYouSettings {
  showPastLaunches: boolean;
  showPastEvents: boolean;
}

export interface UserSettings {
  notifications: NotificationSettings;
  forYou: ForYouSettings;
  developerMode: boolean;
  favorites: string[]; // Array of launch IDs
}

// Combined feed item type for "For You" page
export type FeedItem = (Launch | Event) & { itemType: 'launch' | 'event' };

// Launch status categories
export type LaunchStatus =
  | 'Go'
  | 'TBD'
  | 'Success'
  | 'Failure'
  | 'Partial Failure'
  | 'In Flight'
  | 'Hold';
