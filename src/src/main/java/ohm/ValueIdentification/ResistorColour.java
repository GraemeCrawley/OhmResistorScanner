package ohm.ValueIdentification;


/**
 * @defgroup ColourMapping
 * @author Jonathan Brown
 *@{
 */

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Arc2D;

/**
 * @brief Enum containing all of the possible colours that a resistor can take on. Also features member functions used to map the colours of bands to values used in the calculation process.
 */
public enum ResistorColour {
    BLACK(0,20,20,20),
    BROWN(1,70,45,35),
    RED(2,155,20,35),
    ORANGE(3,245,105,30),
    YELLOW(4,200,200,55),
    GREEN(5,20,75,40),
    BLUE(6,5,70,170),
    VIOLET(7,50,33,80),
    GREY(8,125,125,125),
    WHITE(9,200,200,200),
    GOLD(-5,140,110,50),
    //SILVER(),
    BASE(-1,200,170,145);
    public int value;
    float[] rgb;
    float[] hsb;
    float[] lab;
    /**
     *
     * @param v The number represented by the colour in the calculation of the resistor's ohmage.
     * @param r The red value of the colour in RGB space. Ranges between 0 and 255.
     * @param g The green value of the colour in RGB space. Ranges between 0 and 255.
     * @param b The blue value of the colour in RGB space. Ranges between 0 and 255.
     */
    ResistorColour(int v, int r, int g, int b){
        value = v;
        rgb = new float[3];
        rgb[0] = r; rgb[1] = g; rgb[2] = b;
        hsb = Color.RGBtoHSB(r,g,b,null);
        hsb[0] = hsb[0] * 360;
        lab = new float[3];
        rgb2lab(r,g,b,lab);
    }


    /**
     * //http://www.brucelindbloom.com
     * @param R
     * @param G
     * @param B
     * @param lab
     */
    public static void rgb2lab(float R, float G, float B, float[] lab) {


        float r, g, b, X, Y, Z, fx, fy, fz, xr, yr, zr;
        float Ls, as, bs;
        float eps = 216.f/24389.f;
        float k = 24389.f/27.f;

        float Xr = 0.964221f;  // reference white D50
        float Yr = 1.0f;
        float Zr = 0.825211f;

        // RGB to XYZ
        r = R/255.f; //R 0..1
        g = G/255.f; //G 0..1
        b = B/255.f; //B 0..1

        // assuming sRGB (D65)
        if (r <= 0.04045)
            r = r/12;
        else
            r = (float) Math.pow((r+0.055)/1.055,2.4);

        if (g <= 0.04045)
            g = g/12;
        else
            g = (float) Math.pow((g+0.055)/1.055,2.4);

        if (b <= 0.04045)
            b = b/12;
        else
            b = (float) Math.pow((b+0.055)/1.055,2.4);


        X =  0.436052025f*r     + 0.385081593f*g + 0.143087414f *b;
        Y =  0.222491598f*r     + 0.71688606f *g + 0.060621486f *b;
        Z =  0.013929122f*r     + 0.097097002f*g + 0.71418547f  *b;

        // XYZ to Lab
        xr = X/Xr;
        yr = Y/Yr;
        zr = Z/Zr;

        if ( xr > eps )
            fx =  (float) Math.pow(xr, 1/3.);
        else
            fx = (float) ((k * xr + 16.) / 116.);

        if ( yr > eps )
            fy =  (float) Math.pow(yr, 1/3.);
        else
            fy = (float) ((k * yr + 16.) / 116.);

        if ( zr > eps )
            fz =  (float) Math.pow(zr, 1/3.);
        else
            fz = (float) ((k * zr + 16.) / 116);

        Ls = ( 116 * fy ) - 16;
        as = 500*(fx-fy);
        bs = 200*(fy-fz);

        lab[0] = (float) (2.55*Ls + .5);
        lab[1] = (float) (as + .5) + 128;
        lab[2] = (float) (bs + .5) + 128;
    }

