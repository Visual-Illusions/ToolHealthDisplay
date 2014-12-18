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

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.chat.ReceiverType;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.visualillusionsent.minecraft.plugin.ChatFormat;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPluginInformationCommand;
import net.visualillusionsent.toolhealthdisplay.StatusTranslator;
import net.visualillusionsent.toolhealthdisplay.ToolHealthDisplay;
import net.visualillusionsent.toolhealthdisplay.ToolType;

/**
 * Copyright (C) 2014 Visual Illusions Entertainment
 * All Rights Reserved.
 *
 * @author Jason Jones (darkdiplomat)
 */
public final class ToolHealthListener extends VisualIllusionsCanaryPluginInformationCommand implements PluginListener {
    private final String outOf = ChatFormat.CYAN.concat("%s %s%d").concat(ChatFormat.CYAN.toString()).concat("/").concat(ChatFormat.LIGHT_GREEN.toString()).concat("%d");

    public ToolHealthListener(CanaryTHD plugin) throws CommandDependencyException {
        super(plugin);
        plugin.registerCommands(this, false);
        plugin.registerListener(this);
    }

    @Command(
            aliases = { "toolhealthdisplay", "thd" },
            permissions = "",
            description = "Tool Health Display",
            toolTip = "/toolhealthdiplay (/thd)"
    )
    public final void info(MessageReceiver receiver, String[] args){
        super.sendInformation(receiver);
    }

    @Command(
            aliases = { "show" },
            permissions = "toolhealthdisplay.show",
            description = "Tool Health Display",
            toolTip = "/toolhealthdiplay show",
            parent = "toolhealthdisplay"
    )
    public final void show(MessageReceiver receiver, String[] args){
        if(receiver.getReceiverType().equals(ReceiverType.PLAYER)){
            Item held = ((Player)receiver).getItemHeld();
            if(held != null){
                ToolType type = ToolType.fromNativeName(held.getType().getMachineName());
                if(type != ToolType.NONTOOL){
                    ChatFormat color = ChatFormat.RED;
                    double percentile = held.getDamage() / type.getMaximumUsage();

                    if (percentile == 0){
                        color = ChatFormat.GREEN;
                    }
                    if (percentile <= 0.3D){
                        color = ChatFormat.LIGHT_GREEN;
                    }
                    else if (percentile <= 0.6D) {
                        color = ChatFormat.YELLOW;
                    }
                    else if (percentile <= 0.9D) {
                        color = ChatFormat.LIGHT_RED;
                    }

                    receiver.message(String.format(outOf, ((ToolHealthDisplay)getPlugin()).getTranslator().translate("health.is", receiver.getLocale()), color.stringValue(), held.getDamage(), type.getMaximumUsage()));
                    return;
                }
            }
            receiver.notice(((ToolHealthDisplay)getPlugin()).getTranslator().translate("nontool", receiver.getLocale()));
        }
        else {
            receiver.notice("You must be a Player to use ToolHealthDisplay.");
        }
    }

    @HookHandler(priority = Priority.PASSIVE)
    public final void useTool(BlockDestroyHook hook){
        Item held = hook.getPlayer().getItemHeld();
        String locale = hook.getPlayer().getLocale();
        StatusTranslator translator = ((ToolHealthDisplay)getPlugin()).getTranslator();
        if(held != null){
            ToolType tool = ToolType.fromNativeName(held.getType().getMachineName());
            if(tool != ToolType.NONTOOL){
                String msg = null;
                switch (tool.status(held.getDamage())){
                    case WORN:
                        msg = ChatFormat.ORANGE.concat(translator.translate("worn", locale));
                        break;
                    case BADLYWORN:
                        msg = ChatFormat.LIGHT_RED.concat(translator.translate("badlyworn", locale));
                        break;
                    case BREAKING:
                        msg = ChatFormat.RED.concat(translator.translate("breaking", locale));
                        break;
                }

                if(msg != null){
                    hook.getPlayer().message(msg);
                }
            }
        }
    }
}
