package ohm;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by graeme on 06/11/16.
 */
public class ResistorColourTest {
    //Baseline Tests for standard values
    @Test
    public void control1() throws Exception {
        assertEquals(ResistorColour.fit(200,200,200),ResistorColour.WHITE);
    }
    @Test
    public void control2() throws Exception {
        assertEquals(ResistorColour.fit(0,0,0),ResistorColour.BLACK);
    }
    @Test
    public void control3() throws Exception {
        assertEquals(ResistorColour.fit(70,45,35),ResistorColour.BROWN);
    }
    @Test
    public void control4() throws Exception {
        assertEquals(ResistorColour.fit(20,75,40),ResistorColour.GREEN);
    }
    @Test
    public void control5() throws Exception {
        assertEquals(ResistorColour.fit(200,170,145),ResistorColour.BASE);
    }

    //New colour tests start here
    //Green (5)
    @Test
    public void green1() throws Exception {
        assertEquals(ResistorColour.fit(8,91,81),ResistorColour.GREEN);
    }
    @Test
    public void green2() throws Exception {
        assertEquals(ResistorColour.fit(6,57,52),ResistorColour.GREEN);
    }
    @Test
    public void green3() throws Exception {
        assertEquals(ResistorColour.fit(44,128,105),ResistorColour.GREEN);
    }
    @Test
    public void green4() throws Exception {
        assertEquals(ResistorColour.fit(5,51,41),ResistorColour.GREEN);
    }
    @Test
    public void green5() throws Exception {
        assertEquals(ResistorColour.fit(9,102,75),ResistorColour.GREEN);
    }

    //Blue (5)
    @Test
    public void blue1() throws Exception {
        assertEquals(ResistorColour.fit(32,36,84),ResistorColour.BLUE);
    }
    @Test
    public void blue2() throws Exception {
        assertEquals(ResistorColour.fit(46,53,105),ResistorColour.BLUE);
    }
    @Test
    public void blue3() throws Exception {
        assertEquals(ResistorColour.fit(90,98,144),ResistorColour.BLUE);
    }
    @Test
    public void blue4() throws Exception {
        assertEquals(ResistorColour.fit(23,22,53),ResistorColour.BLUE);
    }
    @Test
    public void blue5() throws Exception {
        assertEquals(ResistorColour.fit(31,43,91),ResistorColour.BLUE);
    }

    //Red (5)
    @Test
    public void red1() throws Exception {
        assertEquals(ResistorColour.fit(190,78,38),ResistorColour.RED);
    }
    @Test
    public void red2() throws Exception {
        assertEquals(ResistorColour.fit(132,40,6),ResistorColour.RED);
    }
    @Test
    public void red3() throws Exception {
        assertEquals(ResistorColour.fit(161,66,20),ResistorColour.RED);
    }
    @Test
    public void red4() throws Exception {
        assertEquals(ResistorColour.fit(176,64,29),ResistorColour.RED);
    }
    @Test
    public void red5() throws Exception {
        assertEquals(ResistorColour.fit(145,54,26),ResistorColour.RED);
    }

    //Yellow (5)
    @Test
    public void yellow1() throws Exception {
        assertEquals(ResistorColour.fit(202,163,8),ResistorColour.YELLOW);
    }
    @Test
    public void yellow2() throws Exception {
        assertEquals(ResistorColour.fit(181,139,32),ResistorColour.YELLOW);
    }
    @Test
    public void yellow3() throws Exception {
        assertEquals(ResistorColour.fit(201,162,8),ResistorColour.YELLOW);
    }
    @Test
    public void yellow4() throws Exception {
        assertEquals(ResistorColour.fit(180,138,3),ResistorColour.YELLOW);
    }
    @Test
    public void yellow5() throws Exception {
        assertEquals(ResistorColour.fit(196,158,33),ResistorColour.YELLOW);
    }

    //Brown (5)
    @Test
    public void brown1() throws Exception {
        assertEquals(ResistorColour.fit(122,73,40),ResistorColour.BROWN);
    }
    @Test
    public void brown2() throws Exception {
        assertEquals(ResistorColour.fit(136,80,43),ResistorColour.BROWN);
    }
    @Test
    public void brown3() throws Exception {
        assertEquals(ResistorColour.fit(142,89,45),ResistorColour.BROWN);
    }
    @Test
    public void brown4() throws Exception {
        assertEquals(ResistorColour.fit(163,116,70),ResistorColour.BROWN);
    }
    @Test
    public void brown5() throws Exception {
        assertEquals(ResistorColour.fit(116,62,28),ResistorColour.BROWN);
    }

