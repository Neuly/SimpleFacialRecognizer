package at.dieneulingers.imagecapturer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.github.sarxos.webcam.Webcam;

public class ImageCapturingFromWebcam implements ImageCapturer {
	/**
	 * Possible webcams are: HD WebCam /dev/video0, USB_Camera /dev/video1,
	 * USB_Camera /dev/video2
	 */
	private Webcam inputSignal;
	private final Logger LOGGER = Logger.getLogger(ImageCapturingFromWebcam.class);
	private static final int X = 5;
	private static final long WAIT_FOR_X_MILLI_SECONDS = TimeUnit.SECONDS.toMillis(2);

	public ImageCapturingFromWebcam(String signal) {
		allocateNewSignal(signal);
	}

	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#allocateNewSignal(java.lang.String)
	 */
	public void allocateNewSignal(String source) {
		allocateNewSignal(source, X, WAIT_FOR_X_MILLI_SECONDS);
	}
	
	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#allocateNewSignal(java.lang.String, int)
	 */
	public void allocateNewSignal(String source, int x) {
		allocateNewSignal(source, x, WAIT_FOR_X_MILLI_SECONDS);
	}
	
	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#allocateNewSignal(java.lang.String, int, long)
	 */
	public void allocateNewSignal(String source, int x, long waitForMilliSeconds) {
		allocateGivenInputSignal(source);
		for (int i = 0; i < x; i++) {
			if (inputSignal.isOpen()) {
				LOGGER.info("Input Signal is opened");
				return;
			} else {
				LOGGER.info("Input Signal still not opened, attempt #" + i);
				sleepForXMillis(waitForMilliSeconds);
			}
		}
		System.exit(-1);
	}
	
	private void allocateGivenInputSignal(String source) {
		LOGGER.info("Acquiring input signal " + source);
		for (Webcam c : Webcam.getWebcams()) {
			if (source.equals(c.getName())) {
				inputSignal = c;
				LOGGER.info("\t" + c.getName() + " Match FOUND!");
				break;
			}
			LOGGER.info("\t" + c.getName() + " did not match");
		}
	}

	private void sleepForXMillis(long waitForMilliSeconds) {
		try {
			LOGGER.debug("Waiting for " + waitForMilliSeconds + " milliseconds ... ");
			Thread.sleep(waitForMilliSeconds);
		} catch (Exception e) {
			LOGGER.fatal("An error occured when sleeping for " + waitForMilliSeconds + " milliseconds", e);
		}
	}

	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#setDimensionOfInputSignalTo1280x720()
	 */
	public void setDimensionOfInputSignalTo1280x720() {
		LOGGER.info("actual width of input signal:\t" + inputSignal.getViewSize().getWidth());
		LOGGER.info("actual height of input signal:\t" + inputSignal.getViewSize().getHeight());
		LOGGER.info("actual FPS:\t" + inputSignal.getFPS());
		Dimension d = new Dimension(1280, 720);
		inputSignal.setCustomViewSizes(new Dimension[] { d });
		inputSignal.setViewSize(d);
	}

	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#logResultsOfAllocation(java.lang.String)
	 */
	public void logResultsOfAllocation(String source) {
		LOGGER.info("actual width of input signal:\t" + inputSignal.getViewSize().getWidth());
		LOGGER.info("actual height of input signal:\t" + inputSignal.getViewSize().getHeight());
		LOGGER.info("actual FPS:\t" + inputSignal.getFPS());
		LOGGER.info("Allocated new Webcam Signal Nr. " + source.toString());
	}

	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#release_signal()
	 */
	public void release_signal() {
		inputSignal.close();
	}

	/* (non-Javadoc)
	 * @see at.dieneulingers.imagecapturer.ImageCapturer#nextFrame()
	 */
	public BufferedImage nextFrame() {
		return inputSignal.getImage();
	}
}
