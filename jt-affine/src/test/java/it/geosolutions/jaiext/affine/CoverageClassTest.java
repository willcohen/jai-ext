package it.geosolutions.jaiext.affine;

import static org.junit.Assert.*;
import it.geosolutions.jaiext.affine.AffineDataDescriptor;
import it.geosolutions.jaiext.affine.AffinePropertyGenerator;
import it.geosolutions.jaiext.interpolators.InterpolationNearestNew;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.NullDescriptor;
import org.jaitools.numeric.Range;
import org.junit.Test;


/**
 * This test-class extends the TestAffine class and is used for extending the code-coverage of the project. In this test-class
 * are checked the getProperty() method of the AffineDataDescriptor class and the capability of the AffineDataCRIF.create() 
 * method to call the TranslateIntOpImage class or the CopyOpImage class when the requested operation is simply a translation
 * or a copy of the source image without ROI object. 
 */
public class CoverageClassTest extends TestAffine {

    // this test-case is used for testing the getProperty() method of the AffineDataDescriptor class
    @Test
    public void testROIProperty() {
        AffineDataDescriptor descriptor = new AffineDataDescriptor();
        AffinePropertyGenerator propertyGenerator = (AffinePropertyGenerator) descriptor
                .getPropertyGenerators()[0];

        boolean useROIAccessor = false;
        boolean setDestinationNoData = false;
        int dataType = DataBuffer.TYPE_BYTE;
        Range<Byte> noDataRange = null;

        // Interpolator initialization
        // Nearest-Neighbor
        InterpolationNearestNew interpNear = new InterpolationNearestNew(noDataRange,
                useROIAccessor, destinationNoData, dataType);

        // ROI creation
        ROI roi = roiCreation();

        byte imageValue = 127;

        // Test image creation

        RenderedImage testImg = createTestImage(dataType, DEFAULT_WIDTH,
                DEFAULT_HEIGHT, imageValue, false);

        RenderedOp testIMG = NullDescriptor.create(testImg, null);

        // Transformation
        // Rotation 
        AffineTransform transform = AffineTransform.getQuadrantRotateInstance(numquadrants, anchorX, anchorY);
        // + Scale (X and Y doubled)
        transform.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
        // + Translation (translation towards the center of the image)
        transform.concatenate(AffineTransform.getTranslateInstance(transX, transY));
                                 
        // Affine transformated images
        RenderedOp affineImgNear = AffineDataDescriptor.create(testIMG, transform, interpNear, null, roi, useROIAccessor, setDestinationNoData, null); 

        affineImgNear.getTile(0, 0);


        // Affine operation on ROI
        ROI roiNear = (ROI) propertyGenerator.getProperty("roi", affineImgNear);
        //roiNear Bounds
        Rectangle roiNearBounds = roiNear.getBounds();
        double actualWidth = roiNearBounds.getWidth();
        double actualHeight = roiNearBounds.getHeight();
        double actualminX = roiNearBounds.getMinX();
        double actualminY = roiNearBounds.getMinY();
        
        
        Rectangle srcBounds = new Rectangle(testIMG.getMinX() + interpNear.getLeftPadding(),
                                            testIMG.getMinY() + interpNear.getTopPadding(),
                                            testIMG.getWidth() - interpNear.getWidth() + 1,
                                            testIMG.getHeight() - interpNear.getHeight() + 1);

            // If necessary, clip the ROI to the effective source bounds.
            if (!srcBounds.contains(roi.getBounds())) {
                roi = roi.intersect(new ROIShape(srcBounds));
            }

            // Create the transformed ROI.
            ROI dstROI = roi.transform(transform);

            // Retrieve the destination bounds.
            Rectangle dstBounds = affineImgNear.getBounds();

            // If necessary, clip the transformed ROI to the
            // destination bounds.
            if (!dstBounds.contains(dstROI.getBounds())) {
                dstROI = dstROI.intersect(new ROIShape(dstBounds));
            }

          //dstROI Bounds
            Rectangle dstROIBounds = dstROI.getBounds();
            double expectedWidth = dstROIBounds.getWidth();
            double expectedHeight = dstROIBounds.getHeight();
            double expectedminX = dstROIBounds.getMinX();
            double expectedminY = dstROIBounds.getMinY();
            
            //Comparison
            double tolerance = 0.1f;
            assertEquals(expectedWidth,actualWidth,tolerance);
            assertEquals(expectedHeight,actualHeight,tolerance);
            assertEquals(expectedminX,actualminX,tolerance);
            assertEquals(expectedminY,actualminY,tolerance);
    }

