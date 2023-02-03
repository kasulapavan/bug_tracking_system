package net.thrymr.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thrymr.task.entity.Ticket;
import net.thrymr.task.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyModuleDto {
    private Long moduleId;

    private String name;

    private  String priority;

    private LocalDate createdOn;

    private Status moduleStatus;

    private  List<TicketDto> ticketList;






}
