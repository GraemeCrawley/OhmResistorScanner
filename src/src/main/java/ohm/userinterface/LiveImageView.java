package ohm.userinterface;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @addtogroup UserInterface
 * @{
 */


/**
 * @brief
 * The LiveImageView is an ImageView that periodically calls a Renderer
 * to produce a new image that the ImageView then displays.
 * This is useful to create images from real time updating images/data
 */
public class LiveImageView extends ImageView {

    private final LiveImageView imageView = this;
    private int frameRate = 12;
    private Renderer renderer = null;

    public LiveImageView() {
        super();
        Thread redrawer = new Thread(new Runnable(){
            @Override
            public void run() {
                boolean running = true;
                while (running){
                    if (renderer != null) {
                        imageView.setImage(renderer.render());
                    }
                    try {
                        Thread.sleep(1000 / frameRate);
                    }catch (InterruptedException e){
                        running = false;
                    }
                }
            }});
        redrawer.start();
    }

    public void setFrameRate(int frameRate){
        this.frameRate = frameRate;
    }

    public void setRenderer(Renderer newRenderer){
        this.renderer = newRenderer;
    }

    interface Renderer {
        Image render();
    }

}

/** @}*/