package com.wafflestudio.babble.chat.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.wafflestudio.babble.chat.presentation.dto.CreateChatRoomRequest;
import com.wafflestudio.babble.common.presentation.AuthUserId;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

public interface SwaggerChatController {

    @ApiImplicitParams({
        @ApiImplicitParam(
            // NOTE: name에 어떤 값을 입력하는지에 따라 애플리케이션 실행이 안되거나 Example Value에 표시되지 않을 수 있다.
            // 우선순위가 낮으므로 디버깅을 보류하고 임의의 문자열을 사용한다.
            name = "ABC",
            required = true,
            paramType = "body", dataTypeClass = CreateChatRoomRequest.class)
    })
    @ApiOperation(value = "로그인한 유저의 현재 위치를 기반으로 채팅방을 생성하고, 본인이 방장이 된다.")
    ResponseEntity<Void> createRoom(@AuthUserId String authId,
                                    @RequestBody CreateChatRoomRequest requestBody);
}
