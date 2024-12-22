package com.example.carsharing.repository;

import com.example.carsharing.model.Rental;
import com.example.carsharing.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Long> {
    List<Rental> findRentalByUserAndActualReturnDateIsNull(User user);

    List<Rental> findRentalByUserAndActualReturnDateIsNotNull(User user);
}
