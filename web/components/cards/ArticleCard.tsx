'use client';

import { Article } from '@/lib/types';
import { formatDate } from '@/lib/utils';
import Image from 'next/image';

interface ArticleCardProps {
  article: Article;
  variant?: 'default' | 'compact';
}

export function ArticleCard({ article, variant = 'default' }: ArticleCardProps) {
  if (variant === 'compact') {
    return <CompactArticleCard article={article} />;
  }

  return <DefaultArticleCard article={article} />;
}

function DefaultArticleCard({ article }: { article: Article }) {
  return (
    <a
      href={article.url}
      target="_blank"
      rel="noopener noreferrer"
      className="block"
    >
      <div className="bg-highlight rounded-lg overflow-hidden hover:bg-opacity-80 transition-all cursor-pointer">
        {article.image_url && (
          <div className="relative w-full h-48">
            <Image
              src={article.image_url}
              alt={article.title}
              fill
              className="object-cover"
              sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
            />
          </div>
        )}

        <div className="p-4">
          <div className="flex items-center gap-2 mb-2">
            {article.news_site && (
              <span className="text-xs bg-muted/20 text-muted px-2 py-1 rounded">
                {article.news_site}
              </span>
            )}
            <span className="text-xs text-muted">
              {formatDate(article.published_at, false)}
            </span>
          </div>

          <h3 className="font-semibold text-foreground mb-2 line-clamp-2">
            {article.title}
          </h3>

          {article.summary && (
            <p className="text-sm text-muted line-clamp-3">{article.summary}</p>
          )}

          <div className="flex items-center gap-2 mt-3 text-accent text-sm">
            <span>Read more</span>
            <span>→</span>
          </div>
        </div>
      </div>
    </a>
  );
}

function CompactArticleCard({ article }: { article: Article }) {
  return (
    <a
      href={article.url}
      target="_blank"
      rel="noopener noreferrer"
      className="block"
    >
      <div className="bg-highlight rounded-lg p-3 hover:bg-opacity-80 transition-all cursor-pointer">
        <div className="flex gap-3">
          {article.image_url && (
            <div className="relative w-20 h-20 flex-shrink-0 rounded overflow-hidden">
              <Image
                src={article.image_url}
                alt={article.title}
                fill
                className="object-cover"
                sizes="80px"
              />
            </div>
          )}

          <div className="flex-1 min-w-0">
            {article.news_site && (
              <p className="text-xs text-muted mb-1">{article.news_site}</p>
            )}

            <h4 className="font-medium text-sm text-foreground line-clamp-2 mb-1">
              {article.title}
            </h4>

            <p className="text-xs text-muted">
              {formatDate(article.published_at, false)}
            </p>
          </div>
        </div>
      </div>
    </a>
  );
}
