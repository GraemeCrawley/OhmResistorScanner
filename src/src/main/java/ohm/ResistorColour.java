package ohm;

/**
 * Created by jon on 2016-11-06.
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
    BASE(-1,200,170,145);

    int value;
    double red;
    double green;
    double blue;

    ResistorColour(int v, double r, double g, double b){
        value = v;
        red = r;
        green = g;
        blue = b;
    }

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
        return closestColour;
    }

    private static double distance(int r, int g, int b, ResistorColour c){
        final double dR = c.red - r;
        final double dG = c.green - g;
        final double dB = c.blue - b;
        final double distance = Math.sqrt(dR*dR + dG*dG + dB*dB);
        return distance;

    }
}
