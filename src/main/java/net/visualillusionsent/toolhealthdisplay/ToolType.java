/*
 * This file is part of ToolHealthDisplay.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
 *
 * ToolHealthDisplay is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 */
package net.visualillusionsent.toolhealthdisplay;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * Copyright (C) 2014 Visual Illusions Entertainment
 * All Rights Reserved.
 *
 * @author Jason Jones (darkdiplomat)
 */
public enum ToolType {

    NONTOOL("NOPE", Integer.MAX_VALUE),
    // WOOD TOOLS
    WOODENHOE("wooden_hoe", 60),
    WOODENSHOVEL("wooden_shovel", 60),
    WOODENPICKAXE("wooden_pickaxe", 60),
    WOODENAXE("wooden_axe", 60),
    // STONE TOOLS
    STONEHOE("stone_hoe", 132),
    STONESHOVEL("stone_shovel", 132),
    STONEPICKAXE("stone_pickaxe", 132),
    STONEAXE("stone_axe", 132),
    // IRON TOOLS
    IRONHOE("iron_hoe", 251),
    IRONSHOVEL("iron_shovel", 251),
    IRONPICKAXE("iron_pickaxe", 251),
    IRONAXE("iron_axe", 251),
    // GOLD TOOLS
    GOLDHOE("golden_hoe", 32),
    GOLDSHOVEL("golden_shovel", 32),
    GOLDPICKAXE("golden_pickaxe", 32),
    GOLDAXE("golden_axe", 32),
    // DIAMOND TOOLS
    DIAMONDHOE("diamond_hoe", 1562),
    DIAMONDSHOVEL("diamond_shovel", 1562),
    DIAMONDPICKAXE("diamond_pickaxe", 1562),
    DIAMONDAXE("diamond_axe", 1562),
    // OTHER
    SHEARS("shears", 238),

    ;

    private static HashMap<String, ToolType> idMap = Maps.newHashMap();
    private final String nmsName;
    private final int maxUse;

    private ToolType(String nmsName, int maxUse){
        this.nmsName = nmsName;
        this.maxUse = maxUse;
    }

    public static ToolType fromNativeName(String nmsName){
        String stripped = nmsName.replaceAll("(minecraft:)?(\\w)(:.+)?", "$2");
        //System.out.println("Original: " + nmsName + " Stripped: " + stripped);
        return idMap.containsKey(stripped) ? idMap.get(stripped) : NONTOOL;
    }

    public final ToolStatus status(int currentDamage){
        switch (maxUse - currentDamage) {
            case 11:
                return ToolStatus.WORN;
            case 6:
                return ToolStatus.BADLYWORN;
            case 2:
                return ToolStatus.BREAKING;
            default:
                return ToolStatus.FINE;
        }
    }

    public final int getMaximumUsage(){
        return maxUse;
    }

    static {
        for(ToolType type : ToolType.values()){
            idMap.put(type.nmsName, type);
        }
    }
}
