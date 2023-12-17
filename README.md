SDL2 + JNA + GraalVM Native

## Build
```shell
./gardlew clean build
```

## Run
```shell
./gardlew runShadow
```

## Native build
Ensure that you have GraalVM JDK available first.
```shell
./gradlew -Pagent run # Runs on JVM with native-image-agent.
./gradlew metadataCopy --task run --dir src/main/resources/META-INF/native-image # Copies the metadata collected by the agent into the project sources
./gradlew nativeCompile # Builds image using metadata acquired by the agent.
```
You can additionally compress output binaries using UPX utility:
```shell
upx -7 -k ./build/native/nativeCompile/java-sdl-shooter
```

## Run native
```shell
./build/native/nativeCompile/java-sdl-shooter
```