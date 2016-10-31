package ohm;

public enum ResistorColour {
    BLACK(0),
    BROWN(1),
    RED(2),
    ORANGE(3),
    YELLOW(4),
    GREEN(5),
    BLUE(6),
    VIOLET(7),
    GREY(8),
    WHITE(9),
    //GOLD(),
    //SILVER,
    BASE(-1);


    private int value;

    ResistorColour(int v){
        value = v;
    }


}
