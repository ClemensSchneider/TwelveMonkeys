package com.twelvemonkeys.image;

import junit.framework.TestCase;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ResampleOpTestCase
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haku $
 * @version $Id: //depot/branches/personal/haraldk/twelvemonkeys/release-2/twelvemonkeys-core/src/test/java/com/twelvemonkeys/image/ResampleOpTestCase.java#1 $
 */
public class ResampleOpTestCase extends TestCase {

    protected BufferedImage createImage(final int pWidth, final int pHeigth) {
        return createImage(pWidth, pHeigth, BufferedImage.TYPE_INT_ARGB);
    }

    protected BufferedImage createImage(final int pWidth, final int pHeigth, final int pType) {
        BufferedImage image = new BufferedImage(pWidth, pHeigth, pType);
        Graphics2D g = image.createGraphics();
        try {
            g.setPaint(new GradientPaint(0, 0, Color.RED, pWidth, pHeigth, new Color(0x00000000, true)));
            g.fillRect(0, 0, pWidth, pHeigth);
        }
        finally {
            g.dispose();
        }

        return image;
    }

    public void testCreateImage() {
        // Sanity test the create method
        BufferedImage image = createImage(79, 84);
        assertNotNull(image);
        assertEquals(79, image.getWidth());
        assertEquals(84, image.getHeight());
    }

    private void assertResample(final BufferedImage pImage, final int pWidth, final int pHeight, final int pFilterType) {
        BufferedImage result = new ResampleOp(pWidth, pHeight, pFilterType).filter(pImage, null);
        assertNotNull(result);
        assertEquals(pWidth, result.getWidth());
        assertEquals(pHeight, result.getHeight());

        result = new ResampleOp(pImage.getWidth(), pImage.getHeight(), pFilterType).filter(createImage(pWidth, pHeight), pImage);
        assertNotNull(result);
        assertEquals(pImage.getType(), result.getType());
        assertSame(pImage, result);
        assertEquals(pImage.getWidth(), result.getWidth());
        assertEquals(pImage.getHeight(), result.getHeight());

        result = new ResampleOp(pImage.getWidth(), pImage.getHeight(), pFilterType).filter(createImage(pWidth, pHeight), createImage(pWidth, pHeight, pImage.getType()));
        assertNotNull(result);
        assertEquals(pImage.getType(), result.getType());
        assertEquals(pWidth, result.getWidth());
        assertEquals(pHeight, result.getHeight());
    }

    private void assertResampleBufferedImageTypes(final int pFilterType) {
        List<String> exceptions = new ArrayList<String>();

        // Test all image types in BufferedImage
        for (int type = BufferedImage.TYPE_INT_ARGB; type <= BufferedImage.TYPE_BYTE_INDEXED; type++) {
            // TODO: Does not currently work with TYPE_BYTE_GRAY or TYPE_USHORT_GRAY
            // TODO: FixMe!
            if ((pFilterType == ResampleOp.FILTER_POINT || pFilterType == ResampleOp.FILTER_TRIANGLE) &&
                    (type == BufferedImage.TYPE_BYTE_GRAY || type == BufferedImage.TYPE_USHORT_GRAY)) {
                continue;
            }

            BufferedImage image = createImage(10, 10, type);
            try {
                assertResample(image, 15, 5, pFilterType);
            }
            catch (ImagingOpException e) {
                // NOTE: It is currently allowed for filters to throw this exception and it is PLATFORM DEPENDENT..
                System.err.println("WARNING: " + e.getMessage() + ", image: " + image);
                //e.printStackTrace();
            }
            catch (Throwable t) {
                exceptions.add(t.toString() + ": " + image.toString());
            }
        }

        assertEquals("Filter threw exceptions: ", Collections.EMPTY_LIST, exceptions);
    }

    // 1x1
    public void testResample1x1Point() {
        assertResample(createImage(1, 1), 10, 11, ResampleOp.FILTER_POINT);
    }

    public void testResample1x1Box() {
        assertResample(createImage(1, 1), 10, 11, ResampleOp.FILTER_BOX);
    }

    public void testResample1x1Triangle() {
        assertResample(createImage(1, 1), 19, 13, ResampleOp.FILTER_TRIANGLE);
    }

    public void testResample1x1Lanczos() {
        assertResample(createImage(1, 1), 7, 49, ResampleOp.FILTER_LANCZOS);
    }

    public void testResample1x1Gaussian() {
        assertResample(createImage(1, 1), 11, 34, ResampleOp.FILTER_GAUSSIAN);
    }

    public void testResample1x1Sinc() {
        assertResample(createImage(1, 1), 2, 8, ResampleOp.FILTER_BLACKMAN_SINC);
    }

    // 2x2
    public void testResample2x2Point() {
        assertResample(createImage(2, 2), 10, 11, ResampleOp.FILTER_POINT);
    }

    public void testResample2x2Box() {
        assertResample(createImage(2, 2), 10, 11, ResampleOp.FILTER_BOX);
    }

