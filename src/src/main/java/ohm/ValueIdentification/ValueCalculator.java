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

    public ValueCalculator(Integer a, Integer b, Integer c, Integer d){
        if (d > 10){
            resistance = (a * 10 + b)*Math.pow(10,c);
            precision = d;
        }
        else{
            resistance = (d * 10 + c)*Math.pow(10,b);
            precision = a * -1;
        }
    }

    public String getValue(){
        return Double.toString((int) resistance) + " +- " + precision + "%";
    }
}

/** @} */