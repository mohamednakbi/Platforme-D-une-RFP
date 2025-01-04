package com.satoripop.rfp.web.rest;

import com.satoripop.rfp.service.dto.Response;
import com.satoripop.rfp.service.impl.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/response")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping("/generate-res")
    public ResponseEntity<Response> generateRandomResponse() {
        Response response = responseService.generateRandomResponse();
        return ResponseEntity.ok(response);
    }
}
