package com.luwaiwong.nominal.wear.tile

import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.ColorBuilders
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.luwaiwong.nominal.wear.data.LaunchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.guava.future
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Tile service for displaying launch information on WearOS watch face
 */
class LaunchTileService : TileService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val repository = LaunchRepository()

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        return serviceScope.future {
            // Fetch next launch
            val result = repository.getNextLaunch()
            val launch = result.getOrNull()

            val timeline = TimelineBuilders.Timeline.Builder()
                .addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder()
                        .setLayout(
                            LayoutElementBuilders.Layout.Builder()
                                .setRoot(
                                    if (launch != null) {
                                        createLaunchTileLayout(
                                            launchName = launch.name,
                                            countdown = launch.getCountdown(),
                                            provider = launch.provider.name
                                        )
                                    } else {
                                        createEmptyTileLayout()
                                    }
                                )
                                .build()
                        )
                        .build()
                )
                .build()

            TileBuilders.Tile.Builder()
                .setResourcesVersion("1")
                .setTimeline(timeline)
                .build()
        }
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion("1")
                .build()
        )
    }

    private fun createLaunchTileLayout(
        launchName: String,
        countdown: String,
        provider: String
    ): LayoutElementBuilders.LayoutElement {
        return LayoutElementBuilders.Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setAll(DimensionBuilders.dp(8f))
                            .build()
                    )
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(ColorBuilders.argb(0xFF1E1E1E.toInt()))
                            .build()
                    )
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Column.Builder()
                    .setWidth(DimensionBuilders.expand())
                    .setHeight(DimensionBuilders.wrap())
                    .addContent(
                        // Title
                        LayoutElementBuilders.Text.Builder()
                            .setText("Next Launch")
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(12f))
                                    .setColor(ColorBuilders.argb(0xFF4CAF50.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .addContent(
                        LayoutElementBuilders.Spacer.Builder()
                            .setHeight(DimensionBuilders.dp(4f))
                            .build()
                    )
                    .addContent(
                        // Countdown
                        LayoutElementBuilders.Text.Builder()
                            .setText(countdown)
                            .setMaxLines(1)
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(20f))
                                    .setColor(ColorBuilders.argb(0xFF4CAF50.toInt()))
                                    .setWeight(LayoutElementBuilders.FONT_WEIGHT_BOLD)
                                    .build()
                            )
                            .build()
                    )
                    .addContent(
                        LayoutElementBuilders.Spacer.Builder()
                            .setHeight(DimensionBuilders.dp(4f))
                            .build()
                    )
                    .addContent(
                        // Launch name
                        LayoutElementBuilders.Text.Builder()
                            .setText(launchName)
                            .setMaxLines(2)
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(14f))
                                    .setColor(ColorBuilders.argb(0xFFFFFFFF.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .addContent(
                        LayoutElementBuilders.Spacer.Builder()
                            .setHeight(DimensionBuilders.dp(4f))
                            .build()
                    )
                    .addContent(
                        // Provider
                        LayoutElementBuilders.Text.Builder()
                            .setText(provider)
                            .setMaxLines(1)
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(10f))
                                    .setColor(ColorBuilders.argb(0xFFAAAAAA.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun createEmptyTileLayout(): LayoutElementBuilders.LayoutElement {
        return LayoutElementBuilders.Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setAll(DimensionBuilders.dp(8f))
                            .build()
                    )
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(ColorBuilders.argb(0xFF1E1E1E.toInt()))
                            .build()
                    )
                    .build()
            )
            .addContent(
                LayoutElementBuilders.Column.Builder()
                    .setWidth(DimensionBuilders.expand())
                    .setHeight(DimensionBuilders.wrap())
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("Nominal")
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(16f))
                                    .setColor(ColorBuilders.argb(0xFF4CAF50.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .addContent(
                        LayoutElementBuilders.Spacer.Builder()
                            .setHeight(DimensionBuilders.dp(8f))
                            .build()
                    )
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("No upcoming launches")
                            .setFontStyle(
                                LayoutElementBuilders.FontStyle.Builder()
                                    .setSize(DimensionBuilders.sp(12f))
                                    .setColor(ColorBuilders.argb(0xFFAAAAAA.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }
}
