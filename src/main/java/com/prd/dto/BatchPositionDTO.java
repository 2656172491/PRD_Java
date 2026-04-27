package com.prd.dto;

import lombok.Data;

import java.util.List;

@Data
public class BatchPositionDTO {
    private List<PositionItem> positions;

    @Data
    public static class PositionItem {
        private Long id;
        private Double positionX;
        private Double positionY;
    }
}
