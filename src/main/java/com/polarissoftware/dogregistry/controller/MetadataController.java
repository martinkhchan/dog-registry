package com.polarissoftware.dogregistry.controller;

import com.polarissoftware.dogregistry.model.DogStatus;
import com.polarissoftware.dogregistry.model.Gender;
import com.polarissoftware.dogregistry.model.LeavingReason;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;

import java.util.Arrays;
import java.util.List;

@Validated
@Controller("/api/dogs/metadata")
public class MetadataController {

    /**
     * Exposes all valid dog statuses.
     */
    @Get("/statuses")
    public HttpResponse<List<String>> getValidStatuses() {
        List<String> statuses = Arrays.stream(DogStatus.values())
                .map(DogStatus::getValue)
                .toList();
        return HttpResponse.ok(statuses);
    }

    /**
     * Exposes all valid gender options.
     */
    @Get("/genders")
    public HttpResponse<List<String>> getValidGenders() {
        List<String> genders = Arrays.stream(Gender.values())
                .map(Gender::getValue)
                .toList();
        return HttpResponse.ok(genders);
    }

    /**
     * Exposes all valid leaving reasons.
     */
    @Get("/leaving-reasons")
    public HttpResponse<List<String>> getValidLeavingReasons() {
        List<String> reasons = Arrays.stream(LeavingReason.values())
                .map(LeavingReason::getValue)
                .toList();
        return HttpResponse.ok(reasons);
    }
}