    public void testResample2x2Triangle() {
        assertResample(createImage(2, 2), 19, 13, ResampleOp.FILTER_TRIANGLE);
    }

    public void testResample2x2Lanczos() {
        assertResample(createImage(2, 2), 7, 49, ResampleOp.FILTER_LANCZOS);
    }

    public void testResample2x2Gaussian() {
        assertResample(createImage(2, 2), 11, 34, ResampleOp.FILTER_GAUSSIAN);
    }

    public void testResample2x2Sinc() {
        assertResample(createImage(2, 2), 2, 8, ResampleOp.FILTER_BLACKMAN_SINC);
    }

    // 3x3
    public void testResample3x3Point() {
        assertResample(createImage(3, 3), 10, 11, ResampleOp.FILTER_POINT);
    }

    public void testResample3x3Box() {
        assertResample(createImage(3, 3), 10, 11, ResampleOp.FILTER_BOX);
    }

    public void testResample3x3Triangle() {
        assertResample(createImage(3, 3), 19, 13, ResampleOp.FILTER_TRIANGLE);
    }

    public void testResample3x3Lanczos() {
        assertResample(createImage(3, 3), 7, 49, ResampleOp.FILTER_LANCZOS);
    }

    public void testResample3x3Gaussian() {
        assertResample(createImage(3, 3), 11, 34, ResampleOp.FILTER_GAUSSIAN);
    }

    public void testResample3x3Sinc() {
        assertResample(createImage(3, 3), 2, 8, ResampleOp.FILTER_BLACKMAN_SINC);
    }

    // 4x4
    public void testResample4x4Point() {
        assertResample(createImage(4, 4), 10, 11, ResampleOp.FILTER_POINT);
    }

    public void testResample4x4Box() {
        assertResample(createImage(4, 4), 10, 11, ResampleOp.FILTER_BOX);
    }

    public void testResample4x4Triangle() {
        assertResample(createImage(4, 4), 19, 13, ResampleOp.FILTER_TRIANGLE);
    }

    public void testResample4x4Lanczos() {
        assertResample(createImage(4, 4), 7, 49, ResampleOp.FILTER_LANCZOS);
    }

    public void testResample4x4Gaussian() {
        assertResample(createImage(4, 4), 11, 34, ResampleOp.FILTER_GAUSSIAN);
    }

    public void testResample4x4Sinc() {
        assertResample(createImage(4, 4), 2, 8, ResampleOp.FILTER_BLACKMAN_SINC);
    }

    // 20x20
    public void testResample20x20Point() {
        assertResample(createImage(20, 20), 10, 11, ResampleOp.FILTER_POINT);
    }

    public void testResample20x20Box() {
        assertResample(createImage(20, 20), 10, 11, ResampleOp.FILTER_BOX);
    }

    public void testResample20x20Triangle() {
        assertResample(createImage(20, 20), 19, 13, ResampleOp.FILTER_TRIANGLE);
    }

    public void testResample20x20Lanczos() {
        assertResample(createImage(20, 20), 7, 49, ResampleOp.FILTER_LANCZOS);
    }

    public void testResample20x20Gaussian() {
        assertResample(createImage(20, 20), 11, 34, ResampleOp.FILTER_GAUSSIAN);
    }

    public void testResample20x20Sinc() {
        assertResample(createImage(20, 20), 2, 8, ResampleOp.FILTER_BLACKMAN_SINC);
    }

    // 200x160
    public void testResample200x160Point() {
        assertResample(createImage(200, 160), 10, 11, ResampleOp.FILTER_POINT);
    }

    public void testResample200x160Box() {
        assertResample(createImage(200, 160), 10, 11, ResampleOp.FILTER_BOX);
    }

    public void testResample200x160Triangle() {
        assertResample(createImage(200, 160), 19, 13, ResampleOp.FILTER_TRIANGLE);
    }

    public void testResample200x160Lanczos() {
        assertResample(createImage(200, 160), 7, 49, ResampleOp.FILTER_LANCZOS);
    }

    public void testResample200x160Gaussian() {
        assertResample(createImage(200, 160), 11, 34, ResampleOp.FILTER_GAUSSIAN);
    }

    public void testResample200x160Sinc() {
        assertResample(createImage(200, 160), 2, 8, ResampleOp.FILTER_BLACKMAN_SINC);
    }

    // Test 10x10 -> 15x5 with different algorithms and types
    public void testResamplePoint() {
        assertResampleBufferedImageTypes(ResampleOp.FILTER_POINT);
    }

    public void testResampleBox() {
        assertResampleBufferedImageTypes(ResampleOp.FILTER_BOX);
    }

    public void testResampleTriangle() {
        assertResampleBufferedImageTypes(ResampleOp.FILTER_TRIANGLE);
    }

    public void testResampleLanczos() {
        assertResampleBufferedImageTypes(ResampleOp.FILTER_LANCZOS);
    }
}

