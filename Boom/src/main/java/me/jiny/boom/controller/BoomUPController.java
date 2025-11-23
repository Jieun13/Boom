package me.jiny.boom.controller;

import me.jiny.boom.controller.docs.BoomUPControllerDocs;
import me.jiny.boom.dto.response.ApiPayload;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boomups")
public class BoomUPController implements BoomUPControllerDocs {

    @Override
    @PostMapping("/cards/{cardId}")
    public ApiPayload<Void> addBoomUP(@PathVariable Long cardId) {
        return null;
    }

    @Override
    @DeleteMapping("/cards/{cardId}")
    public ApiPayload<Void> removeBoomUP(@PathVariable Long cardId) {
        return null;
    }
}