    @Test
    public void testTranslation() {

        boolean useROIAccessor = false;
        boolean setDestinationNoData = false;
        Range<Byte> noDataRange = null;
        int dataType = DataBuffer.TYPE_BYTE;

        byte imageValue = 127;
        
        // Nearest-Neighbor
        InterpolationNearestNew interpNear = new InterpolationNearestNew(noDataRange,
                useROIAccessor, destinationNoData, dataType);

        RenderedImage testIMG = createTestImage(dataType, DEFAULT_WIDTH, DEFAULT_HEIGHT, imageValue,
                false);

     // Transformation
        // Translation (translation towards the center of the image)
        AffineTransform transform = AffineTransform.getTranslateInstance(transX, transY);
      
        // Affine transformated images
        PlanarImage  affineImgNear = AffineDataDescriptor.create(testIMG, transform, interpNear, null, null, useROIAccessor, setDestinationNoData, null); 

        affineImgNear.getTiles();
        
        double actualX=affineImgNear.getMinX();
        double actualY=affineImgNear.getMinY();
        
        double expectedX=testIMG.getMinX()+ transX;
        double expectedY=testIMG.getMinY()+ transY;
        
        double tolerance = 0.1f;
        
        assertEquals(expectedX, actualX,tolerance);
        assertEquals(expectedY, actualY,tolerance);
        
        double actualWidth=affineImgNear.getWidth();
        double actualHeight=affineImgNear.getHeight();
        
        double expectedWidth=testIMG.getWidth();
        double expectedHeigh=testIMG.getHeight();
        
        assertEquals(expectedWidth, actualWidth,tolerance);
        assertEquals(expectedHeigh, actualHeight,tolerance);  
        
    }
    
    @Test
    public void testCopy() {

        boolean useROIAccessor = false;
        boolean setDestinationNoData = false;
        Range<Byte> noDataRange = null;
        int dataType = DataBuffer.TYPE_BYTE;

        byte imageValue = 127;
        
        // Nearest-Neighbor
        InterpolationNearestNew interpNear = new InterpolationNearestNew(noDataRange,
                useROIAccessor, destinationNoData, dataType);

        RenderedImage testIMG = createTestImage(dataType, DEFAULT_WIDTH, DEFAULT_HEIGHT, imageValue,
                false);

     // Transformation
        // Translation (translation towards the center of the image)
        AffineTransform transform = new AffineTransform();
      
        // Affine transformated images
        PlanarImage  affineImgNear = AffineDataDescriptor.create(testIMG, transform, interpNear, null, null, useROIAccessor, setDestinationNoData, null); 

        affineImgNear.getTiles();
        
        double actualX=affineImgNear.getMinX();
        double actualY=affineImgNear.getMinY();
        
        double expectedX=testIMG.getMinX();
        double expectedY=testIMG.getMinY();
        
        double tolerance = 0.1f;
        
        assertEquals(expectedX, actualX,tolerance);
        assertEquals(expectedY, actualY,tolerance);    
        
        double actualWidth=affineImgNear.getWidth();
        double actualHeight=affineImgNear.getHeight();
        
        double expectedWidth=testIMG.getWidth();
        double expectedHeigh=testIMG.getHeight();
        
        assertEquals(expectedWidth, actualWidth,tolerance);
        assertEquals(expectedHeigh, actualHeight,tolerance);  
        
    
    }    
}
