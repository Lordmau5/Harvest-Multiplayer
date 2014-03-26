package shared.objects.player;

import shared.objects.AbstractObject;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 14:00
 */
public class Player extends AbstractObject {

    public Player(String textureName, boolean animated) {
        super(textureName, animated);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }
}