    //Black (5)
    @Test
    public void black1() throws Exception {
        assertEquals(ResistorColour.fit(37,21,6),ResistorColour.BLACK);
    }
    @Test
    public void black2() throws Exception {
        assertEquals(ResistorColour.fit(23,18,7),ResistorColour.BLACK);
    }
    @Test
    public void black3() throws Exception {
        assertEquals(ResistorColour.fit(55,43,19),ResistorColour.BLACK);
    }
    @Test
    public void black4() throws Exception {
        assertEquals(ResistorColour.fit(40,34,21),ResistorColour.BLACK);
    }
    @Test
    public void black5() throws Exception {
        assertEquals(ResistorColour.fit(25,18,5),ResistorColour.BLACK);
    }

    //Orange (5)
    @Test
    public void orange1() throws Exception {
        assertEquals(ResistorColour.fit(211,105,5),ResistorColour.ORANGE);
    }
    @Test
    public void orange2() throws Exception {
        assertEquals(ResistorColour.fit(227,122,5),ResistorColour.ORANGE);
    }
    @Test
    public void orange3() throws Exception {
        assertEquals(ResistorColour.fit(168,85,7),ResistorColour.ORANGE);
    }
    @Test
    public void orange4() throws Exception {
        assertEquals(ResistorColour.fit(198,105,6),ResistorColour.ORANGE);
    }
    @Test
    public void orange5() throws Exception {
        assertEquals(ResistorColour.fit(183,94,6),ResistorColour.ORANGE);
    }

    //Violet (5)
    @Test
    public void violet1() throws Exception {
        assertEquals(ResistorColour.fit(126,25,80),ResistorColour.VIOLET);
    }
    @Test
    public void violet2() throws Exception {
        assertEquals(ResistorColour.fit(106,1,64),ResistorColour.VIOLET);
    }
    @Test
    public void violet3() throws Exception {
        assertEquals(ResistorColour.fit(129,26,87),ResistorColour.VIOLET);
    }
    @Test
    public void violet4() throws Exception {
        assertEquals(ResistorColour.fit(131,33,92),ResistorColour.VIOLET);
    }
    @Test
    public void violet5() throws Exception {
        assertEquals(ResistorColour.fit(93,5,51),ResistorColour.VIOLET);
    }

    //White (5)
    @Test
    public void white1() throws Exception {
        assertEquals(ResistorColour.fit(164,194,255),ResistorColour.WHITE);
    }
    @Test
    public void white2() throws Exception {
        assertEquals(ResistorColour.fit(197,249,249),ResistorColour.WHITE);
    }
    @Test
    public void white3() throws Exception {
        assertEquals(ResistorColour.fit(208,253,252),ResistorColour.WHITE);
    }
    @Test
    public void white4() throws Exception {
        assertEquals(ResistorColour.fit(215,253,253),ResistorColour.WHITE);
    }
    @Test
    public void white5() throws Exception {
        assertEquals(ResistorColour.fit(199,245,245),ResistorColour.WHITE);
    }

    //Grey (5)
    @Test
    public void grey1() throws Exception {
        assertEquals(ResistorColour.fit(170,146,118),ResistorColour.GREY);
    }
    @Test
    public void grey2() throws Exception {
        assertEquals(ResistorColour.fit(181,157,133),ResistorColour.GREY);
    }
    @Test
    public void grey3() throws Exception {
        assertEquals(ResistorColour.fit(180,156,129),ResistorColour.GREY);
    }
    @Test
    public void grey4() throws Exception {
        assertEquals(ResistorColour.fit(194,163,119),ResistorColour.GREY);
    }
    @Test
    public void grey5() throws Exception {
        assertEquals(ResistorColour.fit(167,141,111),ResistorColour.GREY);
    }

    //Base (5)
    @Test
    public void base1() throws Exception {
        assertEquals(ResistorColour.fit(210,174,118),ResistorColour.BASE);
    }
    @Test
    public void base2() throws Exception {
        assertEquals(ResistorColour.fit(220,174,118),ResistorColour.BASE);
    }
    @Test
    public void base3() throws Exception {
        assertEquals(ResistorColour.fit(205,156,98),ResistorColour.BASE);
    }
    @Test
    public void base4() throws Exception {
        assertEquals(ResistorColour.fit(230,199,61),ResistorColour.BASE);
    }
    @Test
    public void base5() throws Exception {
        assertEquals(ResistorColour.fit(227,188,141),ResistorColour.BASE);
    }



}