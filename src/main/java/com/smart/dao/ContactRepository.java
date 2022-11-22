package com.smart.dao;

import com.smart.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

    //pagination....

    @Query("from Contact as c where c.user.id =:userId")
    public List<Contact> findContactsByUser(@Param("userId") int userId);


}
