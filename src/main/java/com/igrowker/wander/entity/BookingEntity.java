package com.igrowker.wander.entity;

import com.igrowker.wander.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookings")
public class BookingEntity {

    @Id
    private String id;

    @DBRef
    @NotNull(message = "Experience is required")
    private ExperienceEntity experienceId;

    @DBRef
    @NotNull(message = "User is required")
    private User userId;

    @NotNull(message = "Status is required")
    private BookingStatus status;

    @NotNull(message = "Booking date is required")
    private Date bookingDate;

    @NotNull(message = "Total price is required")
    @Min(value = 0, message = "Total price must be non-negative")
    private double totalPrice;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;

    private String paymentStatus;

    private Date createdAt = new Date();

}
