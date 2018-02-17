# ImageListPreference

<a target="_blank" href="https://developer.android.com/about/versions/android-4.0.3.html"><img src="https://img.shields.io/badge/API-15%2B-blue.svg?style=flat" alt="API Version 15" /></a>
<a target="_blank" href="LICENSE"><img src="http://img.shields.io/:license-mit-blue.svg" alt="License" /></a>

A [`ListPreference`](https://developer.android.com/reference/android/preference/ListPreference.html) that shows an image resource for each item.

![](https://raw.githubusercontent.com/austingreco/ImageListPreference/master/images/screenshot1.png | width=290)
![](https://raw.githubusercontent.com/austingreco/ImageListPreference/master/images/screenshot2.png | width=290)

## Usage

Add to your build.gradle:

```
compile 'com.austingreco:image-list-preference:1.0.0'
```

## Using the Preference

Add the `ImageListPreference` to your XML:

```
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"
                  xmlns:app="http://schemas.android.com/apk/res-auto">
    <com.austingreco.imagelistpreference.ImageListPreference
        android:title="Simple Example"
        android:summary="ilp_entryImages"
        android:key="theme"
        android:entries="@array/testEntries"
        android:entryValues="@array/testEntryValues"
        app:ilp_entryImages="@array/testEntryImages"
    />
</PreferenceScreen>
```

Create an array of image resources to use as the `ilp_entryImages` attribute:

```
<array name="testEntryImages">
    <item>@drawable/ic_android_black_24dp</item>
    <item>@drawable/ic_camera_black_24dp</item>
    <item>@drawable/ic_fingerprint_black_24dp</item>
</array>
```

## Attributes

`ImageListPreference` is customizable through these attributes:

| name                | type      | description                                                               | default
|---------------------|-----------|---------------|---------------------------------------------------------------------------------------
| ilp_entryImages     | array     | An <array> of image resources to show in the list                         |
| ilp_tint            | color     | The color to tint the images                                              | #FF000000
| ilp_backgroundTint  | color     | The color to tint the background                                          |
| ilp_useCard         | boolean   | Wrap the image in a CardView which gives rounded corners and a shadow     | false
| ilp_itemLayout      | reference | A custom layout resource to use as each item in the list                  |

## License

MIT
