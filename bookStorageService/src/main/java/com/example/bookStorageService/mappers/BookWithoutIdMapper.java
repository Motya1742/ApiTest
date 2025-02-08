package com.example.bookStorageService.mappers;

import com.example.bookStorageService.models.Book;
import com.example.bookStorageService.models.BookWithoutIdDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookWithoutIdMapper {
    BookWithoutIdMapper INSTANCE = Mappers.getMapper(BookWithoutIdMapper.class);

    BookWithoutIdDTO toDTO(Book book);

    Book toEntity(BookWithoutIdDTO bookDTO);
}
