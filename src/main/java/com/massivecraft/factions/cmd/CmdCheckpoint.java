package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.util.SmokeUtil;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CmdCheckpoint extends FCommand
{
    public CmdCheckpoint(){
        super();
        this.aliases.add("checkp");
        this.aliases.add("checkpoint");
        this.aliases.add("cpoint");

        this.optionalArgs.put("set", "");

        this.permission = Permission.CHECKPOINT.node;
        this.disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform(){
        if (!P.p.getConfig().getBoolean("checkpoints.Enabled")){
            fme.msg(TL.COMMAND_CHECKPOINT_DISABLED);
            return;
        }
        if (args.size() == 1){
            FLocation myLocation = new FLocation(fme.getPlayer().getLocation());
            Faction myLocFaction = Board.getInstance().getFactionAt(myLocation);
            if ( myLocFaction == Factions.getInstance().getWilderness() || myLocFaction == fme.getFaction()){
                fme.getFaction().setCheckpoint(fme.getPlayer().getLocation());
                fme.msg(TL.COMMAND_CHECKPOINT_SET);
                return;
            }
        }
        if (fme.getFaction().getCheckpoint() == null){
            fme.msg(TL.COMMAND_CHECKPOINT_NOT_SET);
            return;
        }
        FLocation checkLocation = new FLocation(fme.getFaction().getCheckpoint());
        Faction checkfaction = Board.getInstance().getFactionAt(checkLocation);

        if (checkfaction.getId().equals(Factions.getInstance().getWilderness().getId()) || checkfaction.getId().equals(fme.getFaction().getId())){
            fme.msg(TL.COMMAND_CHECKPOINT_GO);
            fme.getPlayer().teleport(fme.getFaction().getCheckpoint());
        }else{
            fme.msg(TL.COMMAND_CHECKPOINT_CLAIMED);
        }
        this.doWarmUp(WarmUpUtil.Warmup.WARP, TL.WARMUPS_NOTIFY_TELEPORT, "Checkpoint", new Runnable() {
            @Override
            public void run() {
                // Create a smoke effect
                CmdCheckpoint.this.me.teleport(fme.getFaction().getCheckpoint());
            }
        }, this.p.getConfig().getLong("warmups.f-checkpoint", 0));

    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CHECKPOINT_DESCRIPTION;
    }
}