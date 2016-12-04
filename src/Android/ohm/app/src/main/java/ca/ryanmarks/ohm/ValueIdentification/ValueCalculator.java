package ca.ryanmarks.ohm.ValueIdentification;

/**
 * @defgroup ValueCalculator
 * @author Jonathan Brown
 *@{
 */

import java.text.DecimalFormat;

/**
 * @brief Object used to calculate the resistance of the resistor based on the mapped colours.
 */
public class ValueCalculator {
    final double resistance;

    private static DecimalFormat df2 = new DecimalFormat("00E0");


    public ValueCalculator(ResistorColour a, ResistorColour b, ResistorColour c){
        resistance = (a.value * 10 + b.value)*Math.pow(10,c.value);
    }

    public String getValue(){

        return df2.format(resistance);
    }
}

/** @} */