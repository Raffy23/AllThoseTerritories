package pk.risiko.pojo;

import java.util.List;

/**
 * Created by
 *
 * @author Raphael
 * @version 19.01.2016
 */
public interface AI {

    enum AiTroupState {
        ATTACK,MOVE
    }

    /**
     * This Method does calculate a certain Territory on which the AI
     * does want to deploy units
     * @return a Territory from the GameMap
     */
    Territory setUnitAction();

    /**
     * This Method does return a List of all Territories which the AI
     * wants to reinforce with his army
     * @return a list of territories form the GameMap
     */
    List<Territory> reinforceUnitsAction();

    /**
     * This Method des return a List of Tripel which are formatted as give:
     * x=AiTroupState
     * y=Source Territory
     * z=TargetTerritory
     *
     * This List does represent all actions the AI does want to perform in his
     * current round. There may only be one ATTACK but Multiple Move States in
     * it.
     *
     * @return List of all actions the AI does want to do
     */
    List<Tripel<AiTroupState,Territory,Territory>> moveOrAttackAction();

}
