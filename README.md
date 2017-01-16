# PaletteGetter
A library that can get the color scheme of any app.

For testing and experimentation purposes, a sample apk can be downloaded [here](https://github.com/TheAndroidMaster/PaletteGetter/releases).

## Usage

### Setup

The Gradle dependency is available through jCenter, which is used by default in Android Studio. To add the module to your project, copy this line into the dependencies section of your build.gradle file.
``` gradle
compile 'james.palettegetter:palettegetter:0.0.1'
```

### Getting a Single Color

#### From Package Name

To get a color from the package name of an app, use the method below.
``` java
Integer color = PaletteGetter.get(context, packageName);
```

If the package name isn't valid, the method will return null.

#### From Component Name

Getting a color from a component name is sometimes more accurate than a package name because it specifies which part of an app you want the color from. To get the color from a component name, use the method below.
``` java
Integer color = PaletteGetter.get(context, componentName);
```

### Getting Multiple Colors

To get all the colors in an application, use the method below. This will return a `List` of all the known colors in that application, and may contain a few duplicates. If the package name is invalid, it will return an empty `List`.
``` java
List<Integer> colors = PaletteGetter.getPalette(context, packageName);
```
