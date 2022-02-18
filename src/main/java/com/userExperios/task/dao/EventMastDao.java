package com.userExperios.task.dao;

import com.userExperios.task.model.EventMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventMastDao extends JpaRepository<EventMast,Long> {

    @Query("select x from EventMast x where x.uniqueId=:uniqueId")
    EventMast getEventMastUniqueId(String uniqueId);
}