    public static ResistorColour fit(float r, float g, float b, int colorSpace){
        float[] labSamplePixel = new float[3];
        if (colorSpace == ColorSpace.TYPE_RGB){
            float[] rgbSamplePixel = {r,g,b};
            rgb2lab(r,g,b,labSamplePixel);
        }
        else if (colorSpace == ColorSpace.TYPE_Lab){
            labSamplePixel[0] = r; labSamplePixel[1] = g; labSamplePixel[2] = b;
        }
        double minDistance = Double.MAX_VALUE;
        ResistorColour closestColour = null;
        for (ResistorColour colour : ResistorColour.values()){
            //double distance = Math.sqrt(RGBDistance(r,g,b,colour)*HSBDistance(r,g,b,colour));
            double distance = LABDistance(labSamplePixel[0],labSamplePixel[1],labSamplePixel[2],colour);
            if (distance < minDistance){
                closestColour = colour;
                minDistance = distance;
            }
        }
        if (closestColour == null) throw new RuntimeException();
        return closestColour;
    }

    /**
     * Function takes in a sampled colour from the images and attempts to fit it to the closest known colour a resistor can possess.
     * @param r The red colour value of the colour to be fit.
     * @param g The green colour value of the colour to be fit.
     * @param b The blue colour value of the colour to be fit.
     * @return The known colour that best represents the sampled colour.
     */
    public static ResistorColour fit(int r, int g, int b, int colorSpace){
        return fit((float) r, (float) g, (float) b, colorSpace);
    }


    /**
     * Function takes in a sampled colour from the images and attempts to fit it to the closest known colour a resistor can possess.
     * @param r The red colour value of the colour to be fit.
     * @param g The green colour value of the colour to be fit.
     * @param b The blue colour value of the colour to be fit.
     * @return The known colour that best represents the sampled colour.
     */
    public static ResistorColour fit(double r, double g, double b, int colorSpace){
        return ResistorColour.fit((float) r, (float) g, (float) b, colorSpace);
    }

    /**
     *
     * @param r The red colour value of the colour to be fit.
     * @param g The green colour value of the colour to be fit.
     * @param b The blue colour value of the colour to be fit.
     * @param c The ResistorColour that the sample colour is being compared to.
     * @return The distance in RGB space between the sample colour and the known resistor colour.
     */
    private static double RGBDistance(float r, float g, float b, ResistorColour c){
        if (c == null) throw new IllegalArgumentException("C cannot be null");
        final double dR = c.rgb[0] - r;
        final double dG = c.rgb[1] - g;
        final double dB = c.rgb[2] - b;
        final double distance = Math.sqrt(dR*dR + dG*dG + dB*dB);
        return distance;
    }


    private static float LABDistance(float l, float a, float b, ResistorColour c){
        if (c == null) throw new IllegalArgumentException("C cannot be null");
        final float dL = c.lab[0] - l;
        final float dA = c.lab[1] - a;
        final float dB = c.lab[2] - b;
        final float distance = (float)  Math.sqrt(dL*dL + dA*dA + dB*dB);
        return distance;

    }

    /**
     *
     * @param r The red colour value of the colour to be fit.
     * @param g The green colour value of the colour to be fit.
     * @param b The blue colour value of the colour to be fit.
     * @param c The ResistorColour that the sample colour is being compared to.
     * @return The distance in R/G, R/B space between the sample colour and the known resistor colour.
     */
    private static double HSBDistance(int r, int g, int b, ResistorColour c){
        if (c == null) throw new IllegalArgumentException("C cannot be null");
        int hue = (int) (Color.RGBtoHSB(r,g,b, null)[0]*360);
        return Math.abs(c.hsb[0] - hue);
    }

    private boolean isShadeOfGrey(int r, int g, int b){
        return false;
    }

    public static void main(String[] args){
        for (ResistorColour i: ResistorColour.values()){
            System.out.println(i.toString() + "  " + ((int)i.rgb[0])+ "  " + ((int)i.rgb[1])+ "  " + ((int)i.rgb[2]));
            System.out.println(i.toString() + "  " + (i.hsb[0])+ "  " + (i.hsb[1])+ "  " + (i.hsb[2]));
            System.out.println(i.toString() + "  " + i.lab[0] + "  " + i.lab[1]+ "  " + i.lab[2]);
        }
    }


}

/** @} */