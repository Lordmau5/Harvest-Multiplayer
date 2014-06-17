package com.lordmau5.harvest.shared.farmable.crops;

import com.lordmau5.harvest.shared.farmable.seeds.ISeeds;
import com.lordmau5.harvest.shared.farmable.seeds.TurnipSeeds;
import com.lordmau5.harvest.shared.util.ImageLoader;
import org.newdawn.slick.Image;

/**
 * @author: Lordmau5
 * @time: 17.06.2014 - 01:02
 */
public class TurnipCrop implements ICrop {
    private Image[] textures = {ImageLoader.loadImage("textures/farmables/turnip.png"), ImageLoader.loadImage("textures/farmables/turnipWilted.png")};
    private CropState cropState = CropState.SEEDS;

    private int daysUnwatered = 0;
    private int cropGrowth;
    private int[] cropChanges = {4, 8, 10, 12};

    public TurnipCrop() {
        this.cropGrowth = 0;
    }

    @Override
    public CropState getCropState() {
        return cropState;
    }

    @Override
    public void setCropState(CropState cropState) {
        this.cropState = cropState;
    }

    @Override
    public void grow() {
        this.cropGrowth += 1;
        if(this.cropGrowth == cropChanges[0]) // Crop is out of seeds-state, now growing
            this.cropState = CropState.GROWING;
        else if(this.cropGrowth == cropChanges[1]) // Crop is harvestable
            this.cropState = CropState.HARVESTABLE;
        else if(this.cropGrowth == cropChanges[2]) // Crop is wilted, unharvestable
            this.cropState = CropState.WILTED;
        else if(this.cropGrowth == cropChanges[3]) // Morph to Grass
            this.cropState = CropState.GRASS_MORPH;
    }

    @Override
    public Image getTexture() {
        return cropState.ordinal() > 2 ? textures[cropState.ordinal() - 3] : null;
    }

    @Override
    public ISeeds getSeeds() {
        return new TurnipSeeds();
    }

    @Override
    public int getDaysUnwatered() {
        return daysUnwatered;
    }

    @Override
    public void setDaysUnwatered(int daysUnwatered) {
        this.daysUnwatered = daysUnwatered;
    }
}
