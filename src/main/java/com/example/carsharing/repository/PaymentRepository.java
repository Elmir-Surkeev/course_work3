package com.example.carsharing.repository;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN p.rental r JOIN r.user u WHERE u = :user")
    List<Payment> getByUser(@Param("user") User user);

    @Query("FROM Payment WHERE sessionId = :sessionId")
    Payment getBySessionId(@Param("sessionId") String sessionId);

}
