package koks.wrapper;

import koks.api.Methods;
import koks.api.util.*;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author kroko
 * @created on 30.10.2020 : 17:24
 */
public class Wrapper {

    public final ESPUtil espUtil = new ESPUtil();
    public final Logger logger = new Logger();
    public final LoginUtil loginUtil = new LoginUtil();
    public final MovementUtil movementUtil = new MovementUtil();
    public final RandomUtil randomUtil = new RandomUtil();
    public final RayCastUtil rayCastUtil = new RayCastUtil();
    public final RenderUtil renderUtil = new RenderUtil();
    public final RotationUtil rotationUtil = new RotationUtil();
    public final InventoryUtil inventoryUtil = new InventoryUtil();
    public final NumbersUtil numberUtil = new NumbersUtil();

}
