//package com.sinaukoding.eventbooking;
//
//import com.sinaukoding.eventbooking.entity.master.Produk;
//import com.sinaukoding.eventbooking.mapper.master.ProdukMapper;
//import com.sinaukoding.eventbooking.model.enums.BookingStatus;
//import com.sinaukoding.eventbooking.model.request.LoginRequestRecord;
//import com.sinaukoding.eventbooking.repository.master.ProdukRepository;
//import com.sinaukoding.eventbooking.service.app.ValidatorService;
//import com.sinaukoding.eventbooking.service.master.impl.ProdukServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProdukServiceTest {
//
//    @Mock
//    private ProdukRepository produkRepository;
//
//    @Mock
//    private ValidatorService validatorService;
//
//    @Mock
//    private ProdukMapper produkMapper;
//
//    @InjectMocks
//    private ProdukServiceImpl produkService;
//
//    @Test
//    void testAddProduk_Success() {
//        Set<String> listImage = new HashSet<>();
//        listImage.add("path1");
//
//        var request = new LoginRequestRecord(null, "Macbook Air M1", "Macbook Air M1",
//                10000000D, 10, BookingStatus.AKTIF, listImage);
//
//        var produkEntity = new Produk();
//        when(produkMapper.requestToEntity(request)).thenReturn(produkEntity);
//
//        // when
//        produkService.add(request);
//
//        // then
//        verify(validatorService, times(1)).validator(request);
//        verify(produkRepository, times(1)).save(produkEntity);
//    }
//
//}
