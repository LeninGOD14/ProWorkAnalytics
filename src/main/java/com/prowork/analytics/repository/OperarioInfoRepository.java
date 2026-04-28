package com.prowork.analytics.repository;

import com.prowork.analytics.model.OperarioInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperarioInfoRepository extends JpaRepository<OperarioInfo, Long> {

}
