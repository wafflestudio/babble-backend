package com.wafflestudio.babble.auth.presentation;

import org.springframework.http.ResponseEntity;

import com.wafflestudio.babble.auth.presentation.dto.LoginResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

public interface SwaggerAuthController {

    @ApiOperation(value = "로그인하고 토큰을 발급한다. 관련 회원이 없다면 자동으로 회원가입까지 진행한다.")
    @ApiImplicitParams({
        @ApiImplicitParam(
            value = "카카오 인증 후 리다이렉션 주소로 되돌아오면서 전달받은 인가코드",
            name = "code",
            required = true,
            paramType = "query", dataTypeClass = String.class)
    })
    ResponseEntity<LoginResponse> login(String code);
}
