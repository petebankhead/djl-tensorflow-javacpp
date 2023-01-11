/**
 * Check loading OpenCV and TensorFlow via Deep Java Library and JavaCPP.
 */
package djlTF;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.djl.engine.Engine;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static final String TF_FIRST = "tf-first";
    public static final String OPENCV_FIRST = "opencv-first";
    public static final String OPENCV_FIRST_RESET = "opencv-first-reset";

    public static void main(String[] args) {

        String arg = args.length == 0 ? TF_FIRST : args[0];
        switch (arg) {
            case TF_FIRST:
                logger.info("Trying to load TensorFlow, then OpenCV");
                checkTensorFlow();
                checkOpenCV();
                logger.info("Done!");
                break;
            case OPENCV_FIRST:
                logger.info("Trying to load OpenCV, then TensorFlow");
                checkOpenCV();
                checkTensorFlow();
                logger.info("Done!");
                break;
            case OPENCV_FIRST_RESET:
                logger.info("Trying to load OpenCV, then TensorFlow after resetting Loader");
                checkOpenCV();
                resetLoaderPlatformProperties();
                checkTensorFlow();
                logger.info("Done!");
                break;
            default:
            logger.info(
                    "Unknown args " + Arrays.toString(args) + 
                    " - I expected  one of " + Arrays.asList(TF_FIRST, OPENCV_FIRST, OPENCV_FIRST_RESET));
        }
    }

    /**
     * Reset the Loader.platformProperties private field using reflection, 
     * as this is needed to load TensorFlow after OpenCV.
     */
    private static void resetLoaderPlatformProperties() {
        Field f;
        try {
            logger.info("Resetting Loader.platformProperties using reflection");
            f = Loader.class.getDeclaredField("platformProperties");
            f.setAccessible(true);
            f.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check we can load OpenCV with JavaCPP
     */
    private static void checkOpenCV() {
        var mat = Mat.eye(5, 5, opencv_core.CV_32FC1).asMat();
        logger.info("Successfully created OpenCV Mat: " + mat);
    }

    /**
     * Check we can load TensorFlow with DJL (and JavaCPP)
     */
    private static void checkTensorFlow() {
        var engine = Engine.getEngine("TensorFlow");
        logger.info("Successfully obtained TensorFlow engine: " + engine.getEngineName());
    }

}
