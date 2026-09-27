package com.calc.repository;

import com.calc.entity.CalcRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalcRecordRepository extends JpaRepository<CalcRecord, Long> {
}