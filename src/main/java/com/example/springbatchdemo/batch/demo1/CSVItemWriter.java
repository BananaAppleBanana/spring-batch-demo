package com.example.springbatchdemo.batch.demo1;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

public class CSVItemWriter<T> extends FlatFileItemWriter<T> {

    public CSVItemWriter(Resource resource) {
        setResource(resource);
        setLineAggregator(getDelimitedLineAggregator());
        this.setHeaderCallback(w -> w.write("col1/col2/col3"));
    }

    public DelimitedLineAggregator<T> getDelimitedLineAggregator() {
        BeanWrapperFieldExtractor<T> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] {"id", "firstName", "lastName"});

        DelimitedLineAggregator<T> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter("/");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
        return delimitedLineAggregator;
    }
}
