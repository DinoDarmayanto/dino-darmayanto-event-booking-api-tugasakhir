package com.sinaukoding.eventbooking.controller.managementuser;

import com.sinaukoding.eventbooking.model.app.AppPage;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.filter.UserFilterRequestRecord;
import com.sinaukoding.eventbooking.model.request.UserRequestRecord;
import com.sinaukoding.eventbooking.model.response.BaseResponse;
import com.sinaukoding.eventbooking.service.managementuser.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "USER API")
public class UserController {

    private final UserService userService;

    @PostMapping("save")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> save(@RequestBody UserRequestRecord request) {
        userService.add(request);
        return BaseResponse.ok("Data berhasil disimpan", null);
    }

    @PostMapping("edit")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> edit(@RequestBody UserRequestRecord request) {
        userService.edit(request);
        return BaseResponse.ok("Data berhasil diubah", null);
    }

    @PostMapping("delete")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> delete(@RequestBody UserRequestRecord request) {
        return BaseResponse.ok("Data berhasil dihapus", userService.delete(request.id()));
    }




    @PostMapping("find-all")
    @Parameters({
            @Parameter(name = "page", description = "Page Number", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0"), required = true),
            @Parameter(name = "size", description = "Size Per Page", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10"), required = true),
            @Parameter(name = "sort", description = "Sorting Data", in = ParameterIn.QUERY, schema = @Schema(type = "string", defaultValue = "updatedAt,desc"), required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<AppPage<SimpleMap>> findAll(
            @RequestBody(required = false) UserFilterRequestRecord filterRequest,
            @PageableDefault(direction = Sort.Direction.DESC, sort = "updatedAt") Pageable pageable
    ) {
        AppPage<SimpleMap> page = (AppPage<SimpleMap>) userService.findAll(filterRequest, pageable);
        return BaseResponse.ok("List user berhasil diambil", page);
    }




    @GetMapping("find-by-id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public BaseResponse<?> findById(@PathVariable String id) {
        return BaseResponse.ok(null, userService.findById(id));
    }
}
