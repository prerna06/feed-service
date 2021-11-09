package com.tekcapsule.capsule.application.mapper;

import com.tekcapsule.capsule.application.function.input.*;
import com.tekcapsule.capsule.domain.command.*;
import com.tekcapsule.core.domain.Command;
import com.tekcapsule.core.domain.ExecBy;
import com.tekcapsule.core.domain.Origin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.BiFunction;

@Slf4j
public final class InputOutputMapper {
    private InputOutputMapper() {

    }

    public static final BiFunction<Command, Origin, Command> addOrigin = (command, origin) -> {
        OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
        command.setChannel(origin.getChannel());
        command.setExecBy(ExecBy.builder().tenantId(origin.getTenantId()).userId(origin.getUserId()).build());
        command.setExecOn(utc.toString());
        return command;
    };

    public static final BiFunction<CreateInput, Origin, CreateCommand> buildCreateCommandFromCreateInput = (createInput, origin) -> {
        CreateCommand createCommand =  CreateCommand.builder().build();
        BeanUtils.copyProperties(createInput, createCommand);
        addOrigin.apply(createCommand, origin);
        return createCommand;
    };

    public static final BiFunction<UpdateInput, Origin, UpdateCommand> buildUpdateCommandFromUpdateInput = (updateInput, origin) -> {
        UpdateCommand updateCommand = UpdateCommand.builder().build();
        BeanUtils.copyProperties(updateInput, updateCommand);
        addOrigin.apply(updateCommand, origin);
        return updateCommand;
    };

    public static final BiFunction<DisableInput, Origin, DisableCommand> buildDisableCommandFromDisableInput = (disableInput, origin) -> {
        DisableCommand disableCommand =  DisableCommand.builder().build();
        BeanUtils.copyProperties(disableInput, disableCommand);
        addOrigin.apply(disableCommand, origin);
        return disableCommand;
    };


    public static final BiFunction<RecommendInput, Origin, RecommendCommand> buildRecommendCommandFromRecommendInput = (recommendInput, origin) -> {
        RecommendCommand recommendCommand =  RecommendCommand.builder().build();
        BeanUtils.copyProperties(recommendInput, recommendCommand);
        addOrigin.apply(recommendCommand, origin);
        return recommendCommand;
    };

    public static final BiFunction<ApproveCapsuleInput, Origin, ApproveCommand> buildApproveCapsuleCommandFromApproveCapsuleInput = (approveCapsuleInput, origin) -> {
        ApproveCommand approveCommand =  ApproveCommand.builder().build();
        BeanUtils.copyProperties(approveCapsuleInput, approveCommand);
        addOrigin.apply(approveCommand, origin);
        return approveCommand;
    };

    public static final BiFunction<AddBookmarkInput, Origin, AddBookmarkCommand> buildAddBookmarkCommandFromAddBookmarkInput = (addBookmarkInput, origin) -> {
        AddBookmarkCommand addBookmarkCommand =  AddBookmarkCommand.builder().build();
        BeanUtils.copyProperties(addBookmarkInput, addBookmarkCommand);
        addOrigin.apply(addBookmarkCommand, origin);
        return addBookmarkCommand;
    };

    public static final BiFunction<ViewInput, Origin, ViewCommand> buildViewCommandFromViewInput = (viewInput, origin) -> {
        ViewCommand viewCommand =  ViewCommand.builder().build();
        BeanUtils.copyProperties(viewInput, viewCommand);
        addOrigin.apply(viewCommand, origin);
        return viewCommand;
    };
}
