package ohm;
/**
 * @author Jonathan Brown
 */
public enum ResistorColour {
    BLACK(0,0,0,0),
    BROWN(1,70,45,35),
    RED(2,155,0,35),
    ORANGE(3,245,105,30),
    YELLOW(4,200,200,55),
    GREEN(5,20,75,40),
    BLUE(6,45,45,140),
    VIOLET(7,50,33,80),
    GREY(8,125,125,125),
    WHITE(9,200,200,200),
    //GOLD(),
    //SILVER(),
    BASE(-1,200,170,145);

    int value;
    double red;
    double green;
    double blue;

    /**
     *
     * @param v The number represented by the colour in the calculation of the resistor's ohmage.
     * @param r The red value of the colour in RGB space. Ranges between 0 and 255.
     * @param g The green value of the colour in RGB space. Ranges between 0 and 255.
     * @param b The blue value of the colour in RGB space. Ranges between 0 and 255.
     */
    ResistorColour(int v, double r, double g, double b){
        value = v;
        red = r;
        green = g;
        blue = b;
    }

    /**
     * Function takes in a sampled colour from the images and attempts to fit it to the closest known colour a resistor can possess.
     * @param r The red colour value of the colour to be fit.
     * @param g The green colour value of the colour to be fit.
     * @param b The blue colour value of the colour to be fit.
     * @return The known colour that best represents the sampled colour.
     */
    public static ResistorColour fit(int r, int g, int b){
        double minDistance = Double.MAX_VALUE;
        ResistorColour closestColour = null;
        for (ResistorColour colour : ResistorColour.values()){
            double distance = distance(r,g,b,colour);
            if (distance < minDistance){
                closestColour = colour;
                minDistance = distance;
            }

        }
        if (closestColour == null) throw new RuntimeException();
        return closestColour;
    }

    /**
     *
     * @param r The red colour value of the colour to be fit.
     * @param g The green colour value of the colour to be fit.
     * @param b The blue colour value of the colour to be fit.
     * @param c The ResistorColour that the sample colour is being compared to.
     * @return The distance in RGB space between the sample colour and the known resistor colour.
     */
    private static double distance(int r, int g, int b, ResistorColour c){
        if (c == null) throw new IllegalArgumentException("C cannot be null");
        final double dR = c.red - r;
        final double dG = c.green - g;
        final double dB = c.blue - b;
        final double distance = Math.sqrt(dR*dR + dG*dG + dB*dB);
        return distance;

    }
}
