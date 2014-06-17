package com.lordmau5.harvest.client.farmable.crops;

import com.lordmau5.harvest.client.farmable.seeds.ISeeds;
import com.lordmau5.harvest.client.items.IUseable;
import com.lordmau5.harvest.client.textures.ITexture;

/**
 * @author: Lordmau5
 * @time: 17.06.2014 - 01:02
 */
public interface ICrop extends IUseable, ITexture {

    public abstract ISeeds getSeeds();

    public abstract CropState getCropState();

    public abstract void setCropState(CropState cropState);

    public abstract int getDaysUnwatered();

    public abstract void setDaysUnwatered(int daysUnwatered);

    public abstract void grow();

}
