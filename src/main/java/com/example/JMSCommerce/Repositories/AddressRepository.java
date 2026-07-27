package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address>  findByIdAndUser_Id(Long id, Long currentUserId);

    Collection<Address> findByUser_IdOrderByDefaultAddressDescCreatedAtDesc(Long currentUserId);

    List<Address> findByUser_Id(Long id);

    Optional<Address> findFirstByUser_IdOrderByCreatedAtAsc(Long currentUserId);

//    Iterable<Object> findByUser_email(String );
}
