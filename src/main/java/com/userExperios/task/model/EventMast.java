package com.userExperios.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String uniqueId;
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<EventData> eventDataList;

    public EventMast(Object uniqueId) {
        this.uniqueId = uniqueId.toString();
    }
}
