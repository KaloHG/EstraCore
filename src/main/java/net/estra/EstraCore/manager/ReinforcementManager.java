package net.estra.EstraCore.manager;

import net.estra.EstraCore.model.citadel.ReinforcementType;

import java.util.ArrayList;
import java.util.List;

public class ReinforcementManager {

    private List<ReinforcementType> activeTypes = new ArrayList<>();


    public void setActiveTypes(List<ReinforcementType> types) {
        activeTypes = types;
    }


}
