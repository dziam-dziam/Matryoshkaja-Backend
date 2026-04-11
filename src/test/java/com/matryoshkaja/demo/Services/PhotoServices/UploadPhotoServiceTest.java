package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UploadPhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private PhotoMapper photoMapper;
    @InjectMocks
    private UploadPhotoService uploadPhotoService;

}