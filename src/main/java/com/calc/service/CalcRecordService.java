package com.calc.service;

import com.calc.entity.CalcRecord;
import com.calc.repository.CalcRecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CalcRecordService {
    private final CalcRecordRepository repo;
    public CalcRecordService(CalcRecordRepository repo) {
        this.repo = repo;
    }

    public List<CalcRecord> getAllHistory() {
        return repo.findAll();
    }

    public void saveHistory(String expr, String res) {
        CalcRecord h = new CalcRecord();
        h.setExpression(expr);
        h.setResult(res);
        h.setCreatedAt(java.time.LocalDateTime.now());
        repo.save(h);
    }

    public boolean deleteById(Long id) {
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}