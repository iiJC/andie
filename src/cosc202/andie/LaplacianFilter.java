package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply Laplacian filter.
 * </p>
 * 
 * <p>
 * Implements a Laplacian filter with the choice of an offset 
 * </p>
 * 
 * @author Hadrien Tang
 */


public class LaplacianFilter implements ImageOperation, java.io.Serializable {
    //the kernel values for the horizontal and vertical filter
    float[] array = {0, -1, 0,-1,  4, -1,0, -1, 0};
    private boolean offset; 
    

    LaplacianFilter(boolean offset){
        this.offset = offset; 
    }

    public BufferedImage apply(BufferedImage input) {
        try {
            
            Kernel kernel = new Kernel(3,3,array);
            Convo2 convoOp = new Convo2(kernel,offset);
            //ConvolveOp convOp = new ConvolveOp(kernel);
            BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                    input.isAlphaPremultiplied(), null);
            convoOp.filter(input, output);

            return output;
        } catch (NullPointerException e) {
            System.err.println(Settings.getLanguageProperty("WARN_NULL_INPUT"));
            return input;
        }
    }
}