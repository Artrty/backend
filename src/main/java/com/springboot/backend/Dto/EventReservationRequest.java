package com.springboot.backend.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventReservationRequest {
    private Long userId;
    private boolean paymentStatus;
    private boolean rsvConfirmed;
    private boolean rsvCanceled;
}
