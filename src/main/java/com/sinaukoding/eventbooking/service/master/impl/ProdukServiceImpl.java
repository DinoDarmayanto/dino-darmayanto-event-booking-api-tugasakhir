//package com.sinaukoding.eventbooking.service.master.impl;
//
//import com.sinaukoding.eventbooking.builder.CustomBuilder;
//import com.sinaukoding.eventbooking.builder.CustomSpecification;
//import com.sinaukoding.eventbooking.builder.MultipleCriteria;
//import com.sinaukoding.eventbooking.builder.SearchCriteria;
//import com.sinaukoding.eventbooking.entity.master.Produk;
//import com.sinaukoding.eventbooking.entity.master.ProdukImage;
//import com.sinaukoding.eventbooking.mapper.event.EventMapper;
//import com.sinaukoding.eventbooking.mapper.master.ProdukMapper;
//import com.sinaukoding.eventbooking.model.app.AppPage;
//import com.sinaukoding.eventbooking.model.app.SimpleMap;
//import com.sinaukoding.eventbooking.model.filter.ProdukFilterRecord;
//import com.sinaukoding.eventbooking.model.request.LoginRequestRecord;
//import com.sinaukoding.eventbooking.repository.master.ProdukRepository;
//import com.sinaukoding.eventbooking.service.app.ValidatorService;
//import com.sinaukoding.eventbooking.service.master.ProdukService;
//import com.sinaukoding.eventbooking.util.FilterUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ProdukServiceImpl implements ProdukService {
//
//    private final ProdukRepository produkRepository;
//    private final ValidatorService validatorService;
//    private final EventMapper eventMapper;
//
//    @Override
//    public void add(LoginRequestRecord request) {
//        try {
//            log.trace("Masuk ke menu tambah data produk");
//            log.debug("Request data produk: {}", request);
//
//            // validator mandatory
//            validatorService.validator(request);
//
//            if (request.stok() < 0) {
//                log.warn("Stok produk tidak boleh kurang dari 0");
//            }
//
//            var produk = produkMapper.requestToEntity(request);
//            produkRepository.save(produk);
//
//            log.info("Produk {} berhasil ditambahkan", request.nama());
//            log.trace("Tambah data produk berhasil dan selesai");
//        } catch (Exception e) {
//            log.error("Tambah data produk gagal: {}", e.getMessage());
//        }
//    }
//
//    @Override
//    public void edit(LoginRequestRecord request) {
//        // validator mandatory
//        validatorService.validator(request);
//
//        var produkExisting = produkRepository.findById(request.id()).orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));
//        var produk = produkMapper.requestToEntity(request);
//        produk.setId(produkExisting.getId());
//        produkRepository.save(produk);
//    }
//
//    @Override
//    public Page<SimpleMap> findAll(ProdukFilterRecord filterRequest, Pageable pageable) {
//        CustomBuilder<Produk> builder = new CustomBuilder<>();
//
//        FilterUtil.builderConditionNotBlankLike("nama", filterRequest.nama(), builder);
//        FilterUtil.builderConditionNotNullEqual("status", filterRequest.bookingStatus(), builder);
//        FilterUtil.builderConditionNotNullEqual("stok", filterRequest.stok(), builder);
//
//        if (filterRequest.hargaBawah() != null && filterRequest.hargaAtas() != null) {
//            builder.with(MultipleCriteria.builder().criterias(
//                    SearchCriteria.OPERATOR_AND,
//                    SearchCriteria.of("harga", CustomSpecification.OPERATION_GREATER_THAN_EQUAL, filterRequest.hargaBawah()),
//                    SearchCriteria.of("harga", CustomSpecification.OPERATION_LESS_THAN_EQUAL, filterRequest.hargaAtas())
//            ));
//        } else if (filterRequest.hargaAtas() != null) {
//            builder.with("harga", CustomSpecification.OPERATION_LESS_THAN_EQUAL, filterRequest.hargaAtas());
//        } else if (filterRequest.hargaBawah() != null) {
//            builder.with("harga", CustomSpecification.OPERATION_GREATER_THAN_EQUAL, filterRequest.hargaBawah());
//        }
//
//        Page<Produk> listProduk = produkRepository.findAll(builder.build(), pageable);
//        List<SimpleMap> listData = listProduk.stream().map(produk -> {
//            SimpleMap data = new SimpleMap();
//            data.put("id", produk.getId());
//            data.put("nama", produk.getNama());
//            data.put("deskripsi", produk.getDeskripsi());
//            data.put("harga", produk.getHarga());
//            data.put("stok", produk.getStok());
//            data.put("status", produk.getBookingStatus());
//            data.put("createdDate", produk.getCreatedDate());
//            data.put("modifiedDate", produk.getModifiedDate());
//            data.put("listImage", produk.getListImage().stream().map(ProdukImage::getPath).collect(Collectors.toSet()));
//            return data;
//        }).toList();
//
//        return AppPage.create(listData, pageable, listProduk.getTotalElements());
//    }
//
//    @Override
//    public SimpleMap findById(String id) {
//        var produk = produkRepository.findById(id).orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));
//
//        SimpleMap data = new SimpleMap();
//        data.put("id", produk.getId());
//        data.put("nama", produk.getNama());
//        data.put("deskripsi", produk.getDeskripsi());
//        data.put("harga", produk.getHarga());
//        data.put("stok", produk.getStok());
//        data.put("status", produk.getBookingStatus());
//        data.put("createdDate", produk.getCreatedDate());
//        data.put("modifiedDate", produk.getModifiedDate());
//        data.put("listImage", produk.getListImage().stream().map(ProdukImage::getPath).collect(Collectors.toSet()));
//
//        return data;
//    }
//
//}
