package com.calc.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE})
public class CalcController {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final List<Map<String,Object>> historyList = new ArrayList<>();
    private final AtomicInteger idGen = new AtomicInteger(1);

    @PostMapping("/calculate")
    public Map<String,Object> calculate(@RequestBody Map<String,String> body){
        Map<String,Object> map = new HashMap<>();
        String expression = body.get("expression");
        try{
            Expression exp = parser.parseExpression(expression);
            Double res = exp.getValue(Double.class);
            map.put("success",true);
            map.put("expression",expression);
            map.put("result",res);

            //存入历史记录
            Map<String,Object> item = new HashMap<>();
            item.put("id", idGen.getAndIncrement());
            item.put("expression", expression);
            item.put("result", res);
            item.put("createdAt", new Date().toString());
            historyList.add(item);

        }catch (Exception e){
            map.put("success",false);
            map.put("message","表达式错误");
        }
        return map;
    }

    @GetMapping("/history")
    public List<Map<String,Object>> getHistory(){
        return historyList;
    }

    @DeleteMapping("/history/{id}")
    public String deleteHistory(@PathVariable Integer id){
        historyList.removeIf(item -> id.equals(item.get("id")));
        return "success";
    }
}