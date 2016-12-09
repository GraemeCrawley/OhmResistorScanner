package ohm.valueidentification;

/**
 * @defgroup ValueCalculator
 * @author Jonathan Brown
 *
 *@{
 */

/**
 * @author Jonathan Brown
 * @brief Object used to calculate the resistance of the resistor based on the mapped colours.
 */
public class ValueCalculator {
    final double resistance;
    final double precision;

    /**
     *
     * @param a Leftmost band value
     * @param b 2nd leftmost band value
     * @param c 3rd leftmost band value
     * @param d Rightmost band value
     */
    public ValueCalculator(Integer a, Integer b, Integer c, Integer d){
        b = b >= 10 ? 3 : b;
        c = c >= 10 ? 3 : c;

        if (d >= 10){
            resistance = (a * 10 + b)*Math.pow(10,c);
            precision = 5;
        }
        else{
            resistance = (d * 10 + c)*Math.pow(10,b);
            precision = 5;
        }
    }


    /**
     * Method to obtain the ohmage of the resistor calculated by the ValueCalculator.
     * @return
     */
    public String getValue(){
        return Double.toString((int) resistance) + " +- " + precision + "%";
    }
}

/** @} */