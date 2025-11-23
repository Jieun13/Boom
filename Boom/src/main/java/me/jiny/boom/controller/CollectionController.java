package me.jiny.boom.controller;

import me.jiny.boom.controller.docs.CollectionControllerDocs;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CardSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections")
public class CollectionController implements CollectionControllerDocs {

    @Override
    @PostMapping("/cards/{cardId}")
    public ApiPayload<Void> addCollection(@PathVariable Long cardId) {
        return null;
    }

    @Override
    @DeleteMapping("/cards/{cardId}")
    public ApiPayload<Void> removeCollection(@PathVariable Long cardId) {
        return null;
    }

    @Override
    @GetMapping("/me")
    public ApiPayload<Page<CardSimpleResponse>> getMyCollections(Pageable pageable) {
        return null;
    }
}

