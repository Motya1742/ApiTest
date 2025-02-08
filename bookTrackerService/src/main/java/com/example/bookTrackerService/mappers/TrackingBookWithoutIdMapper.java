package com.example.bookTrackerService.mappers;

import com.example.bookTrackerService.models.TrackingBook;
import com.example.bookTrackerService.models.TrackingBookDTO;
import com.example.bookTrackerService.models.TrackingBookWithoutIdDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrackingBookWithoutIdMapper {
    TrackingBookWithoutIdMapper INSTANCE = Mappers.getMapper(TrackingBookWithoutIdMapper.class);

    TrackingBookWithoutIdDTO toDto(TrackingBook trackingBook);

    TrackingBook toEntity(TrackingBookWithoutIdDTO trackingBookWithoutIdDTO);
}
