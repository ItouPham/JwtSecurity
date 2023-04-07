package com.springboot.JwtSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.JwtSecurity.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
