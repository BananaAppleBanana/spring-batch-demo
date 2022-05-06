package com.example.springbatchdemo.batch.demo1;


import org.springframework.batch.item.ItemProcessor;

public class CSVItemProcessor<I, O> implements ItemProcessor<I, O> {
    @Override
    public O process(I i) throws Exception {
        Employee emp = (Employee) i;
        emp.setFirstName(emp.getFirstName() + "======> processed!!!!!!!");
        return (O)emp;
    }
}
