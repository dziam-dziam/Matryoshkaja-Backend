package com.matryoshkaja.demo.Controllers;

import com.matryoshkaja.demo.Dtos.PhotoCaptionUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoOrderUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoResponseDto;
import com.matryoshkaja.demo.Services.PhotoServices.DeletePhotoService;
import com.matryoshkaja.demo.Services.PhotoServices.GetPhotoService;
import com.matryoshkaja.demo.Services.PhotoServices.UpdatePhotoCaptionService;
import com.matryoshkaja.demo.Services.PhotoServices.UpdatePhotoOrderService;
import com.matryoshkaja.demo.Services.PhotoServices.UploadPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final UploadPhotoService uploadPhotoService;
    private final DeletePhotoService deletePhotoService;
    private final GetPhotoService getPhotoService;
    private final UpdatePhotoOrderService updatePhotoOrderService;
    private final UpdatePhotoCaptionService updatePhotoCaptionService;

    @PostMapping
    public PhotoResponseDto upload(
            @RequestParam("file") MultipartFile multipartFile,
            // CAPTION CHANGE: optional caption sent together with the uploaded file.
            @RequestParam(value = "caption", required = false) String caption
    ){
        return uploadPhotoService.uploadPhoto(multipartFile, caption);
    }

    @GetMapping("/{photoId}")
    public PhotoResponseDto get(@PathVariable Long photoId){
        return getPhotoService.getPhoto(photoId);
    }

    @GetMapping
    public List<PhotoResponseDto> getAll(){
        return getPhotoService.getAllPhotos();
    }

    @DeleteMapping("/{photoId}")
    public void delete(@PathVariable Long photoId) {
        deletePhotoService.deletePhoto(photoId);
    }

    @PutMapping("/order")
    public List<PhotoResponseDto> updateOrder(@RequestBody PhotoOrderUpdateDto request) {
        return updatePhotoOrderService.updateOrder(request);
    }

    @PatchMapping("/{photoId}/caption")
    public PhotoResponseDto updateCaption(
            @PathVariable Long photoId,
            @RequestBody PhotoCaptionUpdateDto request
    ) {
        return updatePhotoCaptionService.updateCaption(photoId, request);
    }
}
