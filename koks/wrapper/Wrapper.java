package koks.wrapper;

import koks.api.Methods;
import koks.api.util.*;

/**
 * @author kroko
 * @created on 28.10.2020 : 20:58
 */
public class Wrapper {

    public ESPUtil espUtil = new ESPUtil();
    public Logger logger = new Logger();
    public LoginUtil loginUtil = new LoginUtil();
    public MovementUtil movementUtil = new MovementUtil();
    public RandomUtil randomUtil = new RandomUtil();
    public RayCastUtil rayCastUtil = new RayCastUtil();
    public RenderUtil renderUtil = new RenderUtil();
    public RotationUtil rotationUtil = new RotationUtil();
    public Methods methods = new Methods();
}
