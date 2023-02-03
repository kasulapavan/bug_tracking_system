package net.thrymr.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thrymr.task.dto.AppUserDto;
import net.thrymr.task.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private  String title;
    @Column(columnDefinition = "Text")

    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false)
    private Status ticketStatus;


}
