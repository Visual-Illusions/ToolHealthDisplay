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
package net.visualillusionsent.toolhealthdisplay.canary;

import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;
import net.visualillusionsent.toolhealthdisplay.StatusTranslator;
import net.visualillusionsent.toolhealthdisplay.ToolHealthDisplay;
import net.visualillusionsent.utils.PropertiesFile;

/**
 * Copyright (C) 2014 Visual Illusions Entertainment
 * All Rights Reserved.
 *
 * @author Jason Jones (darkdiplomat)
 */
public final class CanaryTHD extends VisualIllusionsCanaryPlugin implements ToolHealthDisplay{
    private StatusTranslator translator;

    @Override
    public final boolean enable(){
        try {
            super.enable();
            verifyConfiguration();
            translator = new StatusTranslator(this, getConfig().getString("defaultLocale"), getConfig().getBoolean("updateLang"));
            new ToolHealthListener(this);

            // Good, return true
            return true;
        }
        catch (Exception ex) {
            String reason = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            if (debug) { // Only stack trace if debugging
                getLogman().error("ToolHealthDisplay failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogman().error("ToolHealthDisplay failed to start. Reason: ".concat(reason));
            }
        }
        // And its a failure!
        return false;
    }

    @Override
    public final StatusTranslator getTranslator() {
        return translator;
    }

    private void verifyConfiguration() {
        PropertiesFile cfg = getConfig();
        cfg.getBoolean("updateLang", true);
        cfg.setComments("updateLang", "Whether to auto-update the language files or not");
        cfg.getString("defaultLocale", "en_US");
        cfg.setComments("The default language locale fall back to if a locale isn't found for a user.");
        cfg.save();
    }
}
