package com.example.bookTrackerService.mappers;

import com.example.bookTrackerService.models.TrackingBook;
import com.example.bookTrackerService.models.TrackingBookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrackingBookMapper {
    TrackingBookMapper INSTANCE = Mappers.getMapper(TrackingBookMapper.class);

    TrackingBookDTO toDto(TrackingBook trackingBook);

    TrackingBook toEntity(TrackingBookDTO trackingBookDTO);
}
