# Gold Capsule

A compact Android home-screen widget for Xiaomi/HyperOS that displays the international gold futures price, daily change, and a small intraday trend line.

## Current behavior

- 1 × 2 vertical capsule layout
- Gold futures symbol: `GC=F`
- USD per troy ounce
- Refreshes every 30 minutes through Android WorkManager
- Tap the widget to open the quote page
- No API key required for the initial data source

## Open in Android Studio

1. Clone or download this repository.
2. Open the repository root in Android Studio.
3. Allow Gradle to sync and install any requested Android SDK components.
4. Run the `app` configuration on the phone.
5. Long-press the Xiaomi home screen, choose Widgets, and add “Gold Capsule”.

On HyperOS, allow background activity for the app if automatic updates are delayed.

## Data-source note

The first version uses Yahoo Finance's public chart endpoint for `GC=F`. It is suitable for a working prototype but is not a contracted market-data service. The data layer is isolated in `GoldRepository`, so a licensed API can replace it later without redesigning the widget.
