package com.sinaukoding.eventbooking.service.event.impl;

import com.sinaukoding.eventbooking.builder.CustomBuilder;
import com.sinaukoding.eventbooking.builder.CustomSpecification;
import com.sinaukoding.eventbooking.builder.MultipleCriteria;
import com.sinaukoding.eventbooking.builder.SearchCriteria;
import com.sinaukoding.eventbooking.entity.event.Event;
import com.sinaukoding.eventbooking.entity.managementuser.User;
import com.sinaukoding.eventbooking.mapper.event.EventMapper;
import com.sinaukoding.eventbooking.model.app.AppPage;
import com.sinaukoding.eventbooking.model.app.SimpleMap;
import com.sinaukoding.eventbooking.model.filter.EventFilterRequestRecord;
import com.sinaukoding.eventbooking.model.request.EventRequestRecord;
import com.sinaukoding.eventbooking.repository.event.EventRepository;
import com.sinaukoding.eventbooking.repository.managementuser.UserRepository;
import com.sinaukoding.eventbooking.service.app.ValidatorService;
import com.sinaukoding.eventbooking.service.event.EventService;
import com.sinaukoding.eventbooking.util.FilterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final ValidatorService validatorService;

    @Override
    public void add(EventRequestRecord request) {
        validatorService.validator(request);

        Event event = eventMapper.requestToEntity(request);

        // Ambil user dari security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        event.setCreatedBy((Set<User>) currentUser);

        eventRepository.save(event);
        log.info("Event '{}' berhasil dibuat oleh {}", event.getTitle(), currentUser.getUsername());
    }


    @Override
    public void edit(String id, EventRequestRecord request) {
        validatorService.validator(request);

        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event tidak ditemukan"));

        if (request.endTime().isBefore(request.startTime())) {
            throw new RuntimeException("Waktu selesai harus setelah waktu mulai");
        }

        Event updated = eventMapper.requestToEntity(request);

        updated.setId(existing.getId());
        updated.setCreatedBy(existing.getCreatedBy());
        eventRepository.save(updated);
        log.info("Event {} (id={}) berhasil diupdate", updated.getTitle(), updated.getId());
    }

    @Override
    public SimpleMap delete(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event tidak ditemukan"));

        eventRepository.delete(event);

        return SimpleMap.createMap()
                .add("id", event.getId())
                .add("title", event.getTitle())
                .add("message", "Event berhasil dihapus");
    }

    @Override
    public Page<SimpleMap> findAll(EventFilterRequestRecord filterRequest, Pageable pageable) {
        CustomBuilder<Event> builder = new CustomBuilder<>();

        if (filterRequest != null) {
            // date range -> startTime between startDate and endDate
            if (filterRequest.startDate() != null && filterRequest.endDate() != null) {
                builder.with(MultipleCriteria.builder().criterias(
                        SearchCriteria.OPERATOR_AND,
                        SearchCriteria.of("startTime", CustomSpecification.OPERATION_GREATER_THAN_EQUAL, filterRequest.startDate()),
                        SearchCriteria.of("startTime", CustomSpecification.OPERATION_LESS_THAN_EQUAL, filterRequest.endDate())
                ));
            } else {
                if (filterRequest.startDate() != null) {
                    builder.with("startTime", CustomSpecification.OPERATION_GREATER_THAN_EQUAL, filterRequest.startDate());
                }
                if (filterRequest.endDate() != null) {
                    builder.with("startTime", CustomSpecification.OPERATION_LESS_THAN_EQUAL, filterRequest.endDate());
                }
            }

            // price range
            if (filterRequest.minPrice() != null && filterRequest.maxPrice() != null) {
                builder.with(MultipleCriteria.builder().criterias(
                        SearchCriteria.OPERATOR_AND,
                        SearchCriteria.of("price", CustomSpecification.OPERATION_GREATER_THAN_EQUAL, filterRequest.minPrice()),
                        SearchCriteria.of("price", CustomSpecification.OPERATION_LESS_THAN_EQUAL, filterRequest.maxPrice())
                ));
            } else {
                if (filterRequest.minPrice() != null) {
                    builder.with("price", CustomSpecification.OPERATION_GREATER_THAN_EQUAL, filterRequest.minPrice());
                }
                if (filterRequest.maxPrice() != null) {
                    builder.with("price", CustomSpecification.OPERATION_LESS_THAN_EQUAL, filterRequest.maxPrice());
                }
            }

            // published
            if (filterRequest.published() != null) {
                FilterUtil.builderConditionNotNullEqual("published", filterRequest.published(), builder);
            }
        }

        Page<Event> page = eventRepository.findAll(builder.build(), pageable);

        List<SimpleMap> data = page.stream()
                .map(e -> eventMapper.toSimpleMap(e, false))
                .collect(Collectors.toList());

        return AppPage.create(data, pageable, page.getTotalElements());
    }


    @Override
    public SimpleMap findById(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event tidak ditemukan"));

        return eventMapper.toSimpleMap(event, true);
    }


}
