package com.sinaukoding.eventbooking.controller.event;

import com.sinaukoding.eventbooking.model.app.AppPage;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.filter.EventFilterRequestRecord;
import com.sinaukoding.eventbooking.model.filter.UserFilterRequestRecord;
import com.sinaukoding.eventbooking.model.request.EventRequestRecord;
import com.sinaukoding.eventbooking.model.request.UserRequestRecord;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import com.sinaukoding.eventbooking.service.event.EventService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("event")
@RequiredArgsConstructor
@Tag(name = "Event API")
public class EventController {

    private final EventService eventService;

    @PostMapping("save")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> save(@RequestBody EventRequestRecord request) {
        eventService.add(request);
        return BaseResponse.ok("Event berhasil disimpan", null);
    }

    @PostMapping("edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> edit(@PathVariable String id, @RequestBody EventRequestRecord request) {
        eventService.edit(id, request);
        return BaseResponse.ok("Event berhasil diubah", null);
    }


    @PostMapping("delete")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> delete(@RequestBody EventRequestRecord request) {
        return BaseResponse.ok("Data berhasil dihapus", eventService.delete(request.id()));
    }

    @PostMapping("find-all")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    @Parameters({
            @Parameter(name = "page", description = "Page Number", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0"), required = true),
            @Parameter(name = "size", description = "Size Per Page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10"), required = true),
            @Parameter(name = "sort", description = "Sorting Data (ex: startTime,desc)", in = ParameterIn.QUERY, schema = @Schema(type = "string", defaultValue = "startTime,desc"), required = true)
    })
    public BaseResponse<AppPage<SimpleMap>> findAll(
            @RequestBody(required = false) EventFilterRequestRecord filterRequest,
            @PageableDefault(direction = Sort.Direction.DESC, sort = "startTime") Pageable pageable
    ) {
        AppPage<SimpleMap> page = eventService.findAll(filterRequest, pageable);
        return BaseResponse.ok("List event berhasil diambil", page);
    }


    @GetMapping("find-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> findById(@PathVariable String id) {
        return BaseResponse.ok(null, eventService.findById(id));
    }


}
