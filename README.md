# Deep Java Library + TensorFlow + OpenCV

Minimal example to test loading OpenCV and TensorFlow using Deep Java Library (DJL) and JavaCPP.

For DJL v0.2.0 this works if TensorFlow is loaded first, but fails if OpenCV is loaded first... unless the `Loader` class is modified by reflection in between.

This app shows the different possibilities.

> Output generated using an old (Intel) MacBook Pro.

## Load TensorFlow then OpenCV (works)

Run as
```
./gradlew run --args="tf-first"
```

Output:
```
INFO -- Trying to load TensorFlow, then OpenCV
2023-01-11 17:56:09.610551: I external/org_tensorflow/tensorflow/core/platform/cpu_feature_guard.cc:151] This TensorFlow binary is optimized with oneAPI Deep Neural Network Library (oneDNN) to use the following CPU instructions in performance-critical operations:  AVX2 FMA
To enable them in other operations, rebuild TensorFlow with the appropriate compiler flags.
INFO -- Successfully obtained TensorFlow engine: TensorFlow
INFO -- Successfully created OpenCV Mat: org.bytedeco.opencv.opencv_core.Mat[width=5,height=5,depth=32,channels=1]
INFO -- Done!
```


## Load OpenCV then TensorFlow (fails)

Run as
```
./gradlew run --args="opencv-first"
```

Output:

```
INFO -- Trying to load OpenCV, then TensorFlow
INFO -- Successfully created OpenCV Mat: org.bytedeco.opencv.opencv_core.Mat[width=5,height=5,depth=32,channels=1]
Exception in thread "main" ai.djl.engine.EngineException: Failed to load TensorFlow native library
        at ai.djl.tensorflow.engine.TfEngine.newInstance(TfEngine.java:77)
        at ai.djl.tensorflow.engine.TfEngineProvider.getEngine(TfEngineProvider.java:40)
        at ai.djl.engine.Engine.getEngine(Engine.java:186)
        at djlTF.App.checkTensorFlow(App.java:83)
        at djlTF.App.main(App.java:38)
Caused by: java.lang.UnsatisfiedLinkError: no jnitensorflow in java.library.path: /Users/pete/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
        at java.base/java.lang.ClassLoader.loadLibrary(ClassLoader.java:2429)
        at java.base/java.lang.Runtime.loadLibrary0(Runtime.java:818)
        at java.base/java.lang.System.loadLibrary(System.java:1989)
        at org.bytedeco.javacpp.Loader.loadLibrary(Loader.java:1825)
        at org.bytedeco.javacpp.Loader.load(Loader.java:1416)
        at org.bytedeco.javacpp.Loader.load(Loader.java:1227)
        at org.bytedeco.javacpp.Loader.load(Loader.java:1203)
        at org.tensorflow.internal.c_api.global.tensorflow.<clinit>(tensorflow.java:12)
        at org.tensorflow.internal.c_api.AbstractTFE_ContextOptions.newContextOptions(AbstractTFE_ContextOptions.java:41)
        at ai.djl.tensorflow.engine.javacpp.JavacppUtils.createEagerSession(JavacppUtils.java:210)
        at ai.djl.tensorflow.engine.TfEngine.newInstance(TfEngine.java:58)
        ... 4 more
Caused by: java.lang.UnsatisfiedLinkError: Could not find jnitensorflow in class, module, and library paths.
        at org.bytedeco.javacpp.Loader.loadLibrary(Loader.java:1792)
        ... 11 more
```


## Load TensorFlow then OpenCV (works)

Run as
```
./gradlew run --args="opencv-first-reset"
```

Output:
```
INFO --Trying to load OpenCV, then TensorFlow after resetting Loader
INFO --Successfully created OpenCV Mat: org.bytedeco.opencv.opencv_core.Mat[width=5,height=5,depth=32,channels=1]
INFO --Resetting Loader.platformProperties using reflection
<=========----> 75% EXECUTING [6s]
2023-01-11 17:54:36.197223: I external/org_tensorflow/tensorflow/core/platform/cpu_feature_guard.cc:151] This TensorFlow binary is optimized with oneAPI Deep Neural Network Library (oneDNN) to use the following CPU instructions in performance-critical operations:  AVX2 FMA
To enable them in other operations, rebuild TensorFlow with the appropriate compiler flags.
INFO --Successfully obtained TensorFlow engine: TensorFlow
INFO --Done!
```