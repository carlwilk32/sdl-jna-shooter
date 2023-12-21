> Long time ago in a galaxy far, far away...

This is an example of what happens when Enterprise Java meets Game development. 

SDL2 | JNA | DI | Java 2D Shooter

## Prerequisites
You will need *any* JRE and SDL2 libs at minimum.

### Installing SDL2
This project uses [Simple DirectMedia Layer](https://www.libsdl.org/), which is written in C and doesn't have direct Java mappings.
We will use [JNA](https://github.com/java-native-access/jna) calls to access it from Java. In order to do so the following SDL2 libs should be available in your system:
- SDL2
- SDL2_image
- SDL2_ttf
- SDL2_mixer

There are different ways obtaining and installing above which will. 
One can download it directly from [libsdl-org GitHub releases](https://github.com/libsdl-org/SDL/releases) for their specific system or use your system's package manager.

<details>
  <summary>MacOS</summary>

using `homebrew`
    
```shell
    brew install sdl2 sdl2_image sdl2_ttf sdl2_mixer
 ```
</details>

You can also build them manually from sources, but this process will not be covered here. To do so please refer to SDL documentation [here](https://wiki.libsdl.org/SDL2/Installation).

## Build
```shell
./gardlew clean build
```

## Run
```shell
./gardlew run
```

## Known issues
### JNA can't find SDL
JNA tries to load libraries from default system locations, but sometimes it might fail.

If game didn't launch for you with similar exceptions: 

```shell
UnsatisfiedLinkError: Unable to load library 'SDL2'
... 
java.io.IOException: Native library (darwin-aarch64/libSDL2.dylib) not found in resource pat
```

double check that you have installed SDL libs and they're available in your system.

For example, in my system
```shell
pkg-config --libs sdl2
```
results in `-L/usr/local/lib -lSDL2`

Then provide above path explicitly to JNA by specifying it in JvmArgs using `-Djna.library.path=/usr/local/lib/`.
It should work now. 