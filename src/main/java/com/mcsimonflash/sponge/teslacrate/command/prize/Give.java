package com.mcsimonflash.sponge.teslacrate.command.prize;

import com.google.inject.Inject;
import com.mcsimonflash.sponge.teslacrate.TeslaCrate;
import com.mcsimonflash.sponge.teslacrate.component.prize.Prize;
import com.mcsimonflash.sponge.teslalibs.command.*;
import org.spongepowered.api.command.*;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.mcsimonflash.sponge.teslacrate.command.CmdUtils.*;

@Aliases({"give"})
@Permission("teslacrate.command.prize.give.base")
public final class Give extends Command {

    @Inject
    private Give(Settings settings) {
        super(settings.usage(usage("/teslacrate prize give ", "Gives a prize to a set of users.", USERS_ARG, PRIZE_ARG)).elements(USERS_ELEM, PRIZE_ELEM));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Collection<User> users = args.getAll("users");
        if (users.isEmpty()) {
            throw new CommandException(TeslaCrate.getMessage(src, "teslacrate.command.no-users"));
        }
        Prize prize = args.<Prize>getOne("prize").get();
        users.forEach(prize::give);
        src.sendMessage(TeslaCrate.getMessage(src, "teslacrate.command.prize.give.success", "user", users.size() == 1 ? users.iterator().next().getName() : Text.builder("[users]")
                .onHover(TextActions.showText(Text.of(users.stream().map(User::getName).collect(Collectors.toList()))))
                .build(), "prize", prize.getId(), "value", prize.getValue()));
        return CommandResult.successCount(users.size());
    }

}