package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.response.ApiPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "BoomUP", description = "BoomUP(좋아요) 관련 API")
public interface BoomUPControllerDocs {

    @Operation(summary = "BoomUP 추가", description = "카드에 BoomUP(좋아요)을 추가합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<Void>> addBoomUP(Authentication authentication, @PathVariable Long cardId);

    @Operation(summary = "BoomUP 취소", description = "카드의 BoomUP(좋아요)을 취소합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<Void>> removeBoomUP(Authentication authentication, @PathVariable Long cardId);
}

