package com.lordmau5.harvest.shared.floor;

import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.farmable.crops.CropState;
import com.lordmau5.harvest.shared.farmable.crops.ICrop;
import org.newdawn.slick.geom.Rectangle;

/**
 * @author: Lordmau5
 * @time: 16.06.2014 - 21:10
 */
public class Farmland {

    private boolean tilled = false;
    private boolean watered = false;

    public Rectangle boundingBox;

    public Tile tile;
    private ICrop crop;

    public Farmland(int tX, int tY) {
        this.tile = new Tile(tX, tY);
        this.boundingBox = new Rectangle(tX * 16 + 1, tY * 16, 16, 16);

        this.crop = null;
        this.tilled = false;
        this.watered = false;
    }

    public void setTilled(boolean tilled) {
        this.tilled = tilled;
        if(!tilled) {
            setWatered(false);
            setCrop(null);
        }
    }

    public void setWatered(boolean watered) {
        this.watered = watered;
    }

    public void setCrop(ICrop crop) {
        this.crop = crop;
    }

    public boolean isTilled() {
        return tilled;
    }

    public boolean isWatered() {
        return watered;
    }

    public ICrop getCrop() {
        return crop;
    }

    public boolean hasCrop() { return crop != null; }

    public void operate() {
        if(hasCrop()) {
            ICrop crop = getCrop();
            CropState cropState = crop.getCropState();
            if(cropState == CropState.HARVESTABLE || cropState == CropState.WILTED) {
                crop.grow();
            }
            else {
                if(cropState == CropState.GROWING_WILTED) {
                    crop.setDaysUnwatered(crop.getDaysUnwatered() + 1);
                    if(crop.getDaysUnwatered() == 8)
                        crop.setCropState(CropState.GRASS_MORPH); // Now, it morphs to grass.
                }
                else {
                    if(!isWatered()) {
                        if(cropState == CropState.GROWING) { // We don't want seeds to *morph* to grass, so just don't let them grow
                            crop.setDaysUnwatered(crop.getDaysUnwatered() + 1);
                            if(crop.getDaysUnwatered() == 4) {
                                crop.setCropState(CropState.GROWING_WILTED); // Let's get the crop wilted
                            }
                        }
                    }
                    else { // Farmland is watered, so go on and make the Crop grow. Also, unwater the land.
                        crop.setDaysUnwatered(0);
                        crop.grow();
                    }
                }
            }
        }
        if(isWatered())
            setWatered(false);
    }
}
