package at.dieneulingers.imagecapturer;

import java.awt.image.BufferedImage;

public interface ImageCapturer {

	void allocateNewSignal(String source);

	void allocateNewSignal(String source, int x);

	void allocateNewSignal(String source, int x, long waitForMilliSeconds);

	void setDimensionOfInputSignalTo1280x720();

	void logResultsOfAllocation(String source);

	void release_signal();

	BufferedImage nextFrame();

}