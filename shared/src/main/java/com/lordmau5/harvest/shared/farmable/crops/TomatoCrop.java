package com.lordmau5.harvest.shared.farmable.crops;

import com.lordmau5.harvest.shared.farmable.seeds.ISeeds;
import com.lordmau5.harvest.shared.farmable.seeds.TomatoSeeds;
import com.lordmau5.harvest.shared.util.ImageLoader;
import org.newdawn.slick.Image;

/**
 * @author: Lordmau5
 * @time: 17.06.2014 - 01:03
 */
public class TomatoCrop implements ICrop {
    private Image[] textures = {ImageLoader.loadImage("textures/farmables/tomato.png"), ImageLoader.loadImage("textures/farmables/tomatoWilted.png")};
    private CropState cropState = CropState.SEEDS;

    private int daysUnwatered = 0;
    private int cropGrowth;
    private int[] cropChanges = {4, 8, 10, 12};

    public TomatoCrop() {
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
        if(this.cropGrowth == cropChanges[0])
            this.cropState = CropState.GROWING;
        else if(this.cropGrowth == cropChanges[1])
            this.cropState = CropState.HARVESTABLE;
        else if(this.cropGrowth == cropChanges[2])
            this.cropState = CropState.WILTED;
        else if(this.cropGrowth == cropChanges[3])
            this.cropState = CropState.GRASS_MORPH;
    }

    @Override
    public ISeeds getSeeds() {
        return new TomatoSeeds();
    }

    @Override
    public Image getTexture() {
        return cropState.ordinal() > 2 ? textures[cropState.ordinal() - 3] : null;
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
