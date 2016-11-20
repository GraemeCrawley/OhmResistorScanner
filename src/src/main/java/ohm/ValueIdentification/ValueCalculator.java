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

    ValueCalculator(ResistorColour bandOne, ResistorColour bandTwo, ResistorColour exponentBand, int precisionBand){
        resistance = (bandOne.value * 10 + bandTwo.value)*Math.pow(10,exponentBand.value);
        precision = precisionBand;
    }
}

/** @} */