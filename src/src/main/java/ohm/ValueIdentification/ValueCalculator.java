package ohm.ValueIdentification;

/**
 * @defgroup ValueCalculator
 * @author Jonathan Brown
 *@{
 */

/**
 * @brief Object used to calculate the resistance of the resistor based on the mapped colours.
 */
public class ValueCalculator {
    final double resistance;
    final double precision;

    public ValueCalculator(ResistorColour a, ResistorColour b, ResistorColour c, ResistorColour d){
        if (d.value < 0){
            resistance = (a.value * 10 + b.value)*Math.pow(10,c.value);
            precision = d.value;
        }
        else{
            resistance = (d.value * 10 + c.value)*Math.pow(10,b.value);
            precision = a.value * -1;
        }
    }

    public String getValue(){
        return Double.toString((int) resistance) + " +- " + precision + "%";
    }
}

/** @} */