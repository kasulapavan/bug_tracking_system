package net.thrymr.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thrymr.task.entity.AppUser;
import net.thrymr.task.enums.Status;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {

    private Long ticketId;

    private  String title;

    private String description;

    private Status ticketStatus;



}